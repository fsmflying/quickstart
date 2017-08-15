package com.fsmflying.netty.part05;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.fsmflying.common.thread.FMSyncFileReader;

public class CustomClient {
	public static final String HOSTNAME = "localhost";
	public static final int PORT = 8007;
	public static final int NM = 20;

	public static void main(String[] args) throws InterruptedException, IOException {
		FMSyncFileReader reader = new FMSyncFileReader("d:\\private\\csdn.1000.txt", "UTF-8", NM);
		List<String> lines = reader.readWithFileName().get("data");
		ExecutorService es01 = Executors.newFixedThreadPool(NM);
		for (int i = 0; (i < lines.size()) && (i < NM); i++) {
			es01.submit(new MySendThread(lines.get(i), i));
		}
		es01.shutdown();
		while (true) {
			System.out.println("=====check======");
			if (es01.isTerminated()) {
				System.out.println("===============isTerminated===========================");
				if (reader.getReader() != null)
					try {
						reader.getReader().close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				break;
			}
			Thread.sleep(100 * 5);
		}
	}

	private static class MySendThread implements Runnable {
		String data;
		int seq;

		public MySendThread(String data) {
			this.data = data;
		}

		public MySendThread(String data, int seq) {
			this.data = data;
			this.seq = seq;
		}

		@Override
		public void run() {
			Socket socket = null;
			InputStream input = null;
			OutputStream output = null;
			try {
				socket = new Socket(HOSTNAME, PORT);
				socket.setSoTimeout(3000);
				output = socket.getOutputStream();

				System.out.println("[send data][" + this.seq + "]:" + this.data);
				byte[] bytesForSend = this.data.getBytes();

				ByteBuffer seqBuf = ByteBuffer.allocate(4);
				seqBuf.putInt(this.seq);
				output.write(seqBuf.array());// 写入序号

				ByteBuffer header = ByteBuffer.allocate(4);
				header.putInt(bytesForSend.length);
				output.write(header.array());// 写入长度

				output.write(bytesForSend);// 写入内容
				output.flush();

//				input = socket.getInputStream();
//				byte[] bytes = new byte[1024];
//				input.read(bytes);
//				
//				System.out.println("[client][receive data]:" + new String(bytes));
				

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
