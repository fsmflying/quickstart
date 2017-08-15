package com.fsmflying.rabbitmq.part01;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.fsmflying.rabbitmq.Helper;
import com.fsmflying.rabbitmq.Startup;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class BindQueueToExchange {
	public static void main(String[] args) {
		String queueName = Startup.QUEUE_DEFAULT;
		String exchangeName = "";
		String routingKey = "";
		if (args.length >= 1) {
			queueName = args[0];
		}
		if (args.length >= 2) {
			exchangeName = args[1];
		}
		if (args.length >= 3) {
			routingKey = args[2];
		}

		try {
			bind(queueName, exchangeName, routingKey);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	public static void bind(String queueName, String exchangeName, String routingKey)
			throws IOException, TimeoutException {
		Connection conn = Helper.getNewConnection();
		Channel channel = conn.createChannel();
		channel.queueBind(queueName, exchangeName, routingKey);
		channel.close();
		conn.close();
	}
}
