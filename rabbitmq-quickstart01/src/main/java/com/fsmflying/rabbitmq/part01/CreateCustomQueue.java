package com.fsmflying.rabbitmq.part01;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import com.fsmflying.rabbitmq.Helper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class CreateCustomQueue {
	public static void main(String[] args) {
		String queueName = "";
		boolean durable = false;
		if (args.length >= 1 && !args[0].isEmpty() && !args[0].trim().isEmpty()) {
			queueName = args[0];
		} else {
			System.out.println("exchangeName is not valid !");
			System.out.println("usage: " + CreateCustomQueue.class.getCanonicalName()
					+ " <exchangeName> <direct|fanout|topic|headers> <durable>");
			System.exit(0);
		}
		if (args.length >= 2 && args[1].equalsIgnoreCase("true")) {
			durable = true;
		}

		try {
			createQueue(queueName, durable);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	public static void createQueue(String queueName, boolean durable) throws IOException, TimeoutException {
		Connection conn = Helper.getNewConnection();
		Channel channel = conn.createChannel();
		boolean exclusive = false; // 是否声明一个排它队列，
		boolean autoDelete = false; // 是否声明一个自动删除队列，服务器将在不使用它后，自动删除队列
		// Map<String, Object> arguments = new HashMap<String, Object>();
		Map<String, Object> arguments = null;

		channel.queueDeclare(queueName, durable, exclusive, autoDelete, arguments);

		channel.close();
		conn.close();
	}
}
