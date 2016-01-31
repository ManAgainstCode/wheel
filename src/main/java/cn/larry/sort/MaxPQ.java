package cn.larry.sort;

/**
 * Created by larryfu on 2016/1/9.
 *
 * @author larryfu
 */
public interface MaxPQ {

    void insert(int i);

    int max();

    int delMax();

    boolean isEmpty();

    int size();
}
