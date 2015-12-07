package cn.larry.sample;

/**
 * Created by Thinkpad on 2015/12/6.
 */
public class WeightedQuickUnion extends UnionFind {

    private int[] size;

    public WeightedQuickUnion(int N) {
        super(N);
        size = new int[N];
        for (int i = 0; i < size.length; i++)
            size[i] = 1;
    }

    @Override
    public int find(int p) {
        while (p != id[p])
            p = id[p];
        return p;
    }

    @Override
    public void union(int p, int q) {
        int proot = find(p);
        int qroot = find(q);
        if (proot == qroot) return;
        int big = size[proot] >= size[qroot] ? proot : qroot;
        int small = size[proot] >= size[qroot] ? qroot : proot;
        id[small] = big;
        size[big] += size[small];
        count--;
//        if (size[proot] >= size[qroot]) {
//            id[qroot] = proot;
//            size[proot] += size[qroot];
//        } else {
//            id[proot] = qroot;
//            size[qroot] += size[proot];
//        }
//        count--;
    }
}
