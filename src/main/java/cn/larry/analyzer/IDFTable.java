package cn.larry.analyzer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class IDFTable {
	
	private static final double LN2 = Math.log(2);  
	
	public static void main(String[] args){
		//System.out.println(Math.);
	}

	private Map<String,Double> wordIDFTable = new HashMap<String,Double>();

	private int newsNumber = 0;
	
	public Set<String> getWordSet(){
		return wordIDFTable.keySet();
	}
	
	public void processAticle(String[] words){
		newsNumber ++;
		Set<String> wordset = new HashSet<String>(Arrays.asList(words));
		for(String word :wordset){
			if(wordIDFTable.get(word) == null)
				wordIDFTable.put(word, 0.0);
			double num = wordIDFTable.get(word);
			num += 1.0;
			wordIDFTable.put(word, num);
		}
	}
	
	public Map<String,Double> getWordIdf(){
		return wordIDFTable;
	}
	
	public void caculateIDF(){
		for(String word:getWordSet())
			wordIDFTable.put(word, log2(newsNumber/wordIDFTable.get(word)));
		saveIDF();
	}
	
	private double log2(double num){
		return Math.log(num)/LN2;
	}
	
	Double getWordIDF(String str){
		return wordIDFTable.get(str);
	}
	
	void saveIDF(){
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("D:\\newsinfo\\idf.object"))){
			oos.writeObject(wordIDFTable);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
