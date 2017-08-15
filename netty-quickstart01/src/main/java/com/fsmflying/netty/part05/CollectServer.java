package com.fsmflying.netty.part05;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class CollectServer {
	static Logger logger = LoggerFactory.getLogger(CollectServer.class);
	static final boolean SSL = System.getProperty("ssl") != null;
	static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));
	static BufferedOutputStream out;

	public static void main(String[] args) {
		System.out.println("Server started ! wait for client messages !");
		logger.debug("Server started ! wait for client messages !");
		// System.out.println("PORT:" + PORT);
		EventLoopGroup boss = new NioEventLoopGroup(1);
		EventLoopGroup worker = new NioEventLoopGroup();
		ServerBootstrap b = new ServerBootstrap();
		try {
			out = new BufferedOutputStream(new FileOutputStream("d:\\private\\output.data",true));
			b.group(boss, worker)//
					.channel(NioServerSocketChannel.class)//
					/**
					 * BACKLOG用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，
					 * 用于临时存放已完成三次握手的请求的队列的最大长度。如果未设置或所设置的值小于1，
					 * Java将使用默认值50。
					 */
					.option(ChannelOption.SO_BACKLOG, 1024 * 1024)//
					
					/**
					 * 是否启用心跳保活机制。在双方TCP套接字建立连接后（即都进入ESTABLISHED状态）,
					 * 并且在两个小时左右上层没有任何数据传输的情况下，这套机制才会被激活。
					 */
					//.option(ChannelOption.SO_KEEPALIVE, true)
					
					
					/**
					 * 在TCP/IP协议中，无论发送多少数据，总是要在数据前面加上协议头，同时，对方接收到数据，也需要发送ACK表示确认。
					 * 为了尽可能的利用网络带宽，TCP总是希望尽可能的发送足够大的数据。
					 * 这里就涉及到一个名为Nagle的算法，该算法的目的就是为了尽可能发送大块数据，避免网络中充斥着许多小数据块。
					 * 
					 * TCP_NODELAY就是用于启用或关于Nagle算法。
					 * 如果要求高实时性，有数据发送时就马上发送，就将该选项设置为true(关闭Nagle算法)；
					 * 如果要减少发送次数减少网络交互，就设置为false等累积一定大小后再发送。默认为false。
					 */
					//.option(ChannelOption.TCP_NODELAY, true)
					
					.handler(new LoggingHandler(LogLevel.DEBUG))//
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(new CollectServerHandler2(out));
						}
					});

			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				@Override
				public void run() {
					if (out != null)
						try {
							out.flush();
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					boss.shutdownGracefully();
					worker.shutdownGracefully();
					System.out.println("Runtime shutdownHook !");
					logger.debug("Runtime shutdownHook !");
				}
			}));

			ChannelFuture f = b.bind(PORT).sync();
			System.out.println("f:" + f.getClass().getCanonicalName());
			logger.debug("f:" + f.getClass().getCanonicalName());
			f.channel().closeFuture().sync();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			boss.shutdownGracefully();
			worker.shutdownGracefully();
			System.out.println("finally shutdownHook !");
			logger.debug("finally shutdownHook !");
		}

	}
}
