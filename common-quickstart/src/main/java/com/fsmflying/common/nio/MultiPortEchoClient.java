package com.fsmflying.common.nio;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fsmflying.common.thread.FMFileSplitHandler;
import com.fsmflying.common.thread.FMSyncFileReader;

public class MultiPortEchoClient {
	public static final String filename = "d:\\private\\csdn.10.txt";
	public static final int NUM = 1;

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {

		// Runtime.getRuntime().
		FileInputStream fis01 = new FileInputStream(filename);

		// 计算原始文件行数
		BufferedInputStream bis01 = new BufferedInputStream(fis01);
		long rowCount = 0;
		byte[] c = new byte[1024 * 1024];
		int readChars = 0;
		long start = System.currentTimeMillis();
		while ((readChars = bis01.read(c)) != -1) {
			for (int i = 0; i < readChars; ++i) {
				if (c[i] == '\n') {
					++rowCount;
				}
			}
		}
		bis01.close();
		long end = System.currentTimeMillis();
		System.out.println("文件总行数：" + rowCount);
		System.out.println("总花费时间(毫秒)：" + (end - start));

		// 计算拆分文件的行数，即每次应该在原始文件中读取的行数
		long pageSize = rowCount / NUM + 1;
		System.out.println("PageSize:" + pageSize);

		Reader reader01 = new InputStreamReader(new FileInputStream(filename));
		BufferedReader reader03 = new BufferedReader(reader01);
		FMSyncFileReader reader04 = new FMSyncFileReader(reader03, filename);

		ExecutorService es01 = Executors.newCachedThreadPool();
		for (int i = 0; i < NUM; i++) {
			es01.submit(new SendThread((int) pageSize, reader04, "" + i, "localhost", 8081 + i));
		}
		es01.shutdown();
		while (true) {
			System.out.println("=====check======");
			// if (es01.isShutdown()) {
			// System.out.println("===============isShutdown===========================");
			// break;
			// }
			if (es01.isTerminated()) {
				System.out.println("===============isTerminated===========================");
				if (reader01 != null)
					try {
						reader01.close();
						fis01.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				break;
			}
			Thread.sleep(100 * 5);
		}
	}

	static class SendThread extends FMFileSplitHandler {

		public SendThread(int handleRowCount, FMSyncFileReader reader, String name, String hostname, int port) {
			super(handleRowCount, reader);
			this.name = name;
			this.hostname = hostname;
			this.port = port;
		}

		String hostname;
		int port;
		String name;

		@Override
		public void run() {
			if (this.reader != null) {
				Socket socket = null;
				OutputStream output = null;
				InputStream input = null;
				byte[] buf = new byte[1024];
				try {
					socket = new Socket(hostname, port);
					output = socket.getOutputStream();
					input = socket.getInputStream();
					int numOfRead = 0;

					List<String> list = this.reader.read(handleRowCount);
					while (list != null && list.size() != 0) {
						for (String s : list) {
							output.write(s.getBytes());
							output.flush();
							numOfRead = input.read(buf, 0, buf.length);
							System.out.println(
									"[client][" + name + "][data from server][" + Thread.currentThread().getName() + "]"
											+ new String(Arrays.copyOf(buf, numOfRead)));
							Thread.sleep(10);
						}
						list = this.reader.read((int) this.handleRowCount);
						input.close();
						output.close();
						socket.close();
						
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {

					// try {
					// if (socket != null && socket.getKeepAlive()) {
					// if (input != null)
					// input.close();
					// if (output != null)
					// output.close();
					// socket.close();
					// }
					// } catch (IOException e) {
					// e.printStackTrace();
					// }
				}
			}

		}
	}

	static class SendThreadByNio extends FMFileSplitHandler {

		public SendThreadByNio(int handleRowCount, FMSyncFileReader reader, String name, String hostname, int port) {
			super(handleRowCount, reader);
			this.name = name;
			this.hostname = hostname;
			this.port = port;
		}

		String hostname;
		int port;
		String name;

		@Override
		public void run() {
			if (this.reader != null) {
				SocketChannel sc;
				ByteBuffer buf = ByteBuffer.allocate(1024);
				try {
					sc = SocketChannel.open(new InetSocketAddress(hostname, port));
					sc.configureBlocking(false);

					// sc.connect(new InetSocketAddress(hostname, port));

					int numOfRead = 0;
					List<String> list = this.reader.read(handleRowCount);
					while (list != null && list.size() != 0 && sc.finishConnect()) {
						for (String s : list) {
							// numOfRead = buf.
							sc.write(buf);
							Thread.sleep(100);
						}
						list = this.reader.read((int) this.handleRowCount);
					}
					// input.close();
					// output.close();
					// socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {

					// try {
					// if (output != null)
					// input.close();
					// if (output != null)
					// input.close();
					// } catch (IOException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
				}
			}

		}
	}
}
