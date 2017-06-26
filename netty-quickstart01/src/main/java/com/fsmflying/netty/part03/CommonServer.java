package com.fsmflying.netty.part03;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;

public class CommonServer {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub

		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		ServerBootstrap b = new ServerBootstrap();
		b.group(boss, worker)//
				.channel(NioServerSocketChannel.class)
		// .handler(handler)
		;

		final ChannelFuture f = b.bind().sync();
		//way 1
		f.addListener(ChannelFutureListener.CLOSE);//way 1
		//way 2
		f.addListener(new ChannelFutureListener(){

			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				// TODO Auto-generated method stub
				
			}
			
		});

	}

}
