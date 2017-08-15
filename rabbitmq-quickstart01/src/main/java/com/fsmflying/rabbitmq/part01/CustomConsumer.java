package com.fsmflying.rabbitmq.part01;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.fsmflying.rabbitmq.Helper;
import com.fsmflying.rabbitmq.Startup;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class CustomConsumer {

	public static void main(String[] args) {
		String queueName = Startup.QUEUE_DEFAULT;
		boolean durable = false;
		if (args.length >= 1) {
			queueName = args[0];
		}
		if (args.length >= 2) {
			if (args[1].equalsIgnoreCase("true"))
				durable = true;
		}
		try {
			Consume(queueName, durable);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	public static void Consume(String queueName, boolean durable) throws IOException, TimeoutException {

		Connection connection = Helper.getNewConnection();
		// 创建一个通道
		Channel channel = connection.createChannel();

		// 声明要关注的队列
		channel.queueDeclare(queueName, durable, false, false, null);
		System.out.println("Customer Waiting Received messages from [" + queueName + "]");

		// DefaultConsumer类实现了Consumer接口，通过传入一个频道，
		// 告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
		Consumer consumer = new DefaultConsumer(channel) {
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println("Customer Received '" + message + "'");
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		// 自动回复队列应答 -- RabbitMQ中的消息确认机制
		channel.basicConsume(queueName, true, consumer);
	}
}
