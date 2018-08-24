package com.fsmflying.common.socket;


//import java.net.DatagramSocket;
//import java.net.DatagramPacket;

import java.io.IOException;
//import java.net.UnknownHostException;
import java.net.ServerSocket;
import java.net.Socket;
//import java.net.InetAddress;
//import java.io.Reader;
import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
import java.io.BufferedReader;
//import java.io.BufferedWriter;
import java.io.PrintWriter;

public class NetIMServer {
    public static void main(String args[]) {
        try {
            @SuppressWarnings("resource")
            ServerSocket serverSocket = new ServerSocket(8888);
            int number = 1;
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("客户端" + number + "连接成功!");
                new Thread(new ServerStream(socket, number)).start();
                number++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ServerStream implements Runnable {

    private Socket mSocket;
    private int mNumber;

    public ServerStream(Socket socket, int number) {
        super();
        this.mSocket = socket;
        this.mNumber = number;
    }

    public Socket getSocket() {
        return this.mSocket;
    }

    public int getNumber() {
        return this.mNumber;
    }

    @Override
    public void run() {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            PrintWriter writer01 = new PrintWriter(mSocket.getOutputStream(), true);
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("线程" + this.mNumber + "收到消息:" + line);
                writer01.println(line.toUpperCase());
                if (line.equals("over")) {
                    break;
                }
            }
            reader.close();
            writer01.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
        /**/
    }
}


