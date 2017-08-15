package com.fsmflying.rabbitmq;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.AMQP.Exchange.DeclareOk;
//import com.rabbitmq.client.BasicProperties;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class StartupTests {

	Logger logger = LoggerFactory.getLogger(StartupTests.class);
	Connection conn = null;
	Channel channel = null;
	Writer writer = null;

	@Before
	public void before()
			throws KeyManagementException, NoSuchAlgorithmException, URISyntaxException, IOException, TimeoutException {
		// ConnectionFactory factory = new ConnectionFactory();
		// factory.setUsername("rabbitadmin");
		// factory.setPassword("fangming");
		// // factory.setVirtualHost("");
		// factory.setHost("192.168.1.104");
		// // factory.setPort(5762);

		// amqp://userName:password@hostName:portNumber/virtualHost
		// factory.setUri("amqp://rabbitadmin:fangming@192.168.1.104:5672/");
		String basePath = StartupTests.class.getClassLoader().getResource("data.100000.2.txt").getPath();
		// conn = factory.newConnection();
		conn = Helper.getNewConnection();
		channel = conn.createChannel();
		basePath = basePath.substring(0, basePath.lastIndexOf("/target/"));
		writer = new FileWriter(basePath + "/StartupTests.test.log", true);
		writer.append("======["
				+ new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())
				+ "]======================================================================");
		logger.info("======["
				+ new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())
				+ "]======================================================================");
	}

	@After
	public void after() throws IOException, TimeoutException {
		channel.close();
		conn.close();
		if (writer != null) {
			writer.flush();
			writer.close();
		}
	}

	@Test
	public void test_00() {

	}

	/**
	 * 声明exchange就是创建exchange,当服务器中没有这个exchange时，就会创建，如果已经存在，则引用它
	 * 
	 * @throws IOException
	 */
	@Test
	public void test_01_exchangeDeclare() throws IOException {
		String exchangeName = "testExchange";
		DeclareOk ok = channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true);

	}

	@Test
	public void test_01_createExchange() throws IOException {
		String exchangeName = "testExchange";
		channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true);
		// 通过rabbitmqctl list_exchanges指令列出服务器上所有可用的交换器
	}

	@Test
	public void test_02_produceMessage() throws UnsupportedEncodingException, IOException {
		String message = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())
				+ "Hello RabbitMQ";
		channel.basicPublish("testExchange", "",  
				new AMQP.BasicProperties.Builder()//一个属性构建器
	               .contentType("text/plain")//设定内容类型
	               .deliveryMode(2)//设定交付模式，2表示persistent
	               .priority(1)//设定优先级
	               .userId("bob")//设定用户
	               .build(), 
	            message.getBytes("UTF-8"));
	}

	@Test
	public void test_03_produceToLogsExchange() throws IOException {

		// String queueName = "";
		String message = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())
				+ "Hello RabbitMQ";
		String exchangeName = "logs";// 使用了匿名的交换器
		String routingKey = "";// 发送到routingKey名称对应的队列
		BasicProperties bp = null;

		// channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT,
		// true);

		channel.basicPublish(exchangeName, routingKey, bp, message.getBytes("UTF-8"));
		System.out.println("[test_03_produceToLogsExchange]Producer Send +'" + message + "'");
	}

	@Test
	public void test_03_produceToTestExchange() throws IOException {

		// String queueName = "";
		String message = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())
				+ "Hello RabbitMQ";
		String exchangeName = "testExchange";// 使用了匿名的交换器
		String routingKey = "";// 发送到routingKey名称对应的队列
		BasicProperties bp = null;

		// channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT,
		// true);
		channel.basicPublish(exchangeName, routingKey, bp, message.getBytes("UTF-8"));
		System.out.println("Producer Send +'" + message + "'");
		logger.info("[test_03_produceToTestExchange]Producer Send +'" + message + "'");
	}

	/**
	 * 
	 * @throws IOException
	 */
	@Test
	public void test_04_bindQueueToTestExchange() throws IOException {
		String exchangeName = "testExchange";// 使用了匿名的交换器
		String queueName = "testQueue";
		String routingKey = "";// 发送到routingKey名称对应的队列

		// channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT,
		// true);
		channel.queueBind(queueName, exchangeName, routingKey);
		// 如果要查看绑定列表，可以执行【rabbitmqctl list_bindings】命令
		logger.info("[test_04_bindQueueToTestExchange]");
	}

	/**
	 * 
	 * @throws IOException
	 */
	@Test
	public void test_04_bindQueueToLogsExchange() throws IOException {
		String exchangeName = "logs";// 使用了匿名的交换器
		String queueName = "testQueue";
		String routingKey = "";// 发送到routingKey名称对应的队列
		// channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT,
		// true);
		channel.queueBind(queueName, exchangeName, routingKey);
		// 如果要查看绑定列表，可以执行【rabbitmqctl list_bindings】命令
		logger.info("[test_04_bindQueueToLogsExchange]");
	}

	@Test
	public void test_11_queueDeclare() throws IOException {
		String queueName = "testQueue110";
		boolean durable = false;// 是否是一个持久队列
		boolean exclusive = false; // 是否声明一个排它队列，
		boolean autoDelete = false; // 是否声明一个自动删除队列，服务器将在不使用它后，自动删除队列
		// Map<String, Object> arguments = new HashMap<String, Object>();
		Map<String, Object> arguments = null;
		channel.queueDeclare(queueName, durable, exclusive, autoDelete, arguments);
	}

	/**
	 * 
	 * @throws IOException
	 */
	@Test
	public void test_04_bindQueue02ToLogsExchange() throws IOException {
		String exchangeName = "logs";// 使用了匿名的交换器
		String queueName = "testQueue02";
		String routingKey = "";// 发送到routingKey名称对应的队列
		channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true);
		channel.queueBind(queueName, exchangeName, routingKey);
		// 如果要查看绑定列表，可以执行【rabbitmqctl list_bindings】命令
		logger.info("[test_04_bindQueueToLogsExchange]");
	}

	/**
	 * 
	 * @throws IOException
	 */
	@Test
	public void test_04_bindQueue02ToTestExchange() throws IOException {
		String exchangeName = "testExchange";// 使用了匿名的交换器
		String queueName = "testQueue02";
		String routingKey = "";// 发送到routingKey名称对应的队列
		channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true);
		channel.queueBind(queueName, exchangeName, routingKey);
		// 如果要查看绑定列表，可以执行【rabbitmqctl list_bindings】命令
		logger.info("[test_04_bindQueueToLogsExchange]");
	}

	@Test
	public void test_11_ProduceToQueue() throws IOException {
		String queueName = "testQueue110";
		String message = "Hello RabbitMQ";
		String exchangeName = "";// 使用了匿名的交换器
		channel.basicPublish(exchangeName, queueName, null, message.getBytes("UTF-8"));
		System.out.println("Producer Send +'" + message + "'");

	}

	@Test
	public void test_11_consumeFromQueue() throws IOException {
		String queueName = "testQueue110";
		// 声明要关注的队列
		channel.queueDeclare(queueName, false, false, false, null);
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
		channel.basicConsume(queueName, true, consumer);

	}

	@Test
	public void test_12_getEmptyQueue() throws IOException {
		String queueName = channel.queueDeclare().getQueue();
		System.out.println("random queue name:" + queueName);

	}
}
