package com.fsmflying.zookeeper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DnLockOrderNoGenerate {
	public static void main(String[] args) {
		ExecutorService executorService = Executors.newCachedThreadPool();// Service.
		final DnLock lock = new DnLock("192.168.1.104:2181", "test");
		for (int i = 0; i < 100; i++) {
			executorService.submit(new Runnable() {
				@Override
				public void run() {
					try {
						lock.lock();
						System.out.println(
								"{订单号：\"" + getOrderNo() + "\",thread:\"" + Thread.currentThread().getName() + "\"}");
						lock.unlock();
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			});
		}
		executorService.shutdown();
		while (true) {
			if (!executorService.isTerminated()) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			} else {
				System.out.println("----finished-----------------------");
				lock.close();
				break;
			}
		}
	}

	public static int num = 101;

	public static String getOrderNo() {
		return "" + (num++);
	}
}
