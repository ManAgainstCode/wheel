package cn.larry.sample;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import cn.larry.sample.compress.BinaryInputStream;
import cn.larry.sample.compress.BinaryOutputStream;


/**
 * Hello world!
 *
 */

public class App 
{
	
	public static void main( String[] args ){
		URI uri;
		try {
			uri = App.class.getResource("/").toURI();
			//p = p.substring(1);
		    Path path= Paths.get(uri);
		    path = path.getParent().getParent();
		    //path.resolve("..");
			System.out.println(path.toAbsolutePath().toString());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	static List<String> getList(){
		System.out.println("get list ++++++++++");
		List<String> strs = new ArrayList<>();
		for(int i = 0;i<10;i++){
			strs.add(RandomString.get(6));
		}
		return strs;
	}
	
    public static void mains( String[] args )
    {
//        int a = 255;
//        byte b = (byte)a;
//        a = (int)b;
//        System.out.println(a);
    	
//    	int a = 12321321;
//    	System.out.println(a);
//    	try {
//			byte[] bts = intToByteArray(a);
//			int result = byteArrayToInt(bts,0);
//			System.out.println(result);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	
    	
//    	int a = 123213;
//    	printInt(a);
//    	System.out.println("");
//    	byte byt = (byte)a;
//    	printByte(byt);
//    	System.out.println("");
//    	byte b = -70;
//    	printByte(b);
//    	String str = "10111010";
//    	byte bt = 0x00;
//    	for(int i=0;i<str.length();i++)
//    		if(str.charAt(i) == '1')
//    			bt = (byte) (bt | (0x01<<(7-i)));
//    		
//    	System.out.println(bt);
//    	printByte(bt);
    	
    //	int a = Character.MAX_VALUE;
    //	a= Character.MIN_VALUE;
    	
    	long l = Integer.MAX_VALUE;
    	l = l*2;
    	int a = (int)l;
    	System.out.println(l+","+a);
    	//byte b  = -10;
    	//char c = 'k';
    	//int in = c;
    	//c = (char) in;
    	//System.out.println(c);
    	
    	try {
    		BinaryOutputStream bos = new BinaryOutputStream(new DataOutputStream(new FileOutputStream("d:/text.text")));
			byte[] bts = {60,70};
			//bos.writeBoolean(true);
			//bos.writeBoolean(false);
			//System.out.print("10");
			
			for(byte bt :bts){
				printByte(bt);
				bos.writeByte(bt);
			}
			bos.close();
			System.out.println("---------------");
			
    		BinaryInputStream bis = new BinaryInputStream(new FileInputStream("d:/text.text"));
    		while(true){
    			boolean bl = bis.readBoolean();
    			System.out.print(bl?"1":"0");
    		}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    static void printInt(int in){
    	for(int i=0;i<32;i++){
    		int bl = (in>>(31-i))&0x01;
    		System.out.print(bl == 0?"0":"1");
    	}
    }
    
    static void printByte(byte b){
    	for(int i=0;i<8;i++){
    		int bl = (b>>(7-i))&0x01;
    		System.out.print(bl == 0?"0":"1");
    	}
    }
    
	private static byte[] intToByteArray(final int integer) throws IOException {
		ByteArrayOutputStream boutput = new ByteArrayOutputStream();
		DataOutputStream doutput = new DataOutputStream(boutput);
		doutput.writeInt(integer);
		byte[] buf = boutput.toByteArray();
		return buf;
		}
	public static int byteArrayToInt(byte[] b, int offset) throws IOException {
		ByteArrayInputStream bintput = new ByteArrayInputStream(b);
		DataInputStream dintput = new DataInputStream(bintput);
		int i = dintput.readInt();
		return i;
	 }
    

    String home() {
      return "hello";
    }
}
