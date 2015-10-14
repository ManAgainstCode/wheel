package cn.larry.sample.compress;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class LZWC {
	private static final int R = 256;
	private static final int L = 4096;
	private static final int W = 12;

	private static String readFileAsString(String path) throws IOException {
		StringBuilder sb = new StringBuilder();
		try (DataInputStream dis = new DataInputStream(new FileInputStream(path))) {
			while (dis.available()>0)
				sb.append((char)dis.readByte());	
			}
		return sb.toString() ;
	}
	
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
		String inputs = readFileAsString(inpath);
		System.out.println("data read");
		BinaryOutputStream bos = new BinaryOutputStream(new DataOutputStream(new FileOutputStream(outpath)));
		Trie<Integer> st = new Trie<>();
		for (int i = 0; i < R; i++)
			st.put("" + (char) i, i);
		int code = R + 1;
		while (inputs.length() > 0) {
			//System.out.println("compress input length:"+input.length());
			String s = st.longestPrefixOf(inputs);
			bos.write(st.get(s), W);

			int t = s.length();
			if (t < inputs.length() && code < L) {
				st.put(inputs.substring(0, t + 1), code++);
			}else if(t<inputs.length() && code == L ){
				code ++;
			}else if(t<inputs.length() && code == L+1){
				st = new Trie<>();
				for (char i = 0; i < R; i++)
					st.put(""+ i, (int)i);
				code = R + 1;
			}
			inputs = inputs.substring(t);
		}
		bos.write(R, W);
		bos.close();
	}

	public static void expand(String inpath, String outpath) throws IOException {
		System.out.println("expand start");
		BinaryInputStream bis = new BinaryInputStream(new FileInputStream(inpath));
		BinaryOutputStream bos = new BinaryOutputStream(new DataOutputStream(new FileOutputStream(outpath)));
		String[] st = new String[L];
		int i;
		int codeword; 
		String val; 
		for (i = 0; i < R; i++)
			st[i] = "" + (char) i;
		st[i++] = " ";
		 codeword = bis.readInt(W);
		 val = st[codeword];
		 boolean reset = false;
		//int round = 0;
		while (true) {
			//round ++;
			//System.out.println("expand round:"+round);
			bos.write(val);
			codeword = bis.readInt(W);
			if (codeword == R)
				break;
			String s = st[codeword];
			if (i == codeword)
				s = val + val.charAt(0);
			if(reset)
				reset = false;
			else if (i < L)
				st[i++] = val + s.charAt(0);
			else if(i == L){
				st = new String[L];
				for (i = 0; i < R; i++)
					st[i] = "" + (char) i;
				st[i++] = " ";
				reset = true;
			}
			val = s;
		}
		bos.close();
		bis.close();
	}
}
