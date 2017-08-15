package com.fsmflying.rabbitmq.part02;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Startup {
	public static final String QUEUE_NAME = "testQueue";

	public static void main(String[] args) {

	}

	public static void Consume() throws IOException, TimeoutException {
		// 创建连接工厂
		ConnectionFactory factory = new ConnectionFactory();

		// 设置RabbitMQ地址
		factory.setHost("192.168.1.104");
		factory.setUsername("rabbitadmin");
		factory.setPassword("fangming");
		factory.setPort(5672);

		// 创建一个新的连接
		Connection connection = factory.newConnection();

		// 创建一个通道
		Channel channel = connection.createChannel();

		// 声明要关注的队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		System.out.println("Customer Waiting Received messages");

		// DefaultConsumer类实现了Consumer接口，通过传入一个频道，
		// 告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
		Consumer consumer = new DefaultConsumer(channel) {
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println("Customer Received '" + message + "'");
			}
		};
		// 自动回复队列应答 -- RabbitMQ中的消息确认机制
		channel.basicConsume(QUEUE_NAME, true, consumer);
	}
}
