package com.fsmflying.common.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
//import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.PrintWriter;

public class NetIMClient {
	@SuppressWarnings("unused")
	public static void main(String args[]) {
		// DatagramSocket socket = new DatagramSocket();
		/*
		 * try{ Socket socket = new Socket("127.0.0.1",8888); }
		 * catch(IOException e) { e.printStackTrace(); }
		 */
		boolean FALSE = false;
		if (true)
			startupClient2();
		else if (FALSE)
			startupClient();
	}

//	@SuppressWarnings("unused")
	private static void startupClient() {
		// TODO Auto-generated method stub
		System.out.println("启动客户端!");
		try {
			Socket socket = new Socket("127.0.0.1", 8888);
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// PrintWriter writer01 = new
			// PrintWriter(socket.getOutputStream(),true);
			while (true) {
				String line = reader.readLine();
				if (line.equals("over"))
					break;
				writer.write(line);
				writer.newLine();
				writer.flush();
				System.out.println("服务器端回传内容为:" + serverReader.readLine());
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

//	@SuppressWarnings("unused")
	private static void startupClient2() {
		// TODO Auto-generated method stub
		System.out.println("启动客户端!");
		try {
			Socket socket = new Socket("127.0.0.1", 8888);
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer01 = new PrintWriter(socket.getOutputStream(), true);
			while (true) {
				String line = reader.readLine();
				if (line.equals("over"))
					break;
				writer01.println(line);
				System.out.println("服务器端回传内容为:" + serverReader.readLine());
			}
			writer01.close();
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
