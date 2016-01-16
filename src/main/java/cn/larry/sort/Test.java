package cn.larry.sort;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by larryfu on 2016/1/8.
 *测试递归最大深度 本机上值为17000左右
 * @author larryfu
 */
public class Test {
    public static final AtomicInteger ai = new AtomicInteger();
    public static void main(String[] args) {
        try {
            test();
        }catch (Throwable t){
            t.printStackTrace();
            System.out.println(ai.get());
        }
    }

    public static void test(){
        ai.incrementAndGet();
        test();
    }
}
