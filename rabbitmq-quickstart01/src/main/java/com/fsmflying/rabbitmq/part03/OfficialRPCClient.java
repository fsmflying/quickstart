package com.fsmflying.rabbitmq.part03;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.fsmflying.rabbitmq.Helper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

/**
 * 官方提供的RPC示例的客户端
 * @author FangMing
 *
 */
public class OfficialRPCClient {

	private Connection connection;
	private Channel channel;
	private String requestQueueName = "rpc_queue";
	private String replyQueueName;

	public OfficialRPCClient() throws IOException, TimeoutException {
		connection = Helper.getNewConnection();
		channel = connection.createChannel();
		replyQueueName = channel.queueDeclare().getQueue();
	}

	public String call(String message) throws IOException, InterruptedException {
		final String corrId = UUID.randomUUID().toString();

		AMQP.BasicProperties props = new AMQP.BasicProperties//
				.Builder()//
				.correlationId(corrId)//
				.replyTo(replyQueueName)//
				.build();
		channel.basicPublish("", requestQueueName, props, message.getBytes("UTF-8"));
		final BlockingQueue<String> response = new ArrayBlockingQueue<String>(1);//使用了一个阻塞队列
		channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				if (properties.getCorrelationId().equals(corrId)) {
					response.offer(new String(body, "UTF-8"));
				}
			}
		});

		return response.take();
	}

	public void close() throws IOException {
		connection.close();
	}

	public static void main(String[] args) {
		String message = "30";
		if (args.length >= 1 && args[0].matches("0|([1-9]\\d*)")) {
			message = args[0];
			if (Integer.parseInt(args[0]) >= 40) {
				message = "" + Integer.parseInt(args[0]) % 40;
			}
		}

		
		OfficialRPCClient fibonacciRpc = null;
		String response = null;
		try {
			fibonacciRpc = new OfficialRPCClient();

			System.out.println(" [x] Requesting fib(30)");
			response = fibonacciRpc.call(message);
			System.out.println(" [.] Got '" + response + "'");
		} catch (IOException | TimeoutException | InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (fibonacciRpc != null) {
				try {
					fibonacciRpc.close();
				} catch (IOException _ignore) {
				}
			}
		}
	}
}
