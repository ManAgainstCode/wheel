package cn.larry.sample.compress;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BinaryInputStream {
	
	private FileInputStream in;
	private short index;
	private byte bt;
	
	public BinaryInputStream(FileInputStream in){
		this.in = in;
		index = 0;
	}
	
	public boolean readBoolean() throws IOException{
		  if(index == 0){
			  int b = in.read();
			  if(b == -1)
				  throw new EOFException();
			  bt = (byte)b; 
		  }
		  boolean result = (bt>>(7-index)&0x01) != 0;
		  index ++;
		  if(index > 7)
			  index = 0;
		  return result;
	}
	
	public static int byteArrayToInt(byte[] b, int offset) throws IOException {
		DataInputStream dintput = new DataInputStream(new ByteArrayInputStream(b));
		return dintput.readInt();
	 }
	
	public int readInt() throws IOException{
		byte[] bts = new byte[4];
		for(int i = 0;i<4;i++)
			bts[i] = readByte();
		return byteArrayToInt(bts,0);
	}
	
	public byte readByte() throws IOException{
		byte b = 0x00;
		for(int i=0;i<8;i++){
			boolean bl = readBoolean();
			if(bl)
				b = (byte) (b | 0x01<<(7-i));
		}
		return b;
	}
	
	public byte[] readBytes() throws IOException  {
			List<Byte> bytes = new ArrayList<Byte>();
			int b ; 
			while((b =   in.read()) != -1)
				bytes.add((byte)b);
			in.close();
			byte[] bts = new byte[bytes.size()];
			for(int i=0;i<bytes.size();i++)
				bts[i] = bytes.get(i);
			return bts;
	}
	
	public void close() throws IOException{
		in.close();
		index = 0;
	}

	public int readInt(int w) throws IOException {
		int n = 0;
		for(int i = 0 ; i < w; i ++)
			if(readBoolean())
				n += 1<<(w-i-1);
		return n;
	}

}
