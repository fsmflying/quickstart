package com.fsmflying.zookeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AssistTest01 {

	class Worker implements Runnable {
		private final CountDownLatch startSignal;
		private final CountDownLatch doneSignal;
		private final String name;

		Worker(String name, CountDownLatch startSignal, CountDownLatch doneSignal) {
			this.name = name;
			this.startSignal = startSignal;
			this.doneSignal = doneSignal;
		}

		public void run() {
			try {
				System.out.println("{thread[" + name + "]:run()}");
				startSignal.await();
				doWork();
				doneSignal.countDown();
			} catch (InterruptedException ex) {
			} // return;
		}

		void doWork() {
			System.out.println("{thread[" + name + "]:doWork()}");
		}
	}

	CountDownLatch startSignal = null;
	CountDownLatch doneSignal = null;

	@Before
	public void before() throws IOException {
		startSignal = new CountDownLatch(1);
		doneSignal = new CountDownLatch(10);
	}

	@After
	public void after() throws InterruptedException {
		
	}

	@Test
	public void test_00() throws InterruptedException {
		int N = 10;
		for (int i = 0; i < N; ++i) // create and start threads
			new Thread(new Worker(new String(i + ""), startSignal, doneSignal)).start();

		// doSomethingElse(); // don't let run yet
		System.out.println("doSomethingElse01");
		Thread.currentThread().sleep(3000);
		startSignal.countDown(); // let all threads proceed
		// doSomethingElse();
		System.out.println("doSomethingElse02");
		Thread.currentThread().sleep(3000);
		doneSignal.await(); // wait for all to finish
	}
}
