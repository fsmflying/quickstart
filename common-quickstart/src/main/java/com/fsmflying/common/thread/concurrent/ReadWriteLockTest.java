package com.fsmflying.common.thread.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadWriteLockTest {
	static Logger logger = LoggerFactory.getLogger(ReadWriteLockTest.class);
	public static final ReadWriteLock lock = new ReentrantReadWriteLock();
	public static int num = 0;

	public static void increase(int increment) {
		lock.writeLock().lock();
		logger.info("--[" + Thread.currentThread().getName() + "]--increase--start------");
		num += increment;
		logger.info("--[" + Thread.currentThread().getName() + "]--increase--end--------");
		lock.writeLock().unlock();
	}

	public static int getNum() {

		lock.readLock().lock();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("--[" + Thread.currentThread().getName() + "]--getNum----start------");
		int val = num;
		logger.info("--[" + Thread.currentThread().getName() + "]--getNum----end--------");
		lock.readLock().unlock();
		return val;
	}

	public static void main(String[] args) throws InterruptedException {

		ExecutorService es01 = Executors.newFixedThreadPool(10);
		// ExecutorService es01 = Executors.newCachedThreadPool();
		// CountDownLatch start = new CountDownLatch(1);
		// CountDownLatch end = new CountDownLatch(100);
		for (int i = 1; i <= 25; i++) {
			if (i % 2 == 0)
				es01.execute(new Runnable() {
					@Override
					public void run() {
						logger.info("[getNum][" + Thread.currentThread().getName() + "][main]:" + "num="
								+ ReadWriteLockTest.getNum());
//						ReadWriteLockTest.increase(1);
					}

				});
			else
				es01.execute(new Runnable() {
					@Override
					public void run() {
						logger.info("[increase][" + Thread.currentThread().getName() + "][main]:" + "num="
								+ ReadWriteLockTest.getNum());
						ReadWriteLockTest.increase(5);
					}

				});

		}

		// start.countDown();
		es01.shutdown();
		while (true) {
			if (es01.isTerminated()) {
				logger.info("====finished========================================");
				break;
			} else {

				Thread.sleep(200);
				continue;
			}
		}

	}
}
