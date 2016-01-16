package cn.larry.sort;

import java.time.LocalTime;
import java.util.Random;

/**
 * Created by larryfu on 2016/1/9.
 *
 * @author larryfu
 */
public class HeapPQ implements MaxPQ {

    private int[] elements;

    private int size;

    HeapPQ(int[] nums) {
        elements = new int[nums.length + 1];
        size = 0;
        for (int i = 0; i < nums.length; i++)
            insert(nums[i]);
    }

    private void swap(int i, int j) {
        int temp = elements[i];
        elements[i] = elements[j];
        elements[j] = temp;
    }

    private void swim(int k) {
        for (; k > 1 && elements[k] > elements[k / 2]; k = k / 2)  //由于elements[0]不是有效元素，所以k不能为1 因为k/2=0
            swap(k, k / 2);
    }

    private void sink(int k) {
        while (k * 2 <= size) {
            int i = 2 * k;
            if (elements[i] < elements[i + 1] && i + 1 <= size) //需要保证i+1没有越界 有效元素为elements[1]-elements[size]
                i++;
            if (elements[i] > elements[k]) {
                swap(k, i);
                k = i;
            } else break;
        }
    }

    @Override
    public void insert(int i) {
        elements[++size] = i;
        swim(size);
    }

    @Override
    public int max() {
        return elements[1];
    }

    @Override
    public int delMax() {
        int max = elements[1];
        elements[1] = elements[size--];
        sink(1);
        return max;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    public static void main(String[] args) {
        int num = 40;
        int[] ints = new int[num];
        Random rand = new Random();
        for (int i = 0; i < num; i++)
            ints[i] = rand.nextInt(num * 10);
        System.out.println("start" + LocalTime.now());
        HeapPQ hpq = new HeapPQ(ints);
        System.out.println("end" + LocalTime.now());
        for (int i = 0; i < num; i++)
            System.out.print(hpq.delMax() + ",");
    }
}
