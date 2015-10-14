package cn.larry.sample.compress;

import java.util.LinkedList;
import java.util.Queue;

public class Trie<E> {
	
	private static int R = 256;
	
	private Node root;
	
	private static class Node{
		private Object val;
		private Node[] next = new Node[R];
	}
	
	public E get(String key){
		Node x = get(root, key, 0);
		if(x == null) return null;
		return (E)x.val;
	}
	
	private Node get(Node x,String key,int d){
		if(x == null) return null;
		if(d == key.length()) return x;
		char c = key.charAt(d);
		c = (char) (c & 0x00ff);
		return get(x.next[c], key, d+1);
	}
	
	public void put(String key,E val){
		root = put(root,key,0,val);
	}
	
	private Node put(Node x, String key,int d,E val){
		if(x == null) x = new Node();
		if(key.length() == d) {x.val = val;return x;}
		char c = key.charAt(d);
		c = (char) (c & 0x00ff);
		x.next[c] = put(x.next[c],key,d+1,val);
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
	
	private void collect(Node x,String pre,Queue<String> que){
		if(x == null ) return ;
		if(x.val != null) que.offer(pre);
		for(char c = 0; c < R;c++)
			collect(x.next[c],pre+c,que);
	}
	
	public Queue<String> keys(){
		return keysWithPrefix("");
	}
	
	public Queue<String> keysWithPrefix(String prefix){
		Queue<String> q = new LinkedList<>();
		collect(get(root, prefix, 0),prefix,q);
		return q;
	}
	
	public Queue<String> keysThatMatch(String pat){
		Queue<String> q = new LinkedList<>();
		collect(root,"",pat,q);
		return q;
	}

	private void collect(Node x,String pre,String pat,Queue<String> q){
		int d = pre.length();
		if(x == null) return;
		if(d == pat.length() && x.val != null) q.offer(pre);
		if(d == pat.length()) return;
		
		char ch = pat.charAt(d);
		for(char c = 0; c < R; c ++ )
			if(ch == '.' || ch == c )
				collect(x.next[c],pre+c,pat,q);
	}
	
	public String longestPrefixOf(String s){
		int len = search(root, s, 0,0);
		return s.substring(0,len);
	}
	
	private int search(Node x,String s,int d,int length){
		if(x == null) return length;
		if(x.val != null ) length = d;
		if(d == s.length()) return length;
		char c = s.charAt(d);
		c = (char) (c & 0x00ff);
		return search(x.next[c],s,d+1,length);
	}
	
	public void  delete(String key){
		root = delete(root,key,0);
	}
	
	private Node delete(Node x,String key,int d){
		if(x == null)return null;
		if(d == key.length()) return x;
		else
		{
			char c = key.charAt(d);
			x.next[c] = delete(x.next[c],key,d+1);
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
 