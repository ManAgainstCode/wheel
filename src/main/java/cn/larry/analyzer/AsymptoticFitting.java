package cn.larry.analyzer;

import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AsymptoticFitting {

	private FeatureMatrix featureMatrix;
	
	private Category[] categories = new Category[10];
	
	private double theshold;
	
	public AsymptoticFitting(FeatureMatrix matrix,double theshold ) {
		this.featureMatrix = matrix;
		this.setTheshold(theshold);
	}
	
	public Category[] getCategories(){
		return categories;
	}
	
	public void cluster(){
		
		System.out.println("init category");
		
		initCategory();
		while(true){
			
			System.out.println("once cluster");
			
			onceCluster();
			boolean enough = true;
			for(Category c:categories){
				double dev = c.caculateCenter();
				 if(dev < theshold)
						enough = false ;
				System.out.println("deviation:" + dev);	
			}
			if(enough)
				break;
			for(Category c:categories)
				c.clear();
		}
	}
	
	 void onceCluster(){
		for(String str:featureMatrix.keySet()){
			final NewsFeature nf = featureMatrix.get(str);
			int i = 0;
			double sim = 0.0;
			for(int j = 0; j < categories.length; j++){
				double si = categories[j].getCenter().cos(nf);
				if(si>sim){
					i = j;
					sim = si;
				}
			}
			categories[i].addNews(nf);
		}
	}
	
	void initCategory(){
		List<NewsFeature> startPon = generateStartPoints();
		for(int i = 0;i<startPon.size();i++) 
			categories[i] = new Category(startPon.get(i));
	}
	
	private List<NewsFeature> generateStartPoints(){
		List<NewsFeature> nfs = new ArrayList<>();
		Set<Integer> indexes = new HashSet<>();
		for(int i = 0;i<10;i++){
			int index = RandomUtils.nextInt(0, featureMatrix.keySet().size());
			indexes.add(index);
		}
		List<String> keys = new ArrayList<>(featureMatrix.keySet()) ;
		for(int i = 0;i<keys.size();i++)
			if(indexes.contains(i))
				nfs.add(featureMatrix.get(keys.get(i)));
		return nfs;
	}

	public double getTheshold() {
		return theshold;
	}

	public void setTheshold(double theshold) {
		this.theshold = theshold;
	}
}

class Category{
	private  NewsFeature center;
	private Set<NewsFeature> news = new HashSet<>();
	public Category( NewsFeature center) {
		this.center =center;
	}
	
	 Set<NewsFeature> getNews(){
		return news;
	}
	
	void addNews(NewsFeature nf){
		news.add(nf);
	}
	
	void clear(){
		news.clear();
	}
	
	double caculateCenter(){
		NewsFeature start = null;
		if(news.isEmpty())
			return 0.0;	
		for(NewsFeature nf:news){
			if(start == null)
				start = nf;
			else
				start = NewsFeature.combine(start, nf);
		}
		double deviation = center.cos(start);
		center = start;
		return deviation;
	}

	 NewsFeature getCenter() {
		return center;
	}
	
	 void printContent(){
		System.out.println("a category *******************************************");
		for(NewsFeature nf : news)
			for(String link :nf.getlinks())
				System.out.println(link);
		System.out.println("");
	}

	
}
