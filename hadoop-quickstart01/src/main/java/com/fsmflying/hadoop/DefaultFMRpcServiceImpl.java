package com.fsmflying.hadoop;

public class DefaultFMRpcServiceImpl implements FMRpcService {
	
	@Override
	public String sayHello(String name) {
		// TODO Auto-generated method stub
		return String.format("Hello,%s !", name);
	}

	@Override
	public String existFile(String path) {
		// TODO Auto-generated method stub
		return null;
	}

}
