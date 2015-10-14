package cn.larry.sample.compress;


import java.io.FileInputStream;
import java.io.IOException;
/**
 * 二进制输入流，使用了缓存，以6K为单位读入数据，提高性能
 * @author larryfu
 *
 */
public class BinInputStream {
	private FileInputStream in;
	private short index = 6*1024;
	private byte[] bt = new byte[6*1024];
	private int pos ;
	private byte b;
	private boolean more = true; 
	
	public BinInputStream(FileInputStream in){
		this.in = in;
		pos = 0;
	}
	
	private byte readByte() throws IOException{
		if(index == 6*1024){
			int n = in.read(bt, 0, 6*1024);
			index = 0;
			if(n<6*1024)
				more = false;
		}
		byte b =  bt[index];
		index ++;
		return b;
	}
	
	public int readInt() throws IOException{
		if(pos % 2 == 0){
			byte b1 = readByte();
			byte b2 = readByte();
			int val = (Byte.toUnsignedInt(b1)<<4)+ (Byte.toUnsignedInt(b2)>>4);
			b = (byte) (b2&0x0f);
			pos ++;
			return val;
		}else{
			byte b1 = readByte();
			pos ++;
			return (Byte.toUnsignedInt(b)<<8) + (Byte.toUnsignedInt(b1)); 
		}
	}
	
	public void close() throws IOException{
		in.close();
	}
	
	public static void main(String[] args) {
		byte b = 30;
		int i = Byte.toUnsignedInt(b)<<8;
		System.out.println(1<<12);
	}
}
