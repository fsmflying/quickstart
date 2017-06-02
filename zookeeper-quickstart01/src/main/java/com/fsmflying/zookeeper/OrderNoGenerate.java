package com.fsmflying.zookeeper;

import java.util.Calendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderNoGenerate {

	public static void main(String[] args) {
		final CountDownLatch latch = new CountDownLatch(1);
		ExecutorService executorService = Executors.newCachedThreadPool();// Service.
		for (int i = 0; i < 100; i++) {
			executorService.submit(new Runnable() {

				@Override
				public void run() {
					try {
						latch.await();
						System.out.println("{订单号：\""+getOrderNo()+"\",name:\""+Thread.currentThread().getName()+"\"}");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					//System.out.println("{" + Thread.currentThread().getName() + "}");
				}

			});
		}
		latch.countDown();
		
		executorService.shutdown();
	}
	
	public static int num=101;
	public static String getOrderNo()
	{
		return new java.text.SimpleDateFormat("YYYYMMDDHHMMSS").format(Calendar.getInstance().getTime()) +"-"+ (num++);
	}

}
