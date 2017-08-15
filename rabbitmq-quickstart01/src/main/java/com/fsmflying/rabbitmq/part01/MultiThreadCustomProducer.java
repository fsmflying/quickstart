package com.fsmflying.rabbitmq.part01;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import com.fsmflying.common.thread.FMSyncFileReader;
import com.fsmflying.common.thread.FMSyncReader;
import com.fsmflying.rabbitmq.Helper;
import com.fsmflying.rabbitmq.Startup;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class MultiThreadCustomProducer {

	public static void main(String[] args) {
		String queueName = Startup.QUEUE_DEFAULT;
		boolean durable = false;
		String exchangeName = "";
		String routingKey = "";
		if (args.length >= 1) {
			queueName = args[0];
		}
		if (args.length >= 2) {
			if (args[1].equalsIgnoreCase("true"))
				durable = true;
		}
		if (args.length >= 3) {
			exchangeName = args[2];
		}
		if (args.length >= 4) {
			routingKey = args[3];
		}

		if (exchangeName != null && !exchangeName.isEmpty() && !exchangeName.trim().isEmpty())
			queueName = "";

		try {
			Produce(queueName, durable, exchangeName,routingKey);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void Produce(String queueName, boolean durable, final String exchangeName, final String routingKey)
			throws IOException, TimeoutException, InterruptedException {

		Connection connection = Helper.getNewConnection();
		final Channel channel = connection.createChannel();
		channel.queueDeclare(queueName, durable, false, false, null);
		InputStream input = MultiThreadCustomProducer.class.getClassLoader().getResourceAsStream("data.100000.2.txt");
		final FMSyncReader reader = new FMSyncFileReader(input, "data.100000.2", ".txt", 1000);

		// 发送消息到队列中
		// String message = "Hello RabbitMQ";
		ExecutorService es01 = Executors.newCachedThreadPool();
		final int NM = 200;
		final int ROWS = 100000;
		long start = System.currentTimeMillis();
		for (int i = 0; i < NM; i++) {
			es01.submit(new Runnable() {

				@Override
				public void run() {
					List<String> messages;
					try {
						messages = reader.readWithFileName(ROWS / NM).get("data");
						for (String m : messages) {
							channel.basicPublish(exchangeName, routingKey, null, m.getBytes("UTF-8"));
							System.out.println("Producer Send +'" + m + "'");
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
