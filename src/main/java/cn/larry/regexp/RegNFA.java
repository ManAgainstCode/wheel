package cn.larry.regexp;

import cn.larry.graph.Digraph;
import cn.larry.graph.DirectedDFS;

import java.util.*;

/**
 * Created by larryfu on 16-1-31.
 */
public class RegNFA {

    private char[] re;   //匹配转换
    private Digraph G; //epsilon 转换
    private int M;    //状态数量


    private Set<Integer> pc;

    private StringBuilder currentStr;

    public RegNFA(String regexp) {

        regexp = NFAUtils.processEscape(regexp);
        Stack<Integer> ops = new Stack<>();
        re = NFAUtils.processPlus(regexp.toCharArray());
        re = NFAUtils.processTimes(re);
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
        reset();
    }


    public boolean input(char c, String txt, int index) {
        List<Integer> match = new ArrayList<>();
        for (int v : pc)
            if (v < M && (NFAUtils.reconizeStartEnd(re[v], txt, index) || NFAUtils.equal(re[v], c)))
                match.add(v + 1);
        DirectedDFS dfs = new DirectedDFS(G, match);
        pc = new HashSet<>();
        for (int v = 0; v < M; v++)
            if (dfs.marked(v))
                pc.add(v);
        if (pc.size() > 0)
            currentStr.append(c);
        return pc.size() > 0;
    }

    public String getCurrentMatch() {
        return currentStr.toString();
    }

    public boolean accept() {
        for (Integer i : pc)
            if (i == M)
                return true;
        return false;
    }

    public void reset() {
        currentStr = new StringBuilder();
        pc = new HashSet<>();
        DirectedDFS dfs = new DirectedDFS(G, 0);
        for (int v = 0; v < G.V(); v++)
            if (dfs.marked(v))
                pc.add(v);
    }

    public boolean recognizes(String txt) {
        Set<Integer> pc = new HashSet<>();
        DirectedDFS dfs = new DirectedDFS(G, 0);
        for (int v = 0; v < G.V(); v++)
            if (dfs.marked(v)) pc.add(v);
        for (int i = 0; i < txt.length(); i++) {
            List<Integer> match = new ArrayList<>();
            for (int v : pc)
                if (v < M &&
                        //(re[v] == txt.charAt(i) || re[v] == '.'))
                        (NFAUtils.reconizeStartEnd(re[v], txt, i) || NFAUtils.equal(re[v], txt.charAt(i))))
                    match.add(v + 1);
            pc = new HashSet<>();
            dfs = new DirectedDFS(G, match);
            for (int v = 0; v < G.V(); v++)
                if (dfs.marked(v)) pc.add(v);
        }
        for (int v : pc) if (v == M) return true;
        return false;
    }

    public static void main(String[] args) {
//        String regexp = "\\(bad?((a|b|c)+c)+ab";
        //String reg = "\\((a|b){2,4}";
        String reg = "\\(ab(a|b)";
        RegNFA nfa = new RegNFA(reg);
        System.out.println(nfa.recognizes("(abb"));
        //    System.out.println(replaceAll("that at t", "at", "isa"));
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