package cn.larry.sample;

/**
 * Created by larryfu on 15-10-7.
 */
public class StringSearch {
    public static void main(String[] args){
        String txt = "this is a island";
        String pattern = "is";
        System.out.println(lastIndexof(txt, pattern));
    }

    /**
     * 字符串搜索的暴力方法
     * 
     * @param txt
     * @param pattern
     * @return
     */
    public static int indexof(String txt,String pattern){

        int txtLen = txt.length();
        int ptLen = pattern.length();

        if(ptLen>txtLen)
            return -1;
        for(int i=0;i<txtLen-ptLen+1;i++){
            int j;
            for( j=0;j<pattern.length();j++){
                if( txt.charAt(i+j) != pattern.charAt(j))
                    break;
            }
            if(j == pattern.length())
            	return i;
        }
        return -1;
    }

    public static int lastIndexof(String txt,String pattern){
        int txtLen = txt.length();
        int ptLen = pattern.length();

        if(ptLen>txtLen)
            return -1;
        for(int i=txtLen-ptLen+1;i>=0;i--){
            int j;
            for( j=0;j<pattern.length();j++){
                if( txt.charAt(i+j) != pattern.charAt(j))
                    break;
            }
            if(j == pattern.length())
                return i;
        }
        return -1;
    }
}
