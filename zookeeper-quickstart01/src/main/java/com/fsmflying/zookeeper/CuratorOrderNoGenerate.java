package com.fsmflying.zookeeper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CuratorOrderNoGenerate {
	// TODO Auto-generated method stub
	static final CuratorFramework curatorfwk = CuratorFrameworkFactory.builder()//
			.connectString("192.168.1.104:2181")//
			.retryPolicy(new ExponentialBackoffRetry(2000, 10, 3000))//
			.build();
	static final int N = 100;
	static FileOutputStream out = null;
	static CountDownLatch endSignal = new CountDownLatch(N);

	public static void main(String[] args) throws IOException, InterruptedException {
		final String timeStr = new java.text.SimpleDateFormat("YYYYMMDDHHMMSS").format(Calendar.getInstance().getTime());
		curatorfwk.start();
		String basedir = CuratorOrderNoGenerate.class.getResource("/").getPath();
		out = new FileOutputStream(basedir + "/orderNo" + timeStr + ".log", true);
		final CountDownLatch latch = new CountDownLatch(1);
		ExecutorService executorService = Executors.newCachedThreadPool();// Service.
		// final Lock lock = new ReentrantLock();
		final InterProcessMutex lock = new InterProcessMutex(curatorfwk, "/dn_bit");
		for (int i = 0; i < N; i++) {
			executorService.submit(new Runnable() {

				@Override
				public void run() {
					try {
						latch.await();
						lock.acquire();
						// lock.lock();
						String msg = "{time:\"" + timeStr + "\",订单号：\"" + getOrderNo() + "\",thread:\""
								+ Thread.currentThread().getName() + "\"}";
						System.out.println(msg);
						out.write(msg.getBytes());
						lock.release();
						endSignal.countDown();
						// lock.unlock();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			});
		}
		latch.countDown();
		
//		executorService.awaitTermination(N/10, TimeUnit.SECONDS);

		endSignal.await();
		out.flush();
		out.close();
		executorService.shutdown();
		curatorfwk.close();
		
	}

	public static int num = 101;

	public static String getOrderNo() {
//		return new java.text.SimpleDateFormat("YYYYMMDDHHMMSS").format(Calendar.getInstance().getTime()) + (num++);
		return new Integer(num++).toString();
	}

}
