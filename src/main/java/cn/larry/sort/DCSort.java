package cn.larry.sort;

import java.time.LocalTime;
import java.util.Random;

/**
 * Created by larryfu on 2016/1/8.
 * 归并排序
 *
 * @author larryfu
 */
public class DCSort {

    public static void sort(int[] nums) {
        for (int i = 1; i < nums.length; i = i << 1) { //按照1 2 4 8 ...的尺度进行归并
            //以i 为尺度进行一次归并
            for (int j = 0; j < nums.length - i; //如果剩余的step长度数组只有一个则不用再排序
                 j += (i << 1))
                dcOnce(nums, j, i);
        }
    }

    private static void dcOnce(int[] nums, int start, int step) {
        int end = Math.min(nums.length, start + 2 * step); //数组可能到达结尾，使得后一个字数组长度小于step
        int len = end - start;
        int[] temp = new int[len];
        for (int i = start, j = i + step, k = 0; k < len; k++) {
            if (i == start + step)  //第一个数组的元素已全部排完
                temp[k] = nums[j++];
            else if (j == end)  // 第二个数组的元素已全部排完
                temp[k] = nums[i++];
            else if (nums[i] > nums[j])  //选择较小的元素排到临时数组中
                temp[k] = nums[j++];
            else
                temp[k] = nums[i++];

        }
        for (int i = 0; i < len; i++)
            nums[start + i] = temp[i];
    }

    public static void main(String[] args) {
        int num = 467;
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
