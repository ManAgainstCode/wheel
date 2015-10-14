package cn.larry.sample;

import java.util.Random;
/**
 * 
 * @author gzfu
 */
public class RandomString{
	public static final String BASE_CHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_-";
	
public static String get(int length) { //length��ʾ�����ַ����ĳ���
    String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";   
    Random random = new Random();   
    StringBuffer sb = new StringBuffer();   
    for (int i = 0; i < length; i++) 
        sb.append(base.charAt(random.nextInt(base.length())));   
    
    return sb.toString();   
 }   
}