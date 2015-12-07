package cn.larry.sample;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 *
 * Created by Thinkpad on 2015/10/14.
 */
public class ExprAnalysis {




    public ExprAnalysis(String expr){

    }


    public static void main(String[] args) {
        String expr = "5+3-1";
        System.out.println(analysis(expr));

    }

    /**
     *
     */
    public static  String analysis(String expr){
        if(expr.matches("\\d+"))
            return expr;

        List<String> exprPart = new LinkedList<>();
        List<Character> operators = new ArrayList<>();

        expr = expr.replaceAll("^\\(*","");
        expr = expr.replaceAll("\\)*$","");
        int len = expr.length();
        int leftBarcketNum = 0;
        int leftIndex = 0;
        for(int i = 0;i<len;i++){
            char c = expr.charAt(i);
            if(leftBarcketNum == 0 && ( c == '+'||c=='-'||c=='*'||c=='/')){
                String part = expr.substring(leftIndex,i);
                if(part.length()>0)
                    exprPart.add(part);
                operators.add(c);
                leftIndex = i+1;
            }
            if(c == '(' )
                leftBarcketNum++;
            if(c == ')')
                leftBarcketNum --;
        }
        for(int i=0;i<exprPart.size();i++)
            exprPart.set(i,analysis(exprPart.get(i)));

        for(int i = 0;i<operators.size();i++){
            char c = operators.get(i);
            if(c == '*' || c == '/'){
                String newExpr = exprPart.get(i)+exprPart.get(i+1)+c;
                exprPart.set(i,newExpr);
                exprPart.set(i+1,"");
            }
        }



        List<String> leftExprPart = exprPart.stream().filter((s)->{
            return !s.isEmpty();
        }).collect(Collectors.toList());

        List<Character> leftOperators = operators.stream().filter((c)->{
            return c=='+'||c=='-';
        }).collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();

        for(int i = 0 ;i<leftOperators.size();i++){
            leftExprPart.set(i + 1, leftExprPart.get(i) + leftExprPart.get(i + 1) + leftOperators.get(i));
        }

        return leftExprPart.get(leftExprPart.size()-1);
    }

    /**
     *
     * @return
     */
    public boolean validate(){
        return true;
    }

    /**
     *
     * @return
     */
    public int caculate(){
        return 0;
    }
}