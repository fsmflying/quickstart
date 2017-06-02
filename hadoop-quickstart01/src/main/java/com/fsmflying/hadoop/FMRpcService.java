package com.fsmflying.hadoop;

public interface FMRpcService {
	
	public static final long versionID = 1L;
	public String sayHello(String name);
	public String existFile(String path);
	
}
