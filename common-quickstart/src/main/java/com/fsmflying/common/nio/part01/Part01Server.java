package com.fsmflying.common.nio.part01;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Part01Server {
    //    public final static String REMOTE_IP = "192.168.0.44";
    public final static String REMOTE_IP = "127.0.0.1";
    public final static int PORT = 17531;
    private static ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    private static ServerSocketChannel serverSocketChannel;
    private static boolean closed = false;

    public static void main(String[] args) throws IOException {
        //先确定端口号
        int port = PORT;
        if (args != null && args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        //打开一个ServerSocketChannel
        serverSocketChannel = ServerSocketChannel.open();

//        //获取ServerSocketChannel绑定的Socket
//        ServerSocket serverSocket = serverSocketChannel.socket();
//
//        //设置ServerSocket监听的端口
//        serverSocket.bind(new InetSocketAddress(port));

        serverSocketChannel.bind(new InetSocketAddress(port));

        //设置ServerSocketChannel为非阻塞模式
        serverSocketChannel.configureBlocking(false);

        //打开一个选择器
        Selector selector = Selector.open();

        //将ServerSocketChannel注册到选择器上去并监听accept事件
        //SelectionKey.OP_ACCEPT,SelectionKey.OP_CONNECT,SelectionKey.OP_READ,SelectionKey.OP_WRITE;
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (!closed) {
            //这里会发生阻塞，等待就绪的通道,但在每次select()方法调用之间，只有一个通道就绪了。
            selector.select();

            //获取SelectionKeys上已经就绪的集合
            Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();

            //遍历每一个Key
            while (selectionKeyIterator.hasNext()) {
                SelectionKey selectionKey = selectionKeyIterator.next();
                selectionKeyIterator.remove();
                System.out.println("##########################################################3");
                System.out.println("isConnectable:" + selectionKey.isConnectable());
                System.out.println("isAcceptable:" + selectionKey.isAcceptable());
                System.out.println("isReadable:" + selectionKey.isReadable());
                System.out.println("isWritable:" + selectionKey.isWritable());
                //通道上是否有可接受的连接
                if (selectionKey.isAcceptable()) {

                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
//                    SocketChannel socketChannel = serverSocketChannel.accept(); // accept()方法会一直阻塞到有新连接到达。
                    System.out.println(socketChannel.getClass().getCanonicalName());
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ /*| SelectionKey.OP_WRITE*/);
                } else if (selectionKey.isReadable()) {   //通道上是否有数据可读
                    try {
                        readDataFromSocket(selectionKey);
                    } catch (IOException e) {
                        selectionKey.cancel();
                        continue;
                    }
                } /*else if (selectionKey.isWritable()) {  //测试写入数据，若写入失败在会自动取消注册该键
                    try {
                        writeDataToSocket(selectionKey);
                    } catch (IOException e) {
                        selectionKey.cancel();
                        continue;
                    }
                }*/
                //必须在处理完通道时自己移除。下次该通道变成就绪时，Selector会再次将其放入已选择键集中。
                //iterator.remove();
            }//. end of while


        }

    }


    /**
     * 发送测试数据包，若失败则认为该socket失效
     *
     * @param sk SelectionKey
     * @throws IOException IOException
     */
    private static void writeDataToSocket(SelectionKey sk) throws IOException {
        SocketChannel sc = (SocketChannel) sk.channel();
        byteBuffer.clear();
        String str = "server data";
        byteBuffer.put(str.getBytes());
        while (byteBuffer.hasRemaining()) {
            sc.write(byteBuffer);
        }
    }

    /**
     * 从通道中读取数据
     *
     * @param sk SelectionKey
     * @throws IOException IOException
     */
    private static void readDataFromSocket(SelectionKey sk) throws IOException {
        SocketChannel sc = (SocketChannel) sk.channel();
        byteBuffer.clear();
        List<Byte> list = new ArrayList<>();
        while (sc.read(byteBuffer) > 0) {
            byteBuffer.flip();
            while (byteBuffer.hasRemaining()) {
                list.add(byteBuffer.get());
            }
            byteBuffer.clear();
        }
        byte[] bytes = new byte[list.size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = list.get(i);
        }
        String s = (new String(bytes)).trim();
        if (!s.isEmpty()) {
            if ("exit".equals(s)) {
                serverSocketChannel.close();
                closed = true;
            }
            System.out.println("服务器收到：" + s);
        }
    }
}
