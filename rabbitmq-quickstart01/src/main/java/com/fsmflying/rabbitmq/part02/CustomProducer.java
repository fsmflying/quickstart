package com.fsmflying.rabbitmq.part02;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.TimeoutException;

import com.fsmflying.rabbitmq.Helper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

public class CustomProducer {

	public static void main(String[] args) {
		String exchangeName = "testExchange";
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

		try {
			Produce(exchangeName, routingKey, prefix, suffix);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	public static void Produce(String exchangeName, String routingKey, String prefix, String suffix)
			throws IOException, TimeoutException {
		Connection connection = Helper.getNewConnection();
		Channel channel = connection.createChannel();
		final String[] routingKeys = new String[] { "DEBUG", "INFO", "WARN", "ERROR", "FATAL" /**/ };
		// 发送消息到队列中
		String message = "["
				+ new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())
				+ "]:Hello RabbitMQ";
		for (int i = 1; i <= 60; i++) {
			// channel.basicPublish("", QUEUE_NAME, null, (message + " " +
			// i).getBytes("UTF-8"));
			String localRoutingKey = routingKeys[(int) (Math.round(Math.random() * 1000)) % (routingKeys.length)];
			if (prefix != null && !prefix.isEmpty()) {
				localRoutingKey = prefix + "." + localRoutingKey;
			}
			if (suffix != null && !suffix.isEmpty()) {
				localRoutingKey = localRoutingKey + "." + suffix;
			}
			channel.basicPublish(exchangeName, localRoutingKey, MessageProperties.PERSISTENT_TEXT_PLAIN,
					("[" + localRoutingKey + "]" + message + " " + i).getBytes("UTF-8"));
			System.out.println("[" + localRoutingKey + "]:Producer Send +'" + (message + " " + i) + "'");
		}

		// 关闭通道和连接
		channel.close();
		connection.close();
	}
}
