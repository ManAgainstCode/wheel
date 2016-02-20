package cn.larry.nio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by larryfu on 16-2-14.
 */
public class SyncTimeServer {
    public static void main(String[] args) throws IOException {

        int port = 8080;
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("the time server start at :" + port);
            Socket socket = null;
            while (true) {
                socket = server.accept();
                new Thread(new SyncTimeServerHandler(socket)).start();
            }
        } finally {
            if (server != null) {
                System.out.println("server close");
                server.close();
                server = null;
            }
        }
    }
}
