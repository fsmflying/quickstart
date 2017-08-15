package com.fsmflying.rabbitmq.part03;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.fsmflying.rabbitmq.Helper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class CustomRpcServer {
	public static final String RPC_QUEUE_NAME = "rpc_queue";

	public static void main(String[] args) throws IOException, TimeoutException {

		Connection connection = Helper.getNewConnection();
		final Channel channel = connection.createChannel();
		
		channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
		channel.basicQos(1);
		System.out.println(" [x] Awaiting RPC requests");
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()
						.correlationId(properties.getCorrelationId()).build();

				String response = "";

				try {
					String message = new String(body, "UTF-8");
					int n = Integer.parseInt(message);

					System.out.println(" [.] fib(" + message + ")");
					response += fib(n);
				} catch (RuntimeException e) {
					System.out.println(" [.] " + e.toString());
				} finally {
					channel.basicPublish("", properties.getReplyTo(), replyProps, response.getBytes("UTF-8"));

					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			}
		};

		channel.basicConsume(RPC_QUEUE_NAME, false, consumer);

		channel.close();
		connection.close();
	}

	private static int fib(int n) {
		if (n == 0)
			return 0;
		if (n == 1)
			return 1;
		return fib(n - 1) + fib(n - 2);
	}

}
