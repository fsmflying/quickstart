package com.fsmflying.netty.part05;

import java.io.BufferedOutputStream;
import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class CollectServerHandler extends ChannelInboundHandlerAdapter {
	private BufferedOutputStream out;
	private ByteBuf lenBuf;
	private boolean lengthHasValue = false;
	private ByteBuf contentBuf;

	public CollectServerHandler(BufferedOutputStream out) {
		this.out = out;
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		if (!lengthHasValue) {
			lenBuf = ctx.alloc().buffer(4);
		} else {
			if (lengthHasValue)
				contentBuf = ctx.alloc().buffer(lenBuf.readInt());
		}
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		super.handlerRemoved(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		if (!lengthHasValue) {
			if (buf.readableBytes() < 4)
				return;
			lenBuf = buf.readBytes(4);
			lengthHasValue = true;
			this.contentBuf = ctx.alloc().buffer(lenBuf.readInt());
		} else {
			if (buf.readableBytes() < lenBuf.readInt())
				return;
			this.contentBuf = buf.readBytes(lenBuf.readInt());
			buf.release();
			ReferenceCountUtil.release(msg);
			System.out.println(this.contentBuf.toString(Charset.forName("UTF-8")));
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		super.channelReadComplete(ctx);
		ctx.close();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
	}

}
