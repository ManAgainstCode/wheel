package cn.larry.sort;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by larryfu on 2016/1/9.
 * 插入排序
 *
 * @author larryfu
 */
public class InsertSort {

    public static int[] sort(int[] ints) {
        int[] nums = new int[ints.length];
        for (int i = 0; i < ints.length; i++) {
            nums[i] = ints[i];
            for (int j = 0; j < i; j++)
                if (nums[j] > nums[i]) {
                    moveElement(nums, i, j);
                    break;
                }
        }
        for (int i = 0; i < ints.length; i++) {
            ints[i] = nums[i];
        }
        return ints;
    }


    private static void moveElement(int[] nums, int from, int to) {
        if (from == to)
            return;
        int num = nums[from];
        if (from < to) {
            for (int i = from; i < to; i++)
                nums[i] = nums[i + 1];
        } else {
            for (int i = from; i > to; i--)
                nums[i] = nums[i - 1];
        }
        nums[to] = num;
    }

    public static void main(String[] args) {
        int num = 10;
        int[] ints = new int[num];
        Random rand = new Random();
        for (int i = 0; i < num; i++)
            ints[i] = rand.nextInt(num * 10);
        System.out.println("start" + LocalTime.now());
        sort(ints);
        System.out.println("end" + LocalTime.now());
        System.out.println(Arrays.toString(ints));
    }
}
