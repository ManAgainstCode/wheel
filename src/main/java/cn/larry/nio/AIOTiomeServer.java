package cn.larry.nio;

/**
 * Created by larryfu on 16-2-14.
 */
public class AIOTiomeServer {
    public static void main(String[] args) {
        int port = 8080;
        AsyncTimeServerHandler serverHandler = new AsyncTimeServerHandler(port);
        new Thread(serverHandler, "AsyncTimeServerHandler-001").start();
    }
}
