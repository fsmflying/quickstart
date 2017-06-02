package com.fsmflying.hadoop;

import java.io.IOException;
import java.net.InetSocketAddress;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

public class RPCClient {
	public static void main(String[] args) throws IOException {
		System.out.println("args.length:" + args.length);
		String inetaddr = "localhost";
		if (args.length >= 1)
			inetaddr = args[0];
		int port = 9527;
		if (args.length >= 2)
			port = Integer.parseInt(args[1]);
		String name = "FangMing";
		if (args.length >= 3)
			name = args[2];
		Configuration conf = new Configuration();
		FMRpcService proxy = RPC.getProxy(FMRpcService.class, // protocol
				1L, // clientVersion:define by protocol
				new InetSocketAddress(inetaddr, port), // addr
				conf);// conf
		String result = proxy.sayHello(name);
		System.out.println("result:" + result);
		RPC.stopProxy(proxy);

	}
}
