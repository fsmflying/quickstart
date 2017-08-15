package com.fsmflying.netty.custom;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
/**
 * 
 * <pre>
 * 自定义协议数据包
 *  数据包的格式:
 *  ＋－－－－－－+－-------+--------+
 *  ｜协议开始标志|  长度　　　|  数据          |
 *  ＋－－－－－－+－-------+--------+
 *  1.协议开始标志head_data,为int类型的数据
 *  2.传输数据的长度contentLength,也为int类型的数据
 *  3.要传输的数据
 * </pre>
 * @author FangMing
 *
 */
public class SmartCarEncoder extends MessageToByteEncoder<SmartCarProtocol> {

	@Override
	protected void encode(ChannelHandlerContext ctx, SmartCarProtocol msg, ByteBuf out) throws Exception {
		// TODO Auto-generated method stub
		out.writeInt(msg.getHead_data());
		out.writeInt(msg.getContentLength());
		out.writeBytes(msg.getContent());
		
	}

}
