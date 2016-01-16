package cn.larry.sort;

import java.time.LocalTime;
import java.util.Random;

/**
 * Created by larryfu on 2016/1/7.
 * 冒泡排序
 * @author larryfu
 */
public class BubbleSort {

    public static void sort(int[] nums) {
        for (int i = 1; i < nums.length; i++)  // j 只需要达到nums.length-2 即倒数第二个元素即可，故i从1开始
            for (int j = 0; j < nums.length - i; j++)
                if (nums[j] > nums[j + 1]) {
                    int temp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = temp;
                }
    }

    public static void main(String[] args) {
        int num = 10000;
        int[] ints = new int[num];
        Random rand = new Random();
        for (int i = 0; i < num; i++)
            ints[i] = rand.nextInt(num * 10);
        System.out.println("start" + LocalTime.now());
        sort(ints);
        System.out.println("end" + LocalTime.now());
    }
}
