package com.fsmflying.netty.bio;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

@SuppressWarnings("all")
public class BioClient2 {


    public static void main(String args[]) {
        startupClient();
    }

    private static void startupClient() {
        // TODO Auto-generated method stub
        System.out.println("启动客户端!");
        try {
            Socket socket = new Socket("127.0.0.1", 8888);
            BufferedReader readerForKey = new BufferedReader(new InputStreamReader(System.in));
            BufferedWriter writerForNet = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader readerForNet = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                String line = readerForKey.readLine();
                if (line.equals("over")) break;
                writerForNet.write(line);
                writerForNet.newLine();
                writerForNet.flush();
                System.out.println("服务器端回传内容为:" + readerForNet.readLine());
            }
            writerForNet.close();
            readerForKey.close();
            socket.close();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
