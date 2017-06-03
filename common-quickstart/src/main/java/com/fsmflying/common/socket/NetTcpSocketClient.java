package com.fsmflying.common.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
//import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetTcpSocketClient {
	public static void main(String args[])
	{
    	startupClient();
	}
	private static void startupClient() {
		// TODO Auto-generated method stub
		System.out.println("启动客户端!");
		try {
			Socket socket = new Socket("127.0.0.1",8888);
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			while(true){
			  String line = reader.readLine();
			  if(line.equals("over")) break;
			  writer.write(line);
			  writer.newLine();
			  writer.flush();
			  System.out.println("服务器端回传内容为:"+serverReader.readLine());
			}
			writer.close();
			reader.close();
			socket.close();
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
