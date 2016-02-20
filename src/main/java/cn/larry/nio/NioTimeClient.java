package cn.larry.nio;

import java.time.LocalDateTime;

/**
 * Created by larryfu on 16-2-14.
 */
public class NioTimeClient {

    public static void main(String[] args) {
        int port = 8080;
        System.out.println(LocalDateTime.now());
        for (int i = 0; i < 4000; i++)
            new Thread(new NioTimeClientHandler("127.0.0.1", port), "TimeCliend-" + i).start();
        System.out.println(LocalDateTime.now());
    }
}
