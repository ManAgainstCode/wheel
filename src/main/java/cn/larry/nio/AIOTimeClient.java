package cn.larry.nio;

/**
 * Created by larryfu on 16-2-14.
 */
public class AIOTimeClient {

    public static void main(String[] args) {
        new Thread(new AsyncTimeClientHandler("127.0.0.1", 8080), "AIOTimeClient-001").start();
    }
}
