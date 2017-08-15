package com.fsmflying.rabbitmq.part02;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import com.fsmflying.common.thread.FMSyncFileReader;
import com.fsmflying.common.thread.FMSyncReader;
import com.fsmflying.rabbitmq.Helper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class MultiThreadCustomProducer {

	public static void main(String[] args) {
		String exchangeName = "testExchange02";
		String routingKey = "INFO";
		String prefix = "";
		String suffix = "";
		if (args.length >= 1) {
			exchangeName = args[0];
		}
		if (args.length >= 2) {
			routingKey = args[1];
		}
		if (args.length >= 3) {
			prefix = args[2];
		}
		if (args.length >= 4) {
			suffix = args[3];
		}

		// if (exchangeName != null && !exchangeName.isEmpty() &&
		// !exchangeName.trim().isEmpty())
		// queueName = "";

		try {
			Produce(exchangeName, routingKey, prefix, suffix);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void Produce(final String exchangeName, final String routingKey, final String prefix,
			final String suffix) throws IOException, TimeoutException, InterruptedException {
		Connection connection = Helper.getNewConnection();
		final Channel channel = connection.createChannel();
		InputStream input = MultiThreadCustomProducer.class.getClassLoader().getResourceAsStream("data.1000.txt");
		final FMSyncReader reader = new FMSyncFileReader(input, "data.1000", ".txt", 1000);

		// 发送消息到队列中
		// String message = "Hello RabbitMQ";
		ExecutorService es01 = Executors.newCachedThreadPool();
		final int NM = 20;
		final String[] routingKeys = new String[] { "DEBUG", "INFO", "WARN", "ERROR", "FATAL" };
		final int ROWS = 1000;
		long start = System.currentTimeMillis();
		for (int i = 0; i < NM; i++) {
			es01.submit(new Runnable() {

				@Override
				public void run() {
					List<String> messages;
					try {
						String localRoutingKey = routingKeys[(int) (Math.round(Math.random() * 1000))
								% (routingKeys.length)];
						if (prefix != null && !prefix.isEmpty()) {
							localRoutingKey = prefix + "." + localRoutingKey;
						}
						if (suffix != null && !suffix.isEmpty()) {
							localRoutingKey = localRoutingKey + "." + suffix;
						}
						messages = reader.readWithFileName(ROWS / NM).get("data");
						for (String m : messages) {
							channel.basicPublish(exchangeName, localRoutingKey, null, m.getBytes("UTF-8"));
							System.out.println("[" + localRoutingKey + "]:Producer Send +'" + m + "'");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			});
		}
		es01.shutdown();

		while (true) {
			if (es01.isTerminated()) {
				System.out.println("--------finished------------------------");
				long end = System.currentTimeMillis();
				System.out.println("总耗时" + (end - start) + "毫秒!");
				reader.close();
				// 关闭通道和连接
				channel.close();
				connection.close();
				break;
			} else {
				Thread.sleep(200);
				continue;

			}
		}
	}
}
