package cn.larry.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * 有向图
 * Created by Thinkpad on 2015/12/6.
 */
public class Digraph {
    private final int V;  //顶点总数
    private int E;         //边总数
    private List<Integer>[] adj; //以顶点为下标的边的邻接表

    public Digraph(int V) {
        this.V = V;
        this.E = 0;
        adj =new ArrayList[V];
        for (int v = 0; v < V; v++)
            adj[v] = new ArrayList<>();
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public void addEdge(int v, int w) {
        adj[v].add(w);
        E++;
    }

    public List<Integer> adj(int v) {
        return adj[v];
    }

    /**
     * 翻转有向图
     * @return
     */
    public Digraph reverse() {
        Digraph R = new Digraph(V);
        for (int v = 0; v < V; v++)
            for (int w : adj(v))
                R.addEdge(w, v);
        return R;
    }

}
