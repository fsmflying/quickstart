package com.fsmflying.rabbitmq.part01;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.fsmflying.rabbitmq.Helper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class DeleteCustomQueue {
	public static void main(String[] args) {
		String queueName = "";
		if (args.length >= 1 && !args[0].isEmpty() && !args[0].trim().isEmpty()) {
			queueName = args[0];
		} else {
			System.out.println("queueName is not valid !");
			System.out.println("usage: "
					+ DeleteCustomQueue.class.getCanonicalName()
					+ " <queueName> ");
			System.exit(0);
		}
		

		try {
			deleteQueue(queueName);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	public static void deleteQueue(String queueName)
			throws IOException, TimeoutException {
		Connection conn = Helper.getNewConnection();
		Channel channel = conn.createChannel();
		channel.queueDelete(queueName);
		channel.close();
		conn.close();
	}
}
