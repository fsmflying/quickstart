package com.fsmflying.netty.bio;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("all")
public class BioServer2 {
    public static void main(String args[]) {
        startupServer();
    }

    public static void startupServer() {
        System.out.println("启动服务端!");
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8888);
            SocketHandlerPool socketHandlerPool = new SocketHandlerPool(10,100);
            while (true) {
//                handleRequest(serverSocket.accept());
//                new SocketHandler(serverSocket.accept()).start();
                socketHandlerPool.execute(new SocketHandler(serverSocket.accept()));
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static void handleRequest(Socket socket) {
        if (socket == null) {
            throw new IllegalArgumentException();
        }
        BufferedReader reader = null;
        BufferedWriter writer = null;
        String line;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            while ((line = reader.readLine()) != null) {
                System.out.println("收到内容为:" + line);
                writer.write(line.toUpperCase());
                writer.newLine();
                writer.flush();
                if (line.equals("disc")) //重新等待新的连接.
                {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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

    public static class SocketHandler extends Thread {
        Socket socket;
        public SocketHandler(Socket socket){
            this.socket = socket;
        }
        @Override
        public void run() {
            handleRequest(socket);
            //super.run();
        }
    }

    public static class SocketHandlerPool {
        ExecutorService executorService;

        public SocketHandlerPool(int maxSize, int queueSize) {
            executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                    maxSize, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueSize));
        }

        public void execute(Runnable task) {
            executorService.execute(task);
        }
    }
}
