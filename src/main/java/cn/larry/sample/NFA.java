package cn.larry.sample;

import cn.larry.graph.Digraph;
import cn.larry.graph.DirectedDFS;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 基本正则表达式模式识别 识别.|*
 * Created by Thinkpad on 2015/12/6.
 */
public class NFA {
    private char[] re;   //匹配转换
    private Digraph G; //epsilon 转换
    private int M;    //状态数量

    public NFA(String regexp) {
        Stack<Integer> ops = new Stack<>();
        re = regexp.toCharArray();
        M = re.length;
        G = new Digraph(M + 1);

        for (int i = 0; i < M; i++) {
            int lp = i;
            if (re[i] == '(' || re[i] == '|')
                ops.push(i);
            else if (re[i] == ')') {
                int or = ops.pop();
                if (re[or] == '|') {
                    lp = ops.pop();
                    G.addEdge(lp, or + 1);
                    G.addEdge(or, i);
                } else lp = or;
            }
            if (i < M - 1 && re[i + 1] == '*') {
                G.addEdge(lp, i + 1);
                G.addEdge(i + 1, lp);
            }
            if (re[i] == '(' || re[i] == '*' || re[i] == ')')
                G.addEdge(i, i + 1);
        }
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

    public static void main(String[] args) {
        String regexp = "ba(a|b)*ab";
        NFA nfa = new NFA(regexp);
        System.out.println(nfa.recognizes("baaab"));
        System.out.println("ab".matches("a[^a]?b|rc{2}d"));
    }
}
