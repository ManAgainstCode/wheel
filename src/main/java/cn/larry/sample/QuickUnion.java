package cn.larry.sample;

/**
 * Created by Thinkpad on 2015/12/6.
 */
public class QuickUnion extends UnionFind {

    public QuickUnion(int N) {
        super(N);
    }

    @Override
    public int find(int p) {
        while (id[p] != p)
            p = id[p];
        return p;
    }

    @Override
    public void union(int p, int q) {
        int proot = find(p);
        int qroot = find(q);
        if (qroot != proot)
            count--;
        id[qroot] = proot;
    }
}
