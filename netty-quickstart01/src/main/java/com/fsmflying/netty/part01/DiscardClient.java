package com.fsmflying.netty.part01;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

@SuppressWarnings("all")
public class DiscardClient {
    public static final String HOSTNAME = "localhost";
    public static final int PORT = 8007;
    public static final int NM = 1000;

//	public static void main(String[] args) throws InterruptedException, IOException {
//		FMSyncFileReader reader = new FMSyncFileReader("d:\\private\\csdn.1000.txt", 1);
//		List<String> lines = reader.readWithFileName().get("data");
//		ExecutorService es01 = Executors.newCachedThreadPool();
//		for (int i = 0; i < lines.size() && i < NM; i++) {
//			es01.submit(new MySendThread(lines.get(i)));
//		}
//		es01.shutdown();
//		while (true) {
//			System.out.println("=====check======");
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
            Socket socket = null;
            InputStream input = null;
            OutputStream output = null;
            try {
                socket = new Socket(HOSTNAME, PORT);
                socket.setSoTimeout(3000);
                input = socket.getInputStream();
                output = socket.getOutputStream();
                byte[] bytesForSend = this.data.getBytes();
                output.write(bytesForSend.length);
                output.write(bytesForSend);
                output.flush();
                byte[] bytes = new byte[1024];
                input.read(bytes);
                System.out.println("[client][receive data]:" + new String(bytes));

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (socket != null && socket.getKeepAlive()) {
                        if (input != null)
                            input.close();
                        if (output != null)
                            output.close();
                        socket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
