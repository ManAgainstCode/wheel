package cn.larry.sample.compress;

public class ByteSequence {
	 
	 private byte[] b;
	 private int index;
	
	 public ByteSequence(byte[] b){
		 this.b = b;
		 index = 0;
	 }
	 
	 public byte byteAt(int n){
		 return b[index+n];
	 }
	 
	 public byte[] subByte(int start,int end){
		 start = index+start;
		 end = index + end;
		 byte[] bt = new byte[end-start];
		 System.arraycopy(b, start, bt, 0, end-start);
		 return bt;
	 }
	 
	 public  ByteSequence subByteSeq(int start,int end){
		 start = index+start;
		 end = index + end;
		 byte[] bt = new byte[end-start];
		 System.arraycopy(b, start, bt, 0, end-start);
		 return new ByteSequence(bt);
	 }
	 
	 
	 public void forward(int i){
		 index += i;
	 }
	 
	 public int length(){
		 return b.length - index;
	 }
}
