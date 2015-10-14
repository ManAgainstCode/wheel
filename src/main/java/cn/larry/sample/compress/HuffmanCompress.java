package cn.larry.sample.compress;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.PriorityQueue;

/**
 * 哈夫曼压缩算法的java实现，基于datainputstream，dataoutputstream自行编写了二进制输入输出流
 * @author larryfu
 *
 */
public class HuffmanCompress {
	
	private  BinaryInputStream bin;
	private BinaryOutputStream bos;
	
	private static final int BYTE_AMOUT = 256;
	
	public HuffmanCompress(BinaryInputStream bin,BinaryOutputStream bos){
		this.bin = bin;
		this.bos = bos;
	}
	
	public static void main(String[] args){
		try {
			long start = System.currentTimeMillis();
			String origin = "d:/test1.txt";
			String newfile = "d:/test2.com";
		    String defile = "d:/test3.txt";
		    File f1 = new File(newfile);
			File f2  = new File(defile);
			if(f1.exists())
				f1.delete();
			if(f2.exists())
				f2.delete();
			HuffmanCompress com = new HuffmanCompress(new BinaryInputStream(new FileInputStream(origin)), 
					new BinaryOutputStream(new DataOutputStream(new FileOutputStream(newfile))));
			com.compress();
			long compressed = System.currentTimeMillis();
			HuffmanCompress dcom = new HuffmanCompress(new BinaryInputStream(new FileInputStream(newfile)), 
					new BinaryOutputStream(new DataOutputStream(new FileOutputStream(defile))));
			dcom.expand();
			long end= System.currentTimeMillis();
			long compressTime = compressed - start;
			long expandTime = end - compressed;
			System.out.println("compress time:"+compressTime);
			System.out.println("expand time:"+expandTime);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	int byte2Int(byte b){
		int i = b;
		if(i < 0 )
			i = BYTE_AMOUT + i;
		return i;
	}
	
	public void compress(){
		try {
			byte[] bytes = bin.readBytes();
			int freq[] = new int[BYTE_AMOUT];
			for(int i=0; i< bytes.length; i++)
				freq[byte2Int(bytes[i])]++;
			Node root =  buildTrie(freq);
			String[] st = new String[BYTE_AMOUT];
			buildCode(st, root, "");
			
			///
			//System.out.println("compress build table--------------------------");
			//for(int i = 0;i<st.length;i++)
			//    System.out.println("byte :"+i+",code:"+st[i]);
			///
			
			writeTrie(root);
			bos.write(bytes.length);
			for(int i = 0;i<bytes.length;i++){
				String code = st[byte2Int(bytes[i])];
				for(int j = 0;j<code.length();j++)
					if(code.charAt(j) == '1')
						bos.writeBoolean(true);
					else bos.writeBoolean(false);
			}
			bos.close();
			bin.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
private Node buildTrie(int[] freq) {
		PriorityQueue<Node> pq = new PriorityQueue<Node>((n1,n2)->{
			return n1.freq - n2.freq;
		});
		for(int b = 0;b<BYTE_AMOUT;b++)
			if(freq[b]>0)
				pq.offer(new Node((byte)b,freq[b],null,null));
		while(pq.size()>1){
			Node x = pq.poll();
			Node y = pq.poll();
			Node parent = new Node((byte)0,x.freq+y.freq,x,y);
			pq.offer(parent);
		}
		return pq.poll();
	}

public void expand() throws IOException{
	Node root  = readTrie();
	int N = bin.readInt();
	for(int i = 0;i<N;i++){
		Node x = root;
		while(!x.isLeaf())
			if(bin.readBoolean())
				x = x.right;
			else 
				x = x.left;
		bos.writeByte(x.bt);
	}
	bos.close();
	bin.close();
}

String[] buildCode(Node root){
	String[] st = new String[BYTE_AMOUT];
	buildCode(st,root,"");
	return st;
}

void buildCode(String[] st,Node x,String s){
	if(x.isLeaf()){
		st[byte2Int(x.bt)] = s;
		return;
	}
	buildCode(st, x.left, s+'0');
	buildCode(st, x.right, s+'1');
}

/**
 * 以递归的方式将哈夫曼编码树写入输出流中
 * @param x
 * @throws IOException
 */
private void writeTrie(Node x) throws IOException{
	if(x.isLeaf()){
		bos.writeBoolean(true);
		bos.writeByte(x.bt);
		return;
	}
	bos.writeBoolean(false);
	writeTrie(x.left);
	writeTrie(x.right);
}


/**
 * 递归的方式从输入流中读取哈夫曼树，此处的递归用得非常精髓
 * @return
 * @throws IOException
 */
private Node readTrie() throws IOException{
		if (bin.readBoolean())
			return new Node(bin.readByte(), 0, null, null);
		return new Node((byte) 0, 0, readTrie(), readTrie());
	}
}

class Node implements Comparable<Node>{

	 byte bt;
	
	 int freq;
	
	 final Node left,right;
	
	public byte getValue(){
		return bt;
	}
	
	Node(byte bt,int freq,Node left,Node right){
		this.bt = bt;
		this.freq = freq;
		this.left = left;
		this.right = right;
	}
	
	public boolean isLeaf(){
		return left == null && right == null;
	}
	
	@Override
	public int compareTo(Node that) {
		return this.freq - that.freq;
	}
	
}
