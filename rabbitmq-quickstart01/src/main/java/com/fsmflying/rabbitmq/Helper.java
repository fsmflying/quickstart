package com.fsmflying.rabbitmq;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Helper {
	private static Properties props = null;
	private static final Object lockForProperties = new Object();
	public static String getPropertyValue(String key) {
		if (props == null) {
			synchronized (lockForProperties) {
				if (props == null) {
					props = new Properties();
					try {
						InputStream input = Helper.class.getClassLoader().getResourceAsStream("default.properties");
						props.load(input);
						input.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return props.getProperty(key);
	}

	private static Connection connection = null;
	private static final Object lockForConnection = new Object();
	public static Connection getConnection() throws IOException, TimeoutException {
		if (connection == null) {
			synchronized (lockForConnection) {
				if (connection == null) {
					ConnectionFactory factory = new ConnectionFactory();
					factory.setHost(Helper.getPropertyValue("rabbit.host"));
					factory.setUsername(Helper.getPropertyValue("rabbit.username"));
					factory.setPassword(Helper.getPropertyValue("rabbit.password"));
				}
			}
		}
		return connection;
	}

	public static void closeConnection() throws IOException {
		if (connection != null) {
			connection.close();
		}
	}

	public static Connection getNewConnection() throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(Helper.getPropertyValue("rabbit.host"));
		factory.setUsername(Helper.getPropertyValue("rabbit.username"));
		factory.setPassword(Helper.getPropertyValue("rabbit.password"));
		return factory.newConnection();
	}

	public static void publishMessage(String exchangeName, String routingKey, String message)
			throws IOException, TimeoutException {
		Connection conn = getNewConnection();
		Channel channel = conn.createChannel();
		channel.basicPublish(exchangeName, routingKey, null, message.getBytes("UTF-8"));
		channel.close();
		conn.close();
	}

	public static void publishMessage(String exchangeName, String routingKey, List<String> messages)
			throws IOException, TimeoutException {
		Connection conn = getNewConnection();
		Channel channel = conn.createChannel();
		for (String message : messages)
			channel.basicPublish(exchangeName, routingKey, null, message.getBytes("UTF-8"));
		channel.close();
		conn.close();
	}

	public static void bind(String queueName, String exchangeName, String routingKey, Map<String, Object> arguments)
			throws IOException, TimeoutException {
		Connection conn = getNewConnection();
		Channel channel = conn.createChannel();
		channel.queueBind(queueName, exchangeName, routingKey, arguments);
		channel.close();
		conn.close();
	}

	public static void deleteExchange(String exchangeName)
			throws IOException, TimeoutException {
		Connection conn = getNewConnection();
		Channel channel = conn.createChannel();
		channel.exchangeDelete(exchangeName);
		channel.close();
		conn.close();
	}

	public static void deleteQueue(String queueName)
			throws IOException, TimeoutException {
		Connection conn = getNewConnection();
		Channel channel = conn.createChannel();
		channel.queueDelete(queueName);
		channel.close();
		conn.close();
	}

	public static void createQueue(String queueName,boolean durable,boolean exclusive,boolean autoDelete,Map<String, Object> arguments)
			throws IOException, TimeoutException {
		Connection conn = getNewConnection();
		Channel channel = conn.createChannel();
		channel.queueDeclare(queueName, durable, exclusive, autoDelete, arguments);
		channel.close();
		conn.close();
	}

	public static void createExchange(String exchangeName,String type,boolean durable)
			throws IOException, TimeoutException {
		Connection conn = getNewConnection();
		Channel channel = conn.createChannel();
		channel.exchangeDeclare(exchangeName, type, durable);
		channel.close();
		conn.close();
	}

}
