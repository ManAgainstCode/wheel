package cn.larry.sort;

/**
 * Created by larryfu on 2016/1/9.
 * 二分查找
 *
 * @author larryfu
 */
public class BinarySearch {

    static int search(int[] ints, int num) {
        return searchOnce(ints, 0, ints.length - 1, num);
    }

    static int searchOnce(int[] ints, int start, int end, int number) {
        int medium = (start + end) / 2;
        if (ints[medium] == number)
            return medium;
        if (start == end)
            return -1;
        if (ints[medium] > number)
            end = Math.max(medium - 1, start);
        else
            start = Math.min(medium + 1, end);
        return searchOnce(ints, start, end, number);
    }

    public static void main(String[] args) {
        int[] ints = {1, 2, 5, 9, 10, 16, 22, 32, 41};
        System.out.println(search(ints, 7));
    }
}
