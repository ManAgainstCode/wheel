package cn.larry.analyzer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.springframework.stereotype.Component;

import cn.larry.mapline.util.MD5Util;


@Component
public class FeatureMatrix {
	
	private Map<String,NewsFeature> wordsMatrix = new HashMap<String,NewsFeature>();
	
	public Set<String> keySet(){
		return wordsMatrix.keySet();
	}
	
	public NewsFeature get(String key){
		return wordsMatrix.get(key);
	}
	
	public FeatureMatrix(){
	}
	
	public FeatureMatrix(Map<String,NewsFeature> wordsMatrix){
		this.wordsMatrix = wordsMatrix;
	}
	
	public void processAticle(String link,String[] words){
		Map<String,Double> wordFreq = new HashMap<String,Double>();
		for(String word:words){
			if(wordFreq.get(word) == null )
				wordFreq.put(word,  0.0);
			wordFreq.put(word, wordFreq.get(word)+1);
		}
		wordsMatrix.put(MD5Util.MD5(link),new NewsFeature(link, wordFreq));
	}
	
	Double caculateNewsAngle(String key1,String key2){
		return wordsMatrix.get(key1).cos(wordsMatrix.get(key2));
	}
	
	void caculateWeigth(IDFTable table){
		for(String key:wordsMatrix.keySet())
			wordsMatrix.get(key).caculateWeight(table);
	}
	
	Set<NewsFeature> findMilitary(Map<String,Double> militaryFeature,double theshold){
		Set<NewsFeature> militaries = new HashSet<>();
		NewsFeature miliFeature = new NewsFeature("military", militaryFeature);
		for(String str:wordsMatrix.keySet()){
			NewsFeature nf = wordsMatrix.get(str);
			if(nf.cos(miliFeature) > theshold )
				militaries.add(nf);
			
		}
		return militaries;
	}
	
//	private void clusterOnce(Set<String> keys,Map<String,NewsFeature> matrix,double threshold){
//		System.out.println("cluster once:::"+threshold);
//		if(keys.isEmpty())
//			return;
//		String key = (String) keys.toArray()[0];
//		keys.remove(key);
//		NewsFeature keyfeature = wordsMatrix.get(key);
//		Map<String,Double> similar = new HashMap<String,Double>();
//		for(String str:keys){
//			if(!str.equals(key))
//				similar.put(str, wordsMatrix.get(str).cos(keyfeature));
//		}
//		for(String str:similar.keySet()){
//			if(similar.get(str)<threshold){
//				keyfeature.combine(wordsMatrix.get(str));
//				keys.remove(str);
//			}
//		}
//		matrix.put(key, keyfeature);
//		clusterOnce(keys,matrix,threshold);
//	}
//	
//	public FeatureMatrix clusting(double threshold) {
//		Set<String> keysSet =new HashSet<String>(wordsMatrix.keySet());
//		Map<String,NewsFeature> matrix = new HashMap<String,NewsFeature>();
//		clusterOnce(keysSet, matrix, threshold);
//		return new FeatureMatrix(matrix);
//	}
	
	public FeatureMatrix cluster(double threshold){
		Set<String> clusted = new HashSet<String>();
		PriorityQueue<Similarity> simpq = caculateAngle(this.wordsMatrix);
		Map<String,NewsFeature> matrix = new HashMap<String,NewsFeature>(this.wordsMatrix);
		while(!simpq.isEmpty()){
			Similarity sim = simpq.poll();
			if(sim.sim<threshold)
				break;
			if(!clusted.contains(sim.column)&&!clusted.contains(sim.row))
				clusteSim(sim, clusted, matrix, simpq);
		}
		return new FeatureMatrix(matrix);
	}
	
	public void clusteSim(Similarity sim,Set<String> clusted,Map<String,NewsFeature> matrix,PriorityQueue<Similarity> simpq){
		System.out.println("cluste sim once similarity:"+sim.sim);
		NewsFeature nf = matrix.get(sim.row);
		nf.combine(matrix.get(sim.column));
		matrix.remove(sim.row);
		matrix.remove(sim.column);
		clusted.add(sim.row);
		clusted.add(sim.column);
		for(String str:matrix.keySet()){
			double d = nf.cos(matrix.get(str));
			if(d > 0.001)
				simpq.add(new Similarity(nf.getHash(),str,d));
		}
		matrix.put(nf.getHash(), nf);
	}
	
	public PriorityQueue<Similarity> caculateAngle(Map<
			String,NewsFeature> matrix){
		PriorityQueue<Similarity> simpq = new PriorityQueue<Similarity>((sim1,sim2)->{
			return  (sim1.sim > sim2.sim)?-1:1;
		});
		Set<String> keys = new HashSet<String>(matrix.keySet());
		return caculateAngle(matrix, keys, simpq);
	}
	
	//to avoid stack over flow use while instead of recurisive
	private PriorityQueue<Similarity> caculateAngle(Map<String,NewsFeature> matrix,Set<String> keyset,PriorityQueue<Similarity> simpq){
		while(true){
			if(keyset.isEmpty())
				return simpq;
			String key = (String) keyset.toArray()[0];
			keyset.remove(key);
			NewsFeature keyfeature = matrix.get(key);
			for(String str:keyset){
				double sim = keyfeature.cos(matrix.get(str));
				if(sim > 0.5)
					simpq.add(new Similarity(key,str,sim));
			}
		}
	}
	
	public void printMatrix() {
		for(String str:wordsMatrix.keySet()){
			NewsFeature nf = wordsMatrix.get(str);
			System.out.println("a news class **************************** ");
			for(String link:nf.getlinks())
				System.out.println(link);
			System.out.println("");
		}	
	}
	
	public void autoClusting(){
		int size = wordsMatrix.size();
		
	}
}

class Similarity{
	 String row;
	 String column;
	 Double sim;
	public Similarity(String row,String column,double sim){
		this.column = column;
		this.row = row;
		this.sim = sim;
	}
}

