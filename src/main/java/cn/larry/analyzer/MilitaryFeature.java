package cn.larry.analyzer;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MilitaryFeature {

	
	public static Map<String,Integer> getMilitaryFeature(){
		try{
			Document doc = Jsoup.parse(new File("C:\\Users\\Thinkpad\\Desktop\\测试简历\\腾讯军事_腾讯网.html"),"utf-8"); 
			Elements link = doc.select("a");
			int num = 0;
			Map<String,Integer> wordFreq = new HashMap<String,Integer>();
			for(Element ele :link){
				try{
					if(ele.attr("href").indexOf("news.qq.com/a/") != -1){
						System.out.println(ele.attr("href"));
						String news = Jsoup.parse(new URL(ele.attr("href")), 5000).select("#Cnt-Main-Article-QQ").text();
						String[] words = IKSpliter.splitNews(news);
						for(String str:words){
							if(wordFreq.get(str) == null)
								wordFreq.put(str, 0);
							wordFreq.put(str, wordFreq.get(str)+1);
						}
						num ++;
					}
				}catch(Exception e){
					e.printStackTrace();
				}

			}
			for(String str:wordFreq.keySet()){
				if(wordFreq.get(str)>30)
				System.out.println("key word:"+str+",freq:"+wordFreq.get(str));
			}
			return wordFreq;
//			System.out.println("words amount:"+wordFreq.size());
//			System.out.println("news number:"+num);
//			String objectPath = "D:\\home\\data\\wordFreq.object";
//			ObjectOutputStream  oos = new ObjectOutputStream(new FileOutputStream(objectPath));
//			oos.writeObject(wordFreq);
//			oos.flush();
//			oos.close();
//			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(objectPath));
//			Map<String,Integer> wordfreq = (Map<String, Integer>) ois.readObject();
//			ois.close();
//			System.out.println(wordfreq.size());
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
