package com.fsmflying.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioServer implements Runnable {
    public static void main(String[] args) {
            new Thread(new NioServer()).start();
    }

    ServerSocketChannel serverSocketChannel = null;
    Selector selector = null;

    public NioServer() {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(8888));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocketChannel != null) {
                try {
                    serverSocketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                //轮询所有已注册的通道
                selector.select();
                Iterator<SelectionKey> selectionKeys = selector.keys().iterator();
                while (selectionKeys.hasNext()) {
                    SelectionKey selectionKey = selectionKeys.next();
                    if (selectionKey.isValid()) {

                        selectionKeys.remove();
                        if (selectionKey.isAcceptable()) {
                            //客户端已经进入就绪状态
                            accept(selectionKey);
                        }
                        if (selectionKey.isConnectable()) {
                            //客户端
                        }
                        if (selectionKey.isReadable()) {
                            //
                            read(selectionKey);
                        }
                        if (selectionKey.isWritable()) {
                            //
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void accept(SelectionKey selectionKey) {
        //有客户端接入
        ServerSocketChannel innerServerSocketChannel = (ServerSocketChannel) selectionKey.channel();
        try {
            SocketChannel socketChannel = innerServerSocketChannel.accept();
            //客户端已经接入
            innerServerSocketChannel.configureBlocking(false);
            innerServerSocketChannel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ByteBuffer buffer = ByteBuffer.allocate(1024);
    private void read(SelectionKey selectionKey) {
        try {
            //1:
            buffer.clear();

            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            int count = socketChannel.read(buffer);
            if (count == -1) {
                selectionKey.channel().close();
                selectionKey.cancel();
                return;
            }
            buffer.flip();
            byte[] bytes = new byte[count];
            buffer.get(bytes);
            String message = new String(bytes);
            System.out.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
