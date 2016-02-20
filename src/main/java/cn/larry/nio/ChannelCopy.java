package cn.larry.nio;

import java.io.*;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.time.LocalDateTime;

/**
 * Created by larryfu on 16-2-14.
 */
public class ChannelCopy {

    public static void main(String[] args) {
        String src = "/home/larryfu/ide/ideaIC-15.0.3.tar.gz";
        String dest1 = "/home/larryfu/ideaIC-15.0.3.tar.gz1";
        String dest2 = "/home/larryfu/ideaIC-15.0.3.tar.gz2";
        //    LocalDateTime start = LocalDateTime.now();
        for (int i = 2; i < 128; i += 4) {
            int size = i * 1024;
            long start = System.currentTimeMillis();
            copy(src, dest1, size);
            //  LocalDateTime medium = LocalDateTime.now();
            long medium = System.currentTimeMillis();
            channelCopy(src, dest2, size);
            //LocalDateTime end = LocalDateTime.now();
            long end = System.currentTimeMillis();
            System.out.println("size " + size + " copy Time:" + (medium - start));
            System.out.println("size " + size + " channel copy Time:" + (end - medium));
            new File(dest1).delete();
            new File(dest2).delete();
        }

    }

    public static void copy(String src, String dest, int size) {
        try (FileInputStream fis = new FileInputStream(src);
             FileOutputStream fos = new FileOutputStream(dest)) {

            byte[] bytes = new byte[size];
            int len = 0;
            while ((len = fis.read(bytes)) > 0)
                fos.write(bytes, 0, len);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void channelCopy(String src, String dest, int size) {
        try {
            FileChannel in = new FileInputStream(src).getChannel(),
                    out = new FileOutputStream(dest).getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(size);
            while (in.read(buffer) != -1) {
                buffer.flip();
                out.write(buffer);
                buffer.clear();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

