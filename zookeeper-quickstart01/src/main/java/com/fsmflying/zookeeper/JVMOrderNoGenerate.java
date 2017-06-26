package com.fsmflying.zookeeper;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用ReentrantLock锁来生成订单唯一编号，只能在同一个进程的有多个线程情况下保持唯一
 * @author FangMing
 *
 */
public class JVMOrderNoGenerate {
	public static void main(String[] args) {
		final CountDownLatch latch = new CountDownLatch(1);
		ExecutorService executorService = Executors.newCachedThreadPool();// Service.
		final Lock lock = new ReentrantLock();
		for (int i = 0; i < 100; i++) {
			executorService.submit(new Runnable() {

				@Override
				public void run() {
					try {
						latch.await();
						lock.lock();
						System.out.println(
								"{订单号：\"" + getOrderNo() + "\",thread:\"" + Thread.currentThread().getName() + "\"}");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					lock.unlock();
					// System.out.println("{" + Thread.currentThread().getName()
					// + "}");
				}

			});
		}
		latch.countDown();

		executorService.shutdown();
	}

	public static int num = 101;
	public synchronized static String getOrderNo() {
		return "" + (num++);
	}
}
