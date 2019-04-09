package com.fsmflying.netty.bio;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@SuppressWarnings("all")
public class BioServer {
    public static void main(String[] args) {
        ServerSocket serverSocket;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        Socket socket = null;
        try {
            System.out.println("服务器启动");
            serverSocket = new ServerSocket(8080);
            while (true) {
                socket = serverSocket.accept();
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                //Stream<String> lines = bufferedReader.lines();
                String line = reader.readLine();
//                writer.println("response:" + line);
                writer.write(line);
                writer.newLine();
                writer.flush();
                if ("quit".equals(line)) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (writer != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
