package cn.larry.sample.compress;


public class ByteTrie<E> {
	
	private static int R = 256;
	
	private Node root;  
	
	private static class Node{
		private Object val;
		private Node[] next = new Node[R];
	}
	
	public E get(byte[]  key){
		Node x = get(root, key, 0);
		if(x == null) return null;
		return (E)x.val;
	}
	
	private Node get(Node x,byte[] key,int d){
		if(x == null) return null;
		if(d == key.length) return x;
		byte c = key[d];
		int i = Byte.toUnsignedInt(c);
		return get(x.next[i], key, d+1);
	}
	
	public void put(byte[] key,E val){
		root = put(root,key,0,val);
	}
	
	private Node put(Node x, byte[] key,int d,E val){
		if(x == null) x = new Node();
		if(key.length == d) {x.val = val;return x;}
		byte c = key[d];
		int i = Byte.toUnsignedInt(c);
		x.next[i] = put(x.next[i],key,d+1,val);
		return x;
	}
	
	public int size(){
		return size(root);
	}
	
	private int size(Node x){
		if(x == null) return 0;
		
		int cnt = 0;
		if(x.val != null) cnt++;
		for(char c =0 ; c<R ;c++)
			cnt += size(x.next[c]);
		
		return cnt;
	}
	
	public byte[] longestPrefixOf(byte[] s){
		int len = search(root, s, 0,0);
		byte[] r = new byte[len];
		 System.arraycopy(s, 0, r, 0, len);
		 return r;
	}
	
	private int search(Node x,byte[] s,int d,int length){
		if(x == null) return length;
		if(x.val != null ) length = d;
		if(d == s.length) return length;
		byte c = s[d];
		int i = Byte.toUnsignedInt(c);
		return search(x.next[i],s,d+1,length);
	}
	
	public void  delete(byte[] key){
		root = delete(root,key,0);
	}
	
	private Node delete(Node x,byte[] key,int d){
		if(x == null)return null;
		if(d == key.length) return x;
		else
		{
			byte c = key[d];
			int i = Byte.toUnsignedInt(c);
			x.next[i] = delete(x.next[i],key,d+1);
		}
		if(x.val != null) return x;
		
		for(char c = 0; c< R;c++)
			if(x.next[c] != null) return x.next[c];
		
		return null;
	}
	
	public static void main(String[] args) {
		Trie<Object> trie = new Trie<>();
		trie.put("she", new Object());
		trie.put("shell", new Object());
		trie.put("shore",new Object());
		trie.put("sea", new Object());
		System.out.println(trie.longestPrefixOf("searshells"));
		for(String s:trie.keysThatMatch("s.."))
			System.out.println(s);
		for(String s:trie.keysWithPrefix("s"))
			System.out.println(s);
		System.out.println("sys".substring(0,1));
		//		
//		char c = '0';
//		int[] n = new int[100];
//		n[c] = 100;
//		
//		System.out.println(n[c]);
//		
		
	}
	
	
}
 