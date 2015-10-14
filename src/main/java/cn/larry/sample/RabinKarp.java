package cn.larry.sample;
/**
 *  RabinKarp字符串查找算法，采用hash散列方式查找字符串
 * @author larryfu
 *
 */
public class RabinKarp {

	private String pat;   //模式字符串
	private long patHash;  //模式字符串的散列值
	private int M;      //模式字符串的长度
	private long Q;     // 一个很大的素数
	private int R = 256; //字母表大小
	private long RM;     //R^(M-1)%Q
	
	public RabinKarp(String pat){
		this.pat = pat;
		this.M = pat.length();
		Q = longRandomPrime();
		RM = 1;
		for(int i = 1;i<=M-1;i++) //计算R^(M-1)&%Q
			RM = (R*RM)%Q;
		patHash = hash(pat,M);
	}

	private long longRandomPrime(){
		return 99997l;
	}

	private long hash(String key,int M){
		long h = 0;
		for(int j = 0 ;j<M;j++)
			h = (R*h+key.charAt(j))%Q;
		return h;
	}
	
	
	private boolean check(int n){
		return true;
	}
	
	
	public int search(String txt){
		System.out.println(patHash);
		int N = txt.length();
        long txtHash = hash(txt,M);
        if(txtHash == patHash && check(0))return 0;
        
        for(int i = M ; i<N;i++){
        	System.out.println(txtHash);
        	//减去第一个数字，加上最后一个数字，然后再检查匹配
        	txtHash = (txtHash +Q -RM*txt.charAt(i-M)%Q)%Q;
        	txtHash = (txtHash*R + txt.charAt(i))%Q;
        	if(patHash == txtHash)
        		if(check(i-M+1))return i-M+1;
        }
        return -1;
	}
	
	public static void main(String[] args){
		String pat = "is";
		String txt = "this is  a island";
		RabinKarp bm = new RabinKarp(pat);
		System.out.print(bm.search(txt));
	}
}
