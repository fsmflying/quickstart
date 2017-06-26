package com.fsmflying.netty.part01;

import io.netty.buffer.ByteBuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * Handles a server-side channel.
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter { // (1)

	// @Override
	// public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
	// // Discard the received data silently.
	// ((ByteBuf) msg).release(); // (3)
	// }

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		ByteBuf in = (ByteBuf) msg;
		try {
			byte[] bytes = new byte[1024];
			int i = 0;
			while (in.isReadable() && i < bytes.length) { // (1)
				bytes[i] = in.readByte();
				i++;
			}
			System.out.println("[server][" + Thread.currentThread().getName() + "]:" + new String(bytes));
		} finally {
			ReferenceCountUtil.release(msg); // (2)
			ctx.close();
		}
	}
}