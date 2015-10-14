package cn.larry.analyzer;

import cn.larry.util.MD5Util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


class NewsFeature {

	private Set<String> links = new HashSet<String>();

	private Map<String, Double> wordFreq;

	private Double length = 0.0;

	private String hash;

	public Set<String> getlinks() {
		return links;
	}

	public String getHash() {
		return hash;
	}
	
	public static NewsFeature combine(final NewsFeature nf1,final NewsFeature nf2){
		double ratio1 = (double) nf1.links.size() / (nf1.links.size() + nf2.links.size());
		double ratio2 = (double) nf2.links.size() / (nf1.links.size() + nf2.links.size());
		Set<String> keySet = new HashSet<>();
		keySet.addAll(nf1.wordFreq.keySet());
		keySet.addAll(nf2.wordFreq.keySet());
		Map<String,Double> wordFq = new HashMap<>();
		for (String str : keySet) {
			Double freq1 = nf1.wordFreq.get(str);
			if(freq1 == null)
				freq1 = 0.0;
			Double freq2 =  nf2.wordFreq.get(str);
			if(freq2 == null)
				freq2 = 0.0;
			double freq = freq1 * ratio1 + freq2 * ratio2;
			wordFq.put(str, freq);
		}
		NewsFeature nf = new NewsFeature(wordFq);
		nf.links.addAll(nf1.links);
		nf.links.addAll(nf2.links);
		nf.hash = MD5Util.MD5(nf1.hash + nf2.hash);
		return nf;
	}

	public void combine(NewsFeature nf) {
		this.links.addAll(nf.links);
		hash = MD5Util.MD5(this.hash + nf.hash);
		double ratio1 = (double) this.links.size() / (nf.links.size() + this.links.size());
		double ratio2 = (double) nf.links.size() / (nf.links.size() + this.links.size());
		Set<String> keySet = new HashSet<>();
		keySet.addAll(this.wordFreq.keySet());
		keySet.addAll(nf.wordFreq.keySet());
		for (String str : keySet) {
			Double freq1 = this.wordFreq.get(str);
			if(freq1 == null)
				freq1 = 0.0;
			Double freq2 =  nf.wordFreq.get(str);
			if(freq2 == null)
				freq2 = 0.0;
			double freq = freq1 * ratio1 + freq2 * ratio2;
			wordFreq.put(str, freq);
		}
		caculateLength();
	}
	
	public NewsFeature( Map<String, Double> wordfreq){
		this.wordFreq = wordfreq;
	}

	public NewsFeature(String link, Map<String, Double> wordfreq) {
		this.wordFreq = wordfreq;
		links.add(link);
		hash = MD5Util.MD5(link);
	}

	public Double getLength() {
		if (length == 0)
			caculateLength();
		return length;
	}

	private void caculateLength() {
		double lengthSquare = 0;
		for (String str : wordFreq.keySet()) {
			double freq = wordFreq.get(str);
			lengthSquare += freq * freq;
		}
		length = Math.sqrt(lengthSquare);
	}

	<E> Set<E> intersection(Set<E> set1, Set<E> set2) {
		Set<E> intersection = new HashSet<E>();
		for (E e : set1)
			if (set2.contains(e))
				intersection.add(e);
		return intersection;
	}

	Double cos(NewsFeature another) {
		return this.innerAnd(another) / (this.getLength() * another.getLength());
	}

	public double innerAnd(NewsFeature another) {
		double result = 0;
		Set<String> intersection = intersection(this.wordFreq.keySet(), another.wordFreq.keySet());
		for (String str : intersection)
			result += this.wordFreq.get(str) * another.wordFreq.get(str);
		return result;
	}

	public void caculateWeight(IDFTable table) {
		double threshold = 0.001;
		Set<String> words = wordFreq.keySet();
		for (String str : words) {
			double idffreq = wordFreq.get(str) * table.getWordIDF(str);
			if (idffreq < threshold)
				wordFreq.remove(str);
			else
				wordFreq.put(str, idffreq);
		}
	}

}
