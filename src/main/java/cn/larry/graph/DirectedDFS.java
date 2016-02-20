package cn.larry.graph;

import java.util.List;

/**
 * 有向图的可达性
 * Created by Thinkpad on 2015/12/6.
 */
public class DirectedDFS {

    private boolean[] marked;

    public DirectedDFS(Digraph G, int s) {
        marked = new boolean[G.V()];
        dfs(G, s);
    }

    public DirectedDFS(Digraph G, List<Integer> sources) {
        marked = new boolean[G.V()];
        for (Integer i : sources)
            if (!marked[i]) dfs(G, i);
    }

    private void dfs(Digraph G,int v){
        marked[v] =true;
        for(int w:G.adj(v))
            if(!marked[w])dfs(G,w);
    }

    public boolean marked(int v){
        return marked[v];
    }
}
