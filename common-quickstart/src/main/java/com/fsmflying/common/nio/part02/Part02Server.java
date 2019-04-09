package com.fsmflying.common.nio.part02;


import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

public class Part02Server {

    //客户端
    public void client() throws Exception {
        //1. 获取socketChannel
        SocketChannel sChannel = SocketChannel.open();
        //2. 创建连接
        sChannel.connect(new InetSocketAddress("127.0.0.1", 9898));
        ByteBuffer buf = ByteBuffer.allocate(1024);
        //3. 设置通道为非阻塞
        sChannel.configureBlocking(false);

        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String msg = scanner.nextLine();

            buf.put((new Date() + "：" + msg).getBytes());
            buf.flip();
            //4. 向通道写数据
            sChannel.write(buf);
            buf.clear();
        }
    }

    //客户端
    public void clientSendCsdn() throws Exception {

//        FileInputStream fileInputStream = new FileInputStream("d:\\private\\csdn.24000.txt");
        FileReader fileReader = new FileReader("d:\\private\\csdn.24000.txt");
        //1. 获取socketChannel
        SocketChannel sChannel = SocketChannel.open();
        //2. 创建连接
        sChannel.connect(new InetSocketAddress("127.0.0.1", 9898));
        ByteBuffer buf = ByteBuffer.allocate(1024);
        //3. 设置通道为非阻塞
        sChannel.configureBlocking(false);

        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String msg = scanner.nextLine();

            buf.put((new Date() + "：" + msg).getBytes());
            buf.flip();
            //4. 向通道写数据
            sChannel.write(buf);
            buf.clear();
        }
    }

    //服务端
    public void server() throws Exception {
        //1. 获取服务端通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(9898));
        //2. 设置为非阻塞模式
        serverSocketChannel.configureBlocking(false);

        //3. 打开一个监听器
        Selector selector = Selector.open();
        //4. 向监听器注册接收事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (selector.select() > 0) {
            //5. 获取监听器上所有的监听事件值
            Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
            //6. 如果有值
            while (selectionKeyIterator.hasNext()) {
                //7. 取到SelectionKey
                SelectionKey selectionKey = selectionKeyIterator.next();
                selectionKeyIterator.remove();
//                System.out.println("isConnectable:" + selectionKey.isConnectable());
//                System.out.println("isAcceptable:" + selectionKey.isAcceptable());
//                System.out.println("isReadable:" + selectionKey.isReadable());
//                System.out.println("isWritable:" + selectionKey.isWritable());
                //8. 根据key值判断对应的事件
                if (selectionKey.isAcceptable()) {
                    //9. 接入处理
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (selectionKey.isReadable()) {
                    //10. 可读事件处理
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    readMsg(channel);
                }
                //11. 移除当前key
                //selectionKeyIterator.remove();
            }
        }
    }

    private void readMsg(SocketChannel channel) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(1024);
        int len = 0;
        while ((len = channel.read(buf)) > 0) {
            buf.flip();
            byte[] bytes = new byte[1024];
            buf.get(bytes, 0, len);
            System.out.println(new String(bytes, 0, len));
        }
    }


}
