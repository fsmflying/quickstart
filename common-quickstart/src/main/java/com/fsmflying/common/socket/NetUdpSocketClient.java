package com.fsmflying.common.socket;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.io.IOException;
public class NetUdpSocketClient {
	public static void main(String args[])
	{
		System.out.println("**NetUdpSocketClient:");
		try{
			DatagramSocket socket = new DatagramSocket();
	    	String message = "Hello Server! I'm from A";
	    	DatagramPacket packet = new DatagramPacket(message.getBytes(),message.length(),InetAddress.getByName("localhost"),8000);
	    	socket.send(packet);
	    	
	    	byte[] rMessage = new byte[100];
	    	DatagramPacket rpacket = new DatagramPacket(rMessage,rMessage.length);
	    	socket.receive(rpacket);
	    	System.out.println("返回数据:"+new String(rMessage,0,rpacket.getLength()));
	    	socket.close();
		}
	    catch(SocketException e)
	    {
	    	e.printStackTrace();
	    }
	    catch(IOException e)
	    {
	    	e.printStackTrace();
	    }
	}
}



