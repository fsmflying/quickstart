package com.fsmflying.netty.part01;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

//import java.nio.channels.SocketChannel;

//import io.netty.channel.socket.SocketChannel;

@SuppressWarnings("all")
public class DiscardClient03 {
    public static final String HOSTNAME = "localhost";
    public static final int PORT = 8007;
    public static final int NM = 10;

//	public static void main(String[] args) throws InterruptedException, IOException {
//		FMSyncFileReader reader = new FMSyncFileReader("d:\\private\\csdn.1000.txt", 1);
//		ExecutorService es01 = Executors.newCachedThreadPool();
//		for (int i = 0; i < NM; i++) {
//			String data = reader.readWithFileName().get("data").get(0);
//			// System.out.println("");
//			es01.submit(new MySendThread(data));
//		}
//		es01.shutdown();
//		while (true) {
//			System.out.println("=====check======");
//			// if (es01.isShutdown()) {
//			// System.out.println("===============isShutdown===========================");
//			// break;
//			// }
//			if (es01.isTerminated()) {
//				System.out.println("===============isTerminated===========================");
//				if (reader.getReader() != null)
//					try {
//						reader.getReader().close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				break;
//			}
//			Thread.sleep(100 * 5);
//		}
//	}

    private static class MySendThread implements Runnable {
        String data;

        public MySendThread(String data) {
            this.data = data;
        }

        @Override
        public void run() {
            SocketChannel ch = null;
            try {

                ch = SocketChannel.open();
                ch.configureBlocking(false);
                ch.connect(new InetSocketAddress(HOSTNAME, PORT));

                while (true) {
                    if (ch.isConnected()) {
                        ByteBuffer buf = ByteBuffer.allocate(1024);
                        buf.clear();
                        buf.put(this.data.getBytes());
                        buf.flip();
                        while (buf.hasRemaining())
                            ch.write(buf);
                        //ch.finishConnect();
                        Thread.sleep(10000);
                        ch.close();
                        break;
                    } else {
                        Thread.sleep(100);
                    }
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (ch != null && ch.isConnected()) {
                        ch.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
