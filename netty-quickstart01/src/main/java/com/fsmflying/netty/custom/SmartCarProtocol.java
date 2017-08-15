package com.fsmflying.netty.custom;


public class SmartCarProtocol {
	/**
	 * 消息的开头的信息标志
	 */
	private int head_data = ConstantValue.HEAD_DATA;
	/**
	 * 消息的长度
	 */
	private int contentLength;
	
	/**
	 * 消息的内容
	 */
	private byte[] content;
	
	public SmartCarProtocol(int contentLength,byte[] content){
		this.contentLength = contentLength;
		this.content = content;
	}

	public int getHead_data() {
		return head_data;
	}

//	public void setHead_data(int head_data) {
//		this.head_data = head_data;
//	}

	public int getContentLength() {
		return contentLength;
	}

	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}
	
	
}
