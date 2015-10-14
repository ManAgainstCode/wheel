package cn.larry.sample.compress;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * lzw算法的性能改进版，<br/>
 * 1使用bytesequence取代了string防止了频繁的substring带来的巨大垃圾回收开销<br/>
 * 2采用缓存，每次从文件读取4k字节数据，从而极大提高了IO的速度<br/>
 * 经过这两个方面的改进后压缩解压速度已达到1-10M/s,与主流压缩软件相近
 * @author larryfu
 *
 */
public class LZWAdv {
	private static final int R = 256;
	private static final int L = 4096;
	

	public static void main(String[] args) {
		try {
			long start = System.currentTimeMillis();
			compress("d:\\test1.txt", "d:\\test2.txt");
			long compressed = System.currentTimeMillis();
			expand("d:\\test2.txt","d:\\test3.txt");
			long end= System.currentTimeMillis();
			long compressTime = compressed - start;
			long expandTime = end - compressed;
			System.out.println("compress time:"+compressTime);
			System.out.println("expand time:"+expandTime);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void compress(String inpath, String outpath) throws IOException {
		System.out.println("compress start");
		int unit = 16;
		BinOutputStream bos = new BinOutputStream(new DataOutputStream(new FileOutputStream(outpath)));
		ByteSeqTrie<Integer> st = new ByteSeqTrie<>();
		for (char i = 0; i < R; i++)
			st.put(new ByteSequence(char2Byte(i)), (int)i);
		int code = R + 1;
		
		try(DataInputStream dis = new DataInputStream(new FileInputStream(inpath))){
			while(dis.available() >0 ){
				byte[] bs = new byte[1024*unit];
				int len = dis.read(bs, 0, 1024*unit);
				if(len != 1024*unit)
					bs = subByte(0,len,bs);
				
				ByteSequence bys = new ByteSequence(bs);
				
				while(bys.length() >0){
					ByteSequence b = st.longestPrefixOf(bys);
					bos.writeOneHalfByte(st.get(b));
					int t = b.length();
					if(t<bys.length() && code < L)
						st.put(bys.subByteSeq(0, t+1), code ++);
					bys.forward(t);
				}
			}
			bos.writeOneHalfByte(R);
			bos.close();
		}
	}

	public static void expand(String inpath, String outpath) throws IOException {
		System.out.println("expand start");
		BinInputStream bis = new BinInputStream(new FileInputStream(inpath));
		BinOutputStream bos = new BinOutputStream(new DataOutputStream(new FileOutputStream(outpath)));
		String[] st = new String[L];
		int i;
		for (i = 0; i < R; i++)
			st[i] = "" + (char) i;
		st[i++] = " ";
		int codeword = bis.readInt();
		String val = st[codeword];
		int round = 0;
		while (true) {
			//System.out.println("expand round: "+ ++round);
			bos.writeString(val);
			codeword = bis.readInt();
			if (codeword == R)
				break;
			String s = st[codeword];
			if (i == codeword)
				s = val + val.charAt(0);
			if (i < L)
				st[i++] = val + s.charAt(0);
			val = s;
		}
		bos.close();
		bis.close();
	}
	
	private static byte[] char2Byte(char c){
		byte[] bs = new byte[1];
		bs[0] = (byte) c;
		return bs;
	}
	
	public static byte[] subByte(int start,int end,byte[] bs){
		byte[] b = new byte[end-start];
		System.arraycopy(bs, start, b, 0, end-start);
		return b;
	}
	
	public static byte[] subByte(int start,byte[] bs){
		byte[] b = new byte[bs.length-start];
		System.arraycopy(bs, start, b, 0, bs.length-start);
		return b;
	}
}

