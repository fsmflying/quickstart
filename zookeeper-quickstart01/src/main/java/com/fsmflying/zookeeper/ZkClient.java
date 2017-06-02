package com.fsmflying.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

public class ZkClient {
	public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
//		CountDownLatch;
		java.util.concurrent.CountDownLatch countDownLatch = null;
		ZooKeeper zk = new ZooKeeper("192.168.1.104:2181",2000,null);
		zk.getData("/", null, null);
		zk.close();
	}
}
