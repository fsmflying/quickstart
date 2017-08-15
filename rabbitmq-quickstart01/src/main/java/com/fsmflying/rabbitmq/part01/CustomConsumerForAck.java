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

public class CustomConsumerForAck {

	public static void main(String[] args) {
		String queueName = Startup.QUEUE_DEFAULT;
		boolean durable = false;
		for (String arg : args) {
			if ("--help".equals(arg) || "-h".equals(arg)) {
				System.out.println("" + CustomConsumerForAck.class.getCanonicalName() + " <queueName> <durable>");
				System.exit(0);
				break;
			}
		}
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

	public static void Consume(final String queueName, boolean durable) throws IOException, TimeoutException {
		Connection connection = Helper.getNewConnection();
		final Channel channel = connection.createChannel();
		channel.queueDeclare(queueName, durable, false, false, null);
		System.out.println("Customer Waiting Received messages from queue[" + queueName + "]");

		// 每次获取一条消息
		int prefetchcount = 1;
		channel.basicQos(prefetchcount);

		// DefaultConsumer类实现了Consumer接口，通过传入一个频道，
		// 告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
		final Consumer consumer = new DefaultConsumer(channel) {
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println("Customer Received  '" + message + "'");
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					// 处理成功后,消息确认
					channel.basicAck(envelope.getDeliveryTag(), false);

				}
			}
		};
		// 自动回复队列应答 -- RabbitMQ中的消息确认机制
		channel.basicConsume(queueName, false, consumer);
	}
}
