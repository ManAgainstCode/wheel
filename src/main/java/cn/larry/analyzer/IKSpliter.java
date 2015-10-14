package cn.larry.analyzer;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;  
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;  
import org.apache.lucene.analysis.TokenStream;  
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;  
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.wltea.analyzer.lucene.IKAnalyzer;  
  
public class IKSpliter {  
	
	public static String[] splitNews(String newstext){
		try {
			List<String> wordsList =new ArrayList<String>();
			Analyzer anal=new IKAnalyzer(true);       
	        StringReader reader=new StringReader(newstext);  
	        //分词 
	         TokenStream ts = anal.tokenStream("", reader);
	
	        ts.reset();
	        CharTermAttribute term=ts.getAttribute(CharTermAttribute.class);  
	        //遍历分词数据  
	        while(ts.incrementToken()){  
	        	wordsList.add(term.toString());
	            //System.out.print(term.toString()+"|");  
	        }  
	        String[] strs = new String[wordsList.size()];
	        for(int i = 0;i<wordsList.size();i++)
	        	strs[i] = wordsList.get(i);
        return strs;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}  
	}
	
	public static void main(String[] args){
		try{
			Document doc = Jsoup.parse(new File("C:\\Users\\Thinkpad\\Desktop\\腾讯军事_腾讯网.html"), "utf-8"); 
			Elements link = doc.select("a");
			int num = 0;
			Map<String,Integer> wordFreq = new HashMap<String,Integer>();
			for(Element ele :link){
				try{
					if(ele.attr("href").indexOf("news.qq.com/a/") != -1){
						System.out.println(ele.attr("href"));
						String news = Jsoup.parse(new URL(ele.attr("href")), 5000).select("#Cnt-Main-Article-QQ").text();
						String[] words = splitNews(news);
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
			System.out.println("words amount:"+wordFreq.size());
			System.out.println("news number:"+num);
			String objectPath = "D:\\home\\data\\wordFreq.object";
			ObjectOutputStream  oos = new ObjectOutputStream(new FileOutputStream(objectPath));
			oos.writeObject(wordFreq);
			oos.flush();
			oos.close();
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(objectPath));
			Map<String,Integer> wordfreq = (Map<String, Integer>) ois.readObject();
			ois.close();
			System.out.println(wordfreq.size());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
    public static void main1(String[] args)  {
    	try{
    	
    		Document doc = Jsoup.parse(new URL("http://news.qq.com/military.shtml"),10000);
           // String text="基于java语言开发的轻量级的中文分词工具包";
    		String text = doc.select("#Cnt-Main-Article-QQ").text(); 
            //创建分词对象  
            Analyzer anal=new IKAnalyzer(true);       
            StringReader reader=new StringReader(text);  
            //分词  
            TokenStream ts=anal.tokenStream("", reader);  
            ts.reset();
            CharTermAttribute term=ts.getAttribute(CharTermAttribute.class);  
            //遍历分词数据  
            while(ts.incrementToken()){  
                System.out.print(term.toString()+"|");  
            }  
            reader.close();  
            System.out.println(); 
    	}catch(Exception e){
    		e.printStackTrace();
    	}
 
    }  
  
}  
