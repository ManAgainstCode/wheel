package cn.larry.sample.compress;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
/**
 * lzw的初步改进版本，使用byte数据取代String，防止了频繁的gc
 * @author larryfu
 *
 */
public class LZW {
	private static final int R = 256;
	private static final int L = 4096;
	private static final int W = 12;

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
		int unit = 32;
		BinaryOutputStream bos = new BinaryOutputStream(new DataOutputStream(new FileOutputStream(outpath)));
		ByteTrie<Integer> st = new ByteTrie<>();
		for (char i = 0; i < R; i++)
			st.put(char2Byte(i), (int)i);
		int code = R + 1;
		
		try(DataInputStream dis = new DataInputStream(new FileInputStream(inpath))){
			while(dis.available() >0 ){
				byte[] bs = new byte[1024*unit];
				int len = dis.read(bs, 0, 1024*unit);
				if(len != 1024*unit)
					bs = subByte(0,len,bs);
				while(bs.length >0){
					byte[] b = st.longestPrefixOf(bs);
					bos.write(st.get(b),W);
					int t = b.length;
					if(t<bs.length && code < L)
						st.put(subByte(0, t+1, bs), code ++);
					bs = subByte(t, bs);
				}
			}
			bos.write(R, W);
			bos.close();
		}
	}

	public static void expand(String inpath, String outpath) throws IOException {
		System.out.println("expand start");
		BinaryInputStream bis = new BinaryInputStream(new FileInputStream(inpath));
		BinaryOutputStream bos = new BinaryOutputStream(new DataOutputStream(new FileOutputStream(outpath)));
		String[] st = new String[L];
		int i;
		for (i = 0; i < R; i++)
			st[i] = "" + (char) i;
		st[i++] = " ";
		int codeword = bis.readInt(W);
		String val = st[codeword];
		while (true) {
			//System.out.println("expand round: "+ ++round);
			bos.write(val);
			codeword = bis.readInt(W);
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

