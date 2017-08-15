package com.fsmflying.rabbitmq;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import com.fsmflying.rabbitmq.part01.BindQueueToExchange;
import com.fsmflying.rabbitmq.part01.CreateCustomExchange;
import com.fsmflying.rabbitmq.part01.CreateCustomQueue;
import com.fsmflying.rabbitmq.part01.CustomConsumer;
import com.fsmflying.rabbitmq.part01.CustomConsumerForAck;
import com.fsmflying.rabbitmq.part01.CustomProducer;
import com.fsmflying.rabbitmq.part01.DeleteCustomExchange;
import com.fsmflying.rabbitmq.part01.DeleteCustomQueue;
import com.fsmflying.rabbitmq.part01.MultiThreadCustomProducer;
import com.fsmflying.rabbitmq.part03.CustomRpcClient;
import com.fsmflying.rabbitmq.part03.CustomRpcServer;
import com.fsmflying.rabbitmq.part03.RPCClient;
import com.fsmflying.rabbitmq.part03.RPCServer;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Startup {

	public static final String QUEUE_NAME = "testQueue";

	public static final String QUEUE_01 = "testQueue";

	public static final String QUEUE_02 = "testQueue02";

	public static final String QUEUE_03 = "testQueue03";

	public static final String QUEUE_DEFAULT = "testQueue";

	public static final boolean QUEUE_PERSISTENCE = true;

	public static void main(String[] args) {
		System.out.println(Helper.getPropertyValue("rabbit.host"));
		System.out.println(Helper.getPropertyValue("rabbit.username"));
		System.out.println(Helper.getPropertyValue("rabbit.password"));
		for (String arg : args) {
			if ("--help".equals(arg) || "-h".equals(arg)) {
				System.out.println("usage: ");
				// 消费消息
				System.out.println("" + CustomConsumer.class.getCanonicalName() + " <queueName> <durable>");
				System.out.println("" + CustomConsumerForAck.class.getCanonicalName() + " <queueName> <durable>");

				// 生产消息
				System.out.println("" + CustomProducer.class.getCanonicalName() + " <exchangeName> <routingKey>");
				System.out.println(
						"" + MultiThreadCustomProducer.class.getCanonicalName() + " <exchangeName> <routingKey>");
				// 生产消息
				System.out.println("" + com.fsmflying.rabbitmq.part02.CustomProducer.class.getCanonicalName()
						+ " <exchangeName> <routingKey> <prefix> <suffix>");
				System.out.println("" + com.fsmflying.rabbitmq.part02.MultiThreadCustomProducer.class.getCanonicalName()
						+ " <exchangeName> <routingKey> <prefix> <suffix>");

				// 创建exchange
				System.out.println("" + CreateCustomExchange.class.getCanonicalName()
						+ " <exchangeName> <direct|fanout|topic|headers> <durable>");
				System.out.println("" + DeleteCustomExchange.class.getCanonicalName() + " <queueName> <durable>");
				// 创建queue
				System.out.println("" + CreateCustomQueue.class.getCanonicalName()
						+ " <exchangeName> <direct|fanout|topic|headers> <durable>");
				System.out.println("" + DeleteCustomQueue.class.getCanonicalName() + " <queueName>");
				// 绑定queue到exchange
				System.out.println(
						"" + BindQueueToExchange.class.getCanonicalName() + " <queueName> <exchangeName> <routingKey>");

				// Rpc客户端
				System.out.println("" + CustomRpcClient.class.getCanonicalName() + " ");
				// Rpc服务端
				System.out.println("" + CustomRpcServer.class.getCanonicalName() + " ");

				// Rpc客户端
				System.out.println("" + RPCClient.class.getCanonicalName() + " ");
				// Rpc服务端
				System.out.println("" + RPCServer.class.getCanonicalName() + " ");

				System.exit(0);
				break;
			}
		}
		// try {
		// Produce();
		// } catch (IOException e) {
		// e.printStackTrace();
		// } catch (TimeoutException e) {
		// e.printStackTrace();
		// }
	}


	public static void Produce() throws IOException, TimeoutException {
		// 创建连接工厂
		ConnectionFactory factory = new ConnectionFactory();

		// 设置RabbitMQ相关信息
		factory.setHost(Helper.getPropertyValue("rabbit.host"));
		factory.setUsername(Helper.getPropertyValue("rabbit.username"));
		factory.setPassword(Helper.getPropertyValue("rabbit.password"));
		factory.setPort(5672);

		// 创建一个新的连接
		Connection connection = factory.newConnection();

		// 创建一个通道
		Channel channel = connection.createChannel();

		// 声明一个队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		// 发送消息到队列中
		String message = "Hello RabbitMQ";
		channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
		System.out.println("Producer Send +'" + message + "'");

		// 关闭通道和连接
		channel.close();
		connection.close();
	}
}
