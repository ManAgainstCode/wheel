package cn.larry.nio;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by larryfu on 16-2-14.
 */
public class PoolSyncTimeServer {
    public static void main(String[] args) {
        int port = 8080;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            TimeServerHandlerExecutorPool pool = new TimeServerHandlerExecutorPool(50, 1000);
            while (true)
                pool.Execute(new SyncTimeServerHandler(serverSocket.accept()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
