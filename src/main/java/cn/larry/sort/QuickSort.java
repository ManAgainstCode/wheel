package cn.larry.sort;

import java.time.LocalTime;
import java.util.Random;

/**
 * Created by larryfu on 2016/1/7.
 * 快速排序
 *
 * @author larryfu
 */
public class QuickSort {

    public static void sort(int[] nums) {
        if (nums == null || nums.length == 0) return;
        sortOnce(nums, 0, nums.length - 1);
    }

    private static void sortOnce(int[] nums, int start, int end) {
        if (start == end) return;
        int num = nums[start];
        for (int i = start + 1, j = end; i <= j; ) {
            while (nums[i] <= num && i < end) i++;
            while (nums[j] >= num && j > start) j--;
            if (i < j) swap(nums, i, j);
            else {
                moveElement(nums, start, j);
                sortOnce(nums, start, Math.max(start, j - 1));
                sortOnce(nums, Math.min(end, j + 1), end);
                return;
            }
        }
    }

    private static void moveElement(int[] nums, int start, int end) {
        int medium = nums[start];
        for (int i = start; i < end; i++)
            nums[i] = nums[i + 1];
        nums[end] = medium;
    }

    private static void swap(int[] nums, int index1, int index2) {
        int temp = nums[index1];
        nums[index1] = nums[index2];
        nums[index2] = temp;
    }

    public static void main(String[] args) {
        int num = 400;
        int[] ints = new int[num];
        Random rand = new Random();
        for (int i = 0; i < num; i++)
            ints[i] = rand.nextInt(num * 3);
        System.out.println("start" + LocalTime.now());
        sort(ints);
        System.out.println("end" + LocalTime.now());
        for (int i = 0; i < num; i++)
            System.out.print(ints[i] + ",");
    }
}
