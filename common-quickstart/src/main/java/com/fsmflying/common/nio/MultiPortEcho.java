package com.fsmflying.common.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiPortEcho {

	Logger logger = LoggerFactory.getLogger(MultiPortEcho.class);
	private int ports[];
	private ByteBuffer echoBuffer = ByteBuffer.allocate(1024);

	public MultiPortEcho(int ports[]) throws IOException {
		this.ports = ports;
		go();
	}

	public static void main(String args[]) throws Exception {
		// if (args.length <= 0) {
		// System.err.println("Usage: java MultiPortEcho port [port port …]");
		// System.exit(1);
		// }
		// int ports[] = new int[args.length];

		int ports[] = new int[] { 8081 };

		for (int i = 0; i < args.length; ++i) {
			ports[i] = Integer.parseInt(args[i]);
		}

		new MultiPortEcho(ports);
	}

	private void go() throws IOException {
		// Create a new selector
		Selector selector = Selector.open();

		// Open a listener on each port, and register each one
		// with the selector
		for (int i = 0; i < ports.length; ++i) {
			ServerSocketChannel ssc = ServerSocketChannel.open();
			ssc.configureBlocking(false);
			ServerSocket ss = ssc.socket();
			InetSocketAddress address = new InetSocketAddress(ports[i]);
			ss.bind(address);

			SelectionKey key = ssc.register(selector, SelectionKey.OP_ACCEPT);

			// System.out.println("Going to listen on " + ports[i]);
			logger.info("Going to listen on " + ports[i]);
		}

		while (true) {// 10
			int num = selector.select();
			logger.info("selector.select():" + num);

			Set<SelectionKey> selectedKeys = selector.selectedKeys();

			Iterator<SelectionKey> it = selectedKeys.iterator();

			while (it.hasNext()) {// 11
				SelectionKey key = (SelectionKey) it.next();
				if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {// 12
					logger.info("-----------------------------------------------------------------------");
					logger.info("[server]:OP_ACCEPT");
					// Accept the new connection
					ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
					SocketChannel sc = ssc.accept();
					sc.configureBlocking(false);

					while (sc != null) {//13
						
					}//13

					// Add the new connection to the selector
					SelectionKey newKey = sc.register(selector, SelectionKey.OP_READ);

					// sc.close();
					// ssc.close();

					// System.out.println("Got connection from " + sc);
					logger.info("Got connection from " + sc);
				} else if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {// 12
					logger.info("-----------------------------------------------------------------------");
					// System.out.println("[server]:OP_READ");
					logger.info("[server]:OP_READ");
					// Read the data
					SocketChannel sc = (SocketChannel) key.channel();
					sc.configureBlocking(false);

					// Echo data
					int bytesEchoed = 0;
					System.out.println();

					while (sc.isConnected()) {// 13
						echoBuffer.clear();
						int r = sc.read(echoBuffer);
						if (r <= 0) {
							break;
						}
						echoBuffer.flip();

						// echoBuffer.mark();
						// byte[] bytes = new byte[1024];
						// while (echoBuffer.hasRemaining())
						// echoBuffer.get(bytes);
						// logger.info("[receive data]:" + new String(bytes));
						// echoBuffer.reset();

						logger.info(echoBuffer.toString());
						sc.write(echoBuffer);// 把发送过来的数据再发送回去
						bytesEchoed += r;
					} // 13
						// sc.finishConnect();
						// sc.close();
					logger.info("Echoed " + bytesEchoed + " from " + sc);
				} // 12

				// it.remove();
				selector.selectedKeys().clear();

				// selector.selectedKeys().remove(it);
				// selector.selectedKeys().clear();

				// selectedKeys.remove(it);
			} // 11

			// System.out.println("going to clear");
			logger.info("going to clear");
			// selectedKeys.clear();
			// System.out.println("cleared");
			logger.info("cleared");
		} // 10
	}
}
