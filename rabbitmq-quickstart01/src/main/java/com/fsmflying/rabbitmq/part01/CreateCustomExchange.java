package com.fsmflying.rabbitmq.part01;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.fsmflying.rabbitmq.Helper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class CreateCustomExchange {
	public static void main(String[] args) {
		String exchangeName = "";
		String exchangeType = "direct";
		boolean durable = false;
		if (args.length >= 1 && !args[0].isEmpty() && !args[0].trim().isEmpty()) {
			exchangeName = args[0];
		} else {
			System.out.println("exchangeName is not valid !");
			System.out.println("usage: "
					+ CreateCustomExchange.class.getCanonicalName()
					+ " <exchangeName> <direct|fanout|topic|headers> <durable>");
			System.exit(0);
		}
		if (args.length >= 2) {
			if (args[1].equalsIgnoreCase("direct"))
				exchangeType = "direct";
			else if (args[1].equalsIgnoreCase("fanout"))
				exchangeType = "fanout";
			else if (args[1].equalsIgnoreCase("topic"))
				exchangeType = "topic";
			else if (args[1].equalsIgnoreCase("headers"))
				exchangeType = "headers";
		}
		if (args.length >= 3 && args[2].equalsIgnoreCase("true")) {
			durable = true;
		}

		try {
			createExchange(exchangeName, exchangeType, durable);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	public static void createExchange(String exchangeName, String exchangeType, boolean durable)
			throws IOException, TimeoutException {
		Connection conn = Helper.getNewConnection();
		Channel channel = conn.createChannel();
		channel.exchangeDeclare(exchangeName, exchangeType, durable);
		channel.close();
		conn.close();
	}
}
