package com.fsmflying.common.socket;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
//import java.net.InetAddress;
import java.net.SocketException;
import java.io.IOException;
public class NetUdpSocketServer {
	public static void main(String args[])
	{
		System.out.println("**NetUdpSocketServer:");
		try{
			
			
	    	DatagramSocket socket = new DatagramSocket(8000);
			System.out.println("**1:");
	    	byte[] rMessage = new byte[100];
	    	DatagramPacket rpacket = new DatagramPacket(rMessage,rMessage.length);
	    	System.out.println("**2:");
	    	socket.receive(rpacket);
	    	System.out.println("**3:");
	    	System.out.println("接收数据:"+new String(rMessage,0,rpacket.getLength()));
	    	
	    	
	    	System.out.println("**4:");
	    	String message = new String(rMessage).toUpperCase();
	    	DatagramPacket packet = new DatagramPacket(message.getBytes(),message.length(),rpacket.getAddress(),rpacket.getPort());	    	
	    	System.out.println("**5:");
	    	socket.send(packet);
	    	System.out.println("**6:");
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