package com.fsmflying.rabbitmq.part03;

import com.fsmflying.rabbitmq.Helper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;

public class CustomRpcClient {
	public static final String RPC_QUEUE_NAME = "rpc_queue";

	public static void main(String[] args) {
		String message = "30";

		if (args.length >= 1 && args[0].matches("0|([1-9]\\d*)")) {
			message = args[0];
			if (Integer.parseInt(args[0]) >= 40) {
				message = "" + Integer.parseInt(args[0]) % 40;
			}
		}
		Connection connection = null;
		Channel channel = null;
		try {
			connection = Helper.getNewConnection();
			channel = connection.createChannel();

			final String corrId = UUID.randomUUID().toString();
			String callbackQueueName = channel.queueDeclare().getQueue();
			BasicProperties props = new BasicProperties.Builder().correlationId(corrId).replyTo(callbackQueueName)
					.build();
			channel.basicPublish("", RPC_QUEUE_NAME, props, message.getBytes());
			final BlockingQueue<String> response = new ArrayBlockingQueue<String>(1);
			channel.basicConsume(callbackQueueName, true, new DefaultConsumer(channel) {

				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
						byte[] body) throws IOException {
					// super.handleDelivery(consumerTag, envelope, properties,
					// body);
					if (properties.getCorrelationId().equals(corrId)) {
						String result = new String(body, "UTF-8");
						// System.out.println(result);
						// System.exit(0);
						response.offer(result);
					}
				}

			});

			System.out.println("fib(" + message + ")=" + response.take());

			// channel.close();
			// connection.close();
		} catch (IOException | TimeoutException | InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				if (channel != null)
					channel.close();
				if (connection != null)
					connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
		}

	}
}
