//package cn.larry.analyzer;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import javax.annotation.Resource;
//
//import org.hibernate.Criteria;
//import org.hibernate.criterion.Projections;
//import org.springframework.stereotype.Service;
//
//import cn.larry.crawler.entity.News;
//import cn.larry.mapline.dao.HibernateDaoUtil;
//
//@Service
//public class NewsAnalyzer {
//
//	@Resource
//	HibernateDaoUtil hibernateDaoUtil;
//
//	@Resource
//	IDFTable wordIDFTable;
//
//	@Resource
//	FeatureMatrix featureMatrix;
//
//	long getNewsNumber(){
//		return hibernateDaoUtil.Excute((ss)->{
//			Criteria crti = ss.createCriteria(News.class);
//			return (long) crti.setProjection(Projections.rowCount()).uniqueResult();
//		});
//	}
//
//	List<News> getNewsByRange(int start,int num){
//		return hibernateDaoUtil.Excute((ss)->{
//			Criteria crti = ss.createCriteria(News.class);
//			crti.setFirstResult(start);
//			crti.setMaxResults(num);
//			return (List<News>)crti.list();
//		});
//	}
//
//	List<News> getNews(){
//		return  hibernateDaoUtil.Excute((ss)->{
//			 return (List<News>)ss.createQuery("from "+News.class.getName()).list();
//		});
//	}
//
//	public void analyzeMilitaryNews(){
//		int size = 1000;
//		for(int i = 0;i<25;i++){
//			List<News> newsList = getNewsByRange(i*size, size);
//			for(News news : newsList){
//				String[] words = IKSpliter.splitNews(news.getTitle()+news.getContent());
//				wordIDFTable.processAticle(words);
//				featureMatrix.processAticle(news.getLink(), words);
//			}
//		}
//		//System.out.println("caculate idf");
//		wordIDFTable.caculateIDF();
//		featureMatrix.caculateWeigth(wordIDFTable);
//		Map<String,Integer> militaryNewsMap = MilitaryFeature.getMilitaryFeature();
//		Map<String,Double> militaryfeature = caculateMilitary(militaryNewsMap,wordIDFTable.getWordIdf());
//		Set<NewsFeature> militaries = featureMatrix.findMilitary(militaryfeature, 0.08);
//		for(NewsFeature nf :militaries){
//			Set<String> links = nf.getlinks();
//			for(String link:links)
//				System.out.println(link);
//		}
//	}
//
//	public Map<String,Double> caculateMilitary(Map<String,Integer> military,Map<String,Double> idf){
//		Map<String,Double> militaryfeature = new HashMap<>();
//		for(String str:military.keySet()){
//			Double idfval = idf.get(str);
//			if(idfval == null)
//				idfval = 1.0;
//			militaryfeature.put(str, idfval*military.get(str));
//		}
//		return militaryfeature;
//	}
//
//	 public void autoCluster() {
//		 long number = getNewsNumber();
//		 number = 5000;
//		 int size = 1000;
//			for(int i = 0;i*size<number;i++){
//
//				List<News> newsList = getNewsByRange(i*size, size);
//				//List<News> newsList = getNews();
//				System.out.println("analyze news round:::::::;"+i);
//				for(News news : newsList){
//					String[] words = IKSpliter.splitNews(news.getTitle()+news.getContent());
//					wordIDFTable.processAticle(words);
//					featureMatrix.processAticle(news.getLink(), words);
//				}
//			}
//			System.out.println("caculate idf");
//			wordIDFTable.caculateIDF();
//			featureMatrix.caculateWeigth(wordIDFTable);
//			AsymptoticFitting af = new AsymptoticFitting(featureMatrix,0.98);
//			af.cluster();
//			writeResult(af);
//			printCategories(af.getCategories());
//	}
//
//	 private void writeResult(AsymptoticFitting af) {
//		File file = new File("d:\\autocluster.txt");
//		try(PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)))){
//			for(Category c:af.getCategories()){
//				pw.println("a cluster******************");
//				for(NewsFeature nf : c.getNews())
//					for(String link :nf.getlinks())
//						pw.println(link);
//				pw.println("");
//			}
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		};
//
//	}
//
//	 private void printCategories(Category[] cs){
//		 for(Category c:cs)
//			 c.printContent();
//	 }
//
//	//新闻聚类 将最接近的新闻聚在一起
//	public void analyzeNews(int round,int size){
//		long number = getNewsNumber();
//		for(int i = 0;i<round;i++){
//
//			List<News> newsList = getNewsByRange(i*size, size);
//			//List<News> newsList = getNews();
//			//System.out.println("analyze news round:::::::;"+i);
//			for(News news : newsList){
//				String[] words = IKSpliter.splitNews(news.getTitle()+news.getContent());
//				wordIDFTable.processAticle(words);
//				featureMatrix.processAticle(news.getLink(), words);
//			}
//		}
//		//System.out.println("caculate idf");
//		wordIDFTable.caculateIDF();
//		featureMatrix.caculateWeigth(wordIDFTable);
//
//		System.out.println("cluster");
////		featureMatrix = featureMatrix.cluster(0.1);
//
//
////		featureMatrix.printMatrix();
//
//	}
//
//}
