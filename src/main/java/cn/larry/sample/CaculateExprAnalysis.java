package cn.larry.sample;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * 算术表达式解析,先将中缀表达式解析为逆波兰表达式，后运算
 * Created by Thinkpad on 2015/10/14.
 */
public class CaculateExprAnalysis {

    public CaculateExprAnalysis(String expr) {

    }



    public static void main(String[] args) {
        String expr = "(((5+3)*2-((2+5)*3-8)-9+61))";
        System.out.println(caculate(expr));
    }

    public static int caculate(String expr){
        return caculate(generatePoland(analysis(expr)));
    }

    /**
     * 去除表达式两端的无用括号
     * @param expr
     * @return
     */
    public static String removeUselessBarcket(String expr) {
        int start = 0, end = 0;
        for (int i = 0; i < expr.length(); i++)
            if (expr.charAt(i) == '(')
                start++;
            else break;
        for (int i = expr.length() - 1; i > 0; i--)
            if (expr.charAt(i) == ')')
                end++;
            else break;
        int num = Math.min(start,end);
        int validNum = 0;
        Stack<Integer> startBarckets = new Stack<>();
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == '(')
                startBarckets.push(i);
            if (c == ')') {
                int leftIndex = startBarckets.pop();
                if (i >= expr.length() - num && leftIndex < num) {
                    validNum = leftIndex + 1;
                    break;
                }
            }
        }
        expr = expr.replaceAll("^\\({" + validNum + "}", "");
        expr = expr.replaceAll("\\){" + validNum + "}$", "");
        return expr;
    }

    /**
     *得到表达式的你波兰表达式形式
     */
    public static String analysis(String expr) {
        if (expr.matches("\\d+"))
            return expr;

        List<String> exprPart = new LinkedList<>();
        List<Character> operators = new ArrayList<>();

        expr = removeUselessBarcket(expr);
        int len = expr.length();
        int leftBarcketNum = 0;
        int leftIndex = 0;
        for (int i = 0; i < len; i++) {
            char c = expr.charAt(i);
            if (leftBarcketNum == 0 && (c == '+' || c == '-' || c == '*' || c == '/')) {
                String part = expr.substring(leftIndex, i);
                if (part.length() > 0)
                    exprPart.add(part);
                operators.add(c);
                leftIndex = i + 1;
            }
            if (c == '(')
                leftBarcketNum++;
            if (c == ')')
                leftBarcketNum--;
            if (i == len - 1)
                exprPart.add(expr.substring(leftIndex));
        }
        for (int i = 0; i < exprPart.size(); i++)
            exprPart.set(i, analysis(exprPart.get(i)));

        for (int i = 0; i < operators.size(); i++) {
            char c = operators.get(i);
            if (c == '*' || c == '/') {
                String newExpr = exprPart.get(i) + "_" + exprPart.get(i + 1) + c;
                exprPart.set(i, newExpr);
                exprPart.set(i + 1, "");
            }
        }

        List<String> leftExprPart = exprPart.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());

        List<Character> leftOperators = operators.stream().filter(c -> c == '+' || c == '-').collect(Collectors.toList());

        for (int i = 0; i < leftOperators.size(); i++)
            leftExprPart.set(i + 1, leftExprPart.get(i) + "_" + leftExprPart.get(i + 1) + leftOperators.get(i)); //使用_分隔不同的数字

        return leftExprPart.get(leftExprPart.size() - 1);
    }

    /**
     * 根据逆波兰表达式的字符串形式生成队列形式
     * @param expr
     * @return
     */
    public static Queue<String> generatePoland(String expr) {
        Queue<String> polandQueue = new LinkedList<>();
        while (!expr.isEmpty()) {
            String ele = getEle(expr);
            if (!ele.isEmpty()) {
                polandQueue.offer(ele);
                expr = expr.substring(ele.length());
                if (!expr.isEmpty() && expr.charAt(0) == '_')
                    expr = expr.substring(1);
            }
        }
        return polandQueue;
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private static String getEle(String expr) {
        int len = 0;
        char s = expr.charAt(0);
        if (isOperator(s))
            return expr.substring(0, 1);
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (isOperator(c)) {
                len = i;
                break;
            }
            if (c == '_') { //使用_分隔不同的数值
                len = i;
                break;
            }
        }
        return expr.substring(0, len);
    }

    /**
     * 根据队列计算出结果
     * @return
     */
    public static int caculate(Queue<String> poland) {
        Stack<Integer> numStack = new Stack<>();
        while (!poland.isEmpty()) {
            String s = poland.poll();
            if (isNumber(s))
                numStack.push(Integer.parseInt(s));
            if (isOperator(s.charAt(0))) {
                int second = numStack.pop();
                int first = numStack.pop();
                numStack.push(caculate(first, second, s.charAt(0)));
            }
        }
        return numStack.pop();
    }

    private static int caculate(int first, int second, char operator) {
        if (operator == '+')
            return first + second;
        if (operator == '-')
            return first - second;
        if (operator == '*')
            return first * second;
        if (operator == '/')
            return (int) (first / second);
        return 0;
    }

    private static boolean isNumber(String s) {
        return s.matches("\\d+");
    }
}
