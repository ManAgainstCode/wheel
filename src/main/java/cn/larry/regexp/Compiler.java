package cn.larry.regexp;

import cn.larry.graph.Digraph;
import cn.larry.graph.DirectedDFS;

import java.util.*;

/**
 * Created by larryfu on 16-1-31.
 */
public class Compiler {

    private char[] re;   //匹配转换
    private Digraph G; //epsilon 转换
    private int M;    //状态数量

    public Compiler(String regexp) {
        Stack<Integer> ops = new Stack<>();
        re = processPlus(regexp.toCharArray());
        re = processTimes(re);
        M = re.length;
        G = new Digraph(M + 1);
        for (int i = 0; i < M; i++) {
            int lp = i;
            if (re[i] == '(' || re[i] == '|')
                ops.push(i);
            else if (re[i] == ')') {
                int or = ops.pop();
                if (re[or] == '|') {
                    List<Integer> cl = new ArrayList<>();
                    while (re[or] == '|') {
                        cl.add(or);
                        or = ops.pop();
                    }
                    lp = or;
                    for (Integer integer : cl) {
                        G.addEdge(integer, i);
                        G.addEdge(lp, integer + 1);
                    }
                } else lp = or;
            }
            if (i < M - 1 && re[i + 1] == '*') {
                G.addEdge(lp, i + 1);
                G.addEdge(i + 1, lp);
            }
            if (i < M - 1 && re[i + 1] == '?')
                G.addEdge(lp, i + 1);
            if (re[i] == '(' || re[i] == '*' || re[i] == ')' || re[i] == '?')
                G.addEdge(i, i + 1);
        }
    }

    /**
     * 将+号转换为*号
     *
     * @param re
     * @return
     */
    private static char[] processPlus(char[] re) {
        Stack<Integer> stack = new Stack<>();
        int lp;
        Map<Integer, char[]> plusChars = new TreeMap<>();
        for (int i = 0; i < re.length; i++) {
            lp = i;
            if (re[i] == '|' || re[i] == '(')
                stack.push(i);
            if (re[i] == ')') {
                lp = stack.pop();
                while (re[lp] == '|')
                    lp = stack.pop();
            }
            if (i < re.length - 1 && re[i + 1] == '+') {
                char[] repeat = new char[i + 1 - lp];
                System.arraycopy(re, lp, repeat, 0, repeat.length);
                plusChars.put(lp, repeat);
                re[i + 1] = '*';
                break;
            }
        }
        int index = 0;
        List<char[]> clist = new ArrayList<>();
        if (plusChars.size() == 0)
            return re;
        for (Map.Entry<Integer, char[]> entry : plusChars.entrySet()) {
            char[] cs = new char[entry.getKey() - index];
            System.arraycopy(re, index, cs, 0, cs.length);
            clist.add(cs);
            clist.add(entry.getValue());
            index = entry.getKey();
        }
        char[] chars = new char[re.length - index];
        System.arraycopy(re, index, chars, 0, chars.length);
        clist.add(chars);
        StringBuilder sb = new StringBuilder();
        clist.forEach(sb::append);
        System.out.println(sb.toString());
        return processPlus(sb.toString().toCharArray());
    }

    /**
     * 处理次数匹配
     *
     * @param re
     * @return
     */
    private static char[] processTimes(char[] re) {
        int lp = 0, end = 0;
        Stack<Integer> stack = new Stack<>();
        Times time = null;
        char[] repeat = null;
        for (int i = 0; i < re.length; i++) {
            lp = i;
            if (re[i] == '|' || re[i] == '(')
                stack.push(i);
            if (re[i] == ')') {
                lp = stack.pop();
                while (re[lp] == '|')
                    lp = stack.pop();
            }
            if (i < re.length - 1 && re[i + 1] == '{') {
                if (re[i + 3] == '}') {
                    time = new Times(re[i + 2] - '0');
                    end = i + 4;
                } else if (re[i + 5] == '}') {
                    time = new Times(re[i + 2] - '0', re[i + 4] - '0');
                    end = i + 6;
                } else throw new IllegalArgumentException();
                repeat = new char[i + 1 - lp];
                System.arraycopy(re, lp, repeat, 0, repeat.length);
                break;
            }
        }
        if (time == null)
            return re;
        char[] prefix = new char[lp];
        System.arraycopy(re, 0, prefix, 0, prefix.length);
        char[] suffix = new char[re.length - end];
        System.arraycopy(re, end, suffix, 0, suffix.length);
        List<char[]> middle = new ArrayList<>();
        for (int i = 0; i < time.min; i++)
            middle.add(repeat);
        char[] exists = new StringBuilder().append(repeat).append('?').toString().toCharArray();
        for (int j = 0; j < time.max - time.min; j++)
            middle.add(exists);
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        middle.forEach(sb::append);
        sb.append(suffix);
        System.out.println(sb.toString());
        return processTimes(sb.toString().toCharArray());
    }

    public static boolean matches(char fact, char pattern) {
        if (fact == pattern || pattern == '.')
            return true;
        //if()
        return  false;
    }

    public boolean recognizes(String txt) {
        List<Integer> pc = new ArrayList<>();
        DirectedDFS dfs = new DirectedDFS(G, 0);
        for (int v = 0; v < G.V(); v++)
            if (dfs.marked(v)) pc.add(v);
        for (int i = 0; i < txt.length(); i++) {
            List<Integer> match = new ArrayList<>();
            for (int v : pc)
                if (v < M && (re[v] == txt.charAt(i) || re[v] == '.'))
                    match.add(v + 1);
            pc = new ArrayList<>();
            dfs = new DirectedDFS(G, match);
            for (int v = 0; v < G.V(); v++)
                if (dfs.marked(v)) pc.add(v);
        }
        for (int v : pc) if (v == M) return true;
        return false;
    }

    public static void mains(String[] args) {
        String regexp = "(ba*){2,3}dd{2,4}";
        System.out.println(processTimes(regexp.toCharArray()));
    }

    public static void main(String[] args) {
        String regexp = "bad?((a|b|c)+c)+ab";
        String reg = "(a|b){2,4}";
        Compiler nfa = new Compiler(reg);
        System.out.println(nfa.recognizes("aabbb"));
    }
}

class Times {
    int min;
    int max;

    public Times(int min, int max) {
        if (max < min || min < 0)
            throw new IllegalArgumentException();
        this.min = min;
        this.max = max;
    }

    public Times(int time) {
        this(time, time);
    }

}