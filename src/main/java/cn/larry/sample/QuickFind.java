package cn.larry.sample;

/**
 * Created by Thinkpad on 2015/12/6.
 */
public class QuickFind extends UnionFind {

    public QuickFind(int N) {
        super(N);
    }

    @Override
    public int find(int p) {
        return id[p];
    }

    @Override
    public void union(int p, int q) {
        int pid = find(p);
        int qid = find(q);
        if (pid == qid) return;
        for (int i = 0; i < id.length; i++)
            if (id[i] == qid) id[i] = pid;
        count--;
    }
}
