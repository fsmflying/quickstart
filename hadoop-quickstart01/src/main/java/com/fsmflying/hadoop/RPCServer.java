package com.fsmflying.hadoop;

import java.io.IOException;
//import java.net.InetSocketAddress;

import org.apache.hadoop.HadoopIllegalArgumentException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.Server;

public class RPCServer {
	public static void main(String[] args) throws HadoopIllegalArgumentException, IOException {
		Configuration conf = new Configuration();
		Server server = new RPC.Builder(conf)//
				.setProtocol(FMRpcService.class)//
				.setInstance(new DefaultFMRpcServiceImpl())//
				.setBindAddress("localhost")//
				.setPort(9527)//
				
				.build();
		server.start();
	}
}
