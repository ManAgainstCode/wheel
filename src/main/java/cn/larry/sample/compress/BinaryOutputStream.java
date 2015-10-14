package cn.larry.sample.compress;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 二进制输出流，使用了缓存，以4K为单位写数据，提高性能
 * @author larryfu
 *
 */

public class BinaryOutputStream {

	private DataOutputStream out;
	
	private byte bt;
	
	private short index;
	
	public BinaryOutputStream(DataOutputStream out){
		this.out = out;
		index = 0;
		bt = 0x00;
	}
	
	private void flush() throws IOException{
		out.write(this.bt);
		index = 0;
		bt = 0x00;
	}
	
	public void writeByte(byte b) throws IOException{
		for(byte i = 0;i<8;i++){
			boolean bl = (b>>(7-i)&0x01) != 0;
			writeBoolean(bl);
		}
	}
	
	public void writeChar(char c) throws IOException{
		for(byte i = 0;i<15;i++){
			boolean bl = (c>>(15-i)&0x01) != 0;
			writeBoolean(bl);
		}
	}
	
	public void writeBoolean(boolean bl) throws IOException{
		if(bl)
			bt = (byte) (bt | 0x01<<(7-index));
		index ++;
		if(index > 7)
			flush();	
	}
	
	private byte[] intToByteArray(final int integer) throws IOException {
		ByteArrayOutputStream boutput = new ByteArrayOutputStream();
		DataOutputStream doutput = new DataOutputStream(boutput);
		doutput.writeInt(integer);
		return boutput.toByteArray();
	}
	
	public void write(int d) throws IOException{
		byte[] bts = intToByteArray(d);
		for(int i = 0;i<4;i++)
			writeByte(bts[i]);
	}
	
	public void close() throws IOException{
		if(index != 0)
			flush();
		out.flush();
		out.close();
	}

	public  void write(int n, int w) throws IOException {
		for(int i =w-1;i>=0;i--){
			boolean bl = (n & 1<<i) == 0?false:true;
			writeBoolean(bl);
		}
	}
	
	public void writeAsByte(char c) throws IOException{
		writeByte((byte)c);
	}

	public void write(String val) throws IOException {
		for(int i = 0 ;i<val.length();i++)
			writeAsByte(val.charAt(i));
	}
	
	static void printByte(byte b){
		for(int i = 7 ;i >= 0;i--)
			System.out.print((b&1<<i) == 0?"0":"1");
	}
	
	
}
