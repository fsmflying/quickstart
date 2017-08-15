package com.fsmflying.rabbitmq.part01;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.fsmflying.rabbitmq.Helper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class DeleteCustomExchange {
	public static void main(String[] args) {
		String exchangeName = "";
		if (args.length >= 1 && !args[0].isEmpty() && !args[0].trim().isEmpty()) {
			exchangeName = args[0];
		} else {
			System.out.println("exchangeName is not valid !");
			System.out.println("usage: " + DeleteCustomExchange.class.getCanonicalName()
					+ " <exchangeName> <direct|fanout|topic|headers> <durable>");
			System.exit(0);
		}

		try {
			createExchange(exchangeName);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	public static void createExchange(String exchangeName) throws IOException, TimeoutException {
		Connection conn = Helper.getNewConnection();
		Channel channel = conn.createChannel();
		channel.exchangeDelete(exchangeName);
		channel.close();
		conn.close();
	}
}
