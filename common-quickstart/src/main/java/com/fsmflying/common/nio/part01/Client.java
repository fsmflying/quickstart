package com.fsmflying.common.nio.part01;


import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Objects;
import java.util.Scanner;

public class Client {

    public void sendMessage0(String host, int port, String message) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port));
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.write(message.getBytes());
            out.flush();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send_100_to_17531() throws InterruptedException {
        //new Thread(new ClientThread()).start();
        for (int i = 0; i < 100; i++) {
            sendMessage0(Part01Server.REMOTE_IP, 17531, i + ":17531");
            Thread.sleep(20);
        }
    }

    public void send_100_to_9898() throws InterruptedException {
        //new Thread(new ClientThread()).start();
        for (int i = 0; i < 100; i++) {
            sendMessage0(Part01Server.REMOTE_IP, 9898, i + ":9898");
            Thread.sleep(20);
        }
    }


    public static void sendMessage(String message, int port) {
        SocketChannel socketChannel = null;
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(Part01Server.REMOTE_IP, port));
            socketChannel.configureBlocking(false);

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            byteBuffer.put(message.getBytes());
            byteBuffer.flip();
            socketChannel.write(byteBuffer);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socketChannel != null && socketChannel.isConnected()) {
                    socketChannel.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }

    public void checkStatus(String input) {
        if ("exit".equals(input.trim())) {
            System.out.println("系统即将退出，bye~~");
            System.exit(0);
        }
    }

}

class ClientThread implements Runnable {
    private SocketChannel sc;
    private boolean isConnected = false;
    Client client = new Client();

    public ClientThread() {
        try {
            sc = SocketChannel.open();
            sc.configureBlocking(false);
            sc.connect(new InetSocketAddress(Part01Server.REMOTE_IP, Part01Server.PORT));
            while (!sc.finishConnect()) {
                System.out.println("同" + Part01Server.REMOTE_IP + "的连接正在建立，请稍等！");
                Thread.sleep(10);
            }
            System.out.println("连接已建立，待写入内容至指定ip+端口！时间为" + System.currentTimeMillis());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("请输入要发送的内容：");
                String nextLine = scanner.nextLine();
                client.checkStatus(nextLine);
                ByteBuffer byteBuffer = ByteBuffer.allocate(nextLine.length());
                byteBuffer.put(nextLine.getBytes());
                byteBuffer.flip(); // 写缓冲区的数据之前一定要先反转(flip)
                while (byteBuffer.hasRemaining()) {
                    sc.write(byteBuffer);
                }
                byteBuffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (Objects.nonNull(sc)) {
                try {
                    sc.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        } finally {
            if (Objects.nonNull(sc)) {
                try {
                    sc.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
