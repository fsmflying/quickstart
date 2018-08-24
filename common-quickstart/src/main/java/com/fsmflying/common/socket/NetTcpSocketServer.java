package com.fsmflying.common.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
//import java.net.UnknownHostException;

public class NetTcpSocketServer {
    public static void main(String args[]) {
        startupServer();
        //startupServer2();
    }

    public static void startupServer() {
        System.out.println("启动服务端!");
        ServerSocket server;
        Socket socket;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            server = new ServerSocket(8888);
            socket = server.accept();
            System.out.println(socket.getInetAddress().getHostAddress() + " has connected !");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //PrintWriter pw = new PrintWriter(socket.getOutputStream());
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("disc")) //重新等待新的连接.
                {
                    reader.close();
                    writer.close();
                    socket.close();
                    socket = server.accept();
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                }
                if (line.equals("closeServer")) {
                    //System.out.println("closeServer");
                    //break;
                }
                System.out.println("收到内容为:" + line);
                writer.write(line.toUpperCase());
                writer.newLine();
                writer.flush();
            }
            reader.close();
            writer.close();
            socket.close();
            server.close();
            System.out.println(socket.getInetAddress().getHostAddress() + " has closed !");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    private static void startupServer2() {
        System.out.println("启动服务端!");
        ServerSocket server;
        Socket socket;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            server = new ServerSocket(8888);
            socket = server.accept();
            System.out.println(socket.getInetAddress().getHostAddress() + " has connected !");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("over")) //重新等待新的连接.
                {
                    break;
                }
                System.out.println("客户端内容为:" + line);
                writer.write(line.toUpperCase());
                writer.newLine();
                writer.flush();
            }
            reader.close();
            writer.close();
            socket.close();
            server.close();
            System.out.println(socket.getInetAddress().getHostAddress() + " has closed !");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
