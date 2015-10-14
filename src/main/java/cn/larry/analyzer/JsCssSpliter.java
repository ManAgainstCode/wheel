package cn.larry.analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsCssSpliter {

	public static void main(String[] args){
		try {
			String path = "C:\\Users\\Thinkpad\\Desktop\\woshou";
			File dir  = new File(path);
			processDir(dir);
		}	catch(Exception e){
			e.printStackTrace();
		}
	}
	
	static void processDir(File dir){
		File[] files = dir.listFiles();
		for(File fl:files){
			if(fl.isDirectory()){
				processDir(fl);
			}else if(fl.getName().endsWith(".html")||fl.getName().endsWith(".jsp")){
				processHtml(fl.getAbsolutePath());
			}
		}
	}
	
	static void processHtml(String file){
		try {
			String html = getFileContent(file);
			Document doc = Jsoup.parse(html);
			extractJs(doc,file);
			extractCss(doc,file);
			saveFile(file, doc.html(), false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	catch(Exception e){
			e.printStackTrace();
		}
	}
	
	static void extractJs(Document doc,String filepath) throws FileNotFoundException{
		Elements eles = doc.select("script");
		if(eles.size() == 0)
			return;
		Element ele  = eles.get(eles.size()-1);
		if(!StringUtils.isEmpty(ele.attr("src")))
			return ;
		File fl = new File(filepath);
		String name = fl.getName();
		name = name.split("\\.")[0];
		name = name+".js";
		String path = fl.getParent();
		File dir = new File(path+File.separator+"js");
		if(!dir.exists())
			dir.mkdirs();
		saveFile(path+File.separator+"js"+File.separator+name,ele.data(),true);
		ele.remove();
		String scripttag = "<script src='"+"js/"+name+"'></script>";
		doc.body().append(scripttag);
	}
	
	static void extractCss(Document doc,String filepath) throws FileNotFoundException{
		Elements eles = doc.select("style");
		if(eles.size() == 0)
			return;
		Element ele  = eles.get(eles.size()-1);
		File fl = new File(filepath);
		String name = fl.getName();
		name = name.split("\\.")[0];
		name = name+".css";
		String path = fl.getParent();
		File dir = new File(path+File.separator+"css");
		if(!dir.exists())
			dir.mkdirs();
		saveFile(path+File.separator+"css"+File.separator+name,ele.data(),true);
		ele.remove();
		String styletag = "<link href='"+"css/"+name+"' rel='stylesheet' type='text/css'  />";
		doc.head().append(styletag);
	}

	public static void saveFile(String name,String content,boolean append) throws FileNotFoundException{
		try(PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(name,append)))){
			pw.append(content);
			pw.flush();
		}
	}
	
	public static String getFileContent(String filename) throws  IOException{
			StringBuilder sb = new StringBuilder(); 
	        try(BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filename)))){
	        	String line;    	
	            while ((line = in.readLine()) != null) 
	            	sb.append(line+"\n");
	        }
	        return sb.toString();
	}
}
