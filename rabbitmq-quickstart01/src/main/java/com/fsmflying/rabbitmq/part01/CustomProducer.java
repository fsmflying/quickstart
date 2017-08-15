package com.fsmflying.rabbitmq.part01;

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
		if (args.length >= 1) {
			exchangeName = args[0];
		}
		if (args.length >= 2) {
			routingKey = args[1];
		}

		try {
			Produce(exchangeName, routingKey);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	public static void Produce(String exchangeName, String routingKey) throws IOException, TimeoutException {
		Connection connection = Helper.getNewConnection();

		// 创建一个通道
		Channel channel = connection.createChannel();

		// 发送消息到队列中
		String message = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())
				+ "Hello RabbitMQ";
		for (int i = 1; i <= 60; i++) {
			// channel.basicPublish("", QUEUE_NAME, null, (message + " " +
			// i).getBytes("UTF-8"));

			channel.basicPublish(exchangeName, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN,
					("[" + routingKey + "]" + message + " " + i).getBytes("UTF-8"));
			System.out.println("Producer Send +'" + (message + " " + i) + "'");
		}

		// 关闭通道和连接
		channel.close();
		connection.close();
	}
}
