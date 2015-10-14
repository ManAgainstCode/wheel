package cn.larry.sample.compress;

import java.io.DataOutputStream;
import java.io.IOException;

public class BinOutputStream {
	private DataOutputStream out;
	
	private byte bt;
	
	private byte[] bs = new byte[4*1024];
	
	private int n = 0;
	
	private short index;
	
	public BinOutputStream(DataOutputStream out){
		this.out = out;
		index = 0;
		bt = 0x00;
	}
	
	public void flush() throws IOException{
		out.write(this.bt);
		index = 0;
		bt = 0x00;
	}
	
	private void flushData() throws IOException{
		out.write(bs, 0, n);
	}
	
	private void writeByte(byte b) throws IOException{
		if(n == 4*1024){
			out.write(bs, 0, 4*1024);
			n = 0;
		}
		bs[n++] = b;
	}
	
	public void writeString(String val) throws IOException{
		for(int i = 0 ;i<val.length();i++)
			writeByte((byte)val.charAt(i));
	}
	
	public void writeOneHalfByte(int d) throws IOException{
		if(index % 2 == 0){
			int n = d>>4;
			byte b = (byte) (n&0xff);
			writeByte(b);
			bt = (byte) (d&0x0f);
			bt = (byte) (bt << 4);
		}
		else{
			int n = d >>8;
			n = n & 0x0f;
			byte b = (byte) (bt|n);
			writeByte(b);
			b = (byte) (d & 0xff);
			writeByte(b);
		}
		index ++;
	}
	
	public void close() throws IOException{
		flushData();
		out.close();
	}
	public static void main(String[] args) {
		int i = 16;
		i = i>>5;
		System.out.println(i);
	}
}
