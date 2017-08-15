package com.fsmflying.netty.part05;

import java.io.BufferedOutputStream;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class CollectServerHandler2 extends ChannelInboundHandlerAdapter {
	private BufferedOutputStream out;
	private AtomicInteger gseq;

	public CollectServerHandler2(BufferedOutputStream out) {
		this.out = out;
	}

	public CollectServerHandler2(BufferedOutputStream out, AtomicInteger seq) {
		this.out = out;
		this.gseq = seq;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		ByteBuf buf = (ByteBuf) msg;

		// System.out.println("[buf][readableBytes]:" + buf.readableBytes());
		if (buf.readableBytes() < 8) {
			return;
		}
		int seq = buf.readInt();

		if (buf.readableBytes() < 4) {
			return;
		}
		int len = buf.readInt();

		// buf.markReaderIndex();
		// if (buf.readableBytes() < len) {
		// return;
		// }
		// buf.readBytes(out, len);

		// buf.resetReaderIndex();
		byte[] bytes = new byte[len];
		buf.getBytes(8, bytes);
		
		// byte[] bytes02 = new byte[len+1];
		byte[] bytes02 = Arrays.copyOf(bytes, len + 1);
		bytes02[len] = '\n';
		out.write(bytes02);
		out.flush();
		// out.write('\n');
		if (this.gseq != null)
			this.gseq.set(this.gseq.incrementAndGet());
		if (this.gseq != null)
			System.out
					.println("gseq[" + this.gseq.intValue() + "]seq[" + seq + "]len[" + len + "]:" + new String(bytes));
		else
			System.out.println("seq[" + seq + "]len[" + len + "]:" + new String(bytes));
		// ctx.writeAndFlush("ok");
		ReferenceCountUtil.release(msg);

	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// super.channelReadComplete(ctx);
		// ctx.close();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// super.exceptionCaught(ctx, cause);
		ctx.close();
	}

}
