package com.fsmflying.common.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Thread03Queue {
	public static void main(String[] args) {
		BlockingQueue<String> queue = new ArrayBlockingQueue<String>(1);
		Thread t1 = new Thread(new Producer(queue));
		Thread t2 = new Thread(new Consumer(queue));
		Thread t3 = new Thread(new Consumer(queue));
		Thread t4 = new Thread(new Consumer(queue));
		t1.start();
		t2.start();
		t3.start();
		t4.start();
	}

	static class Producer implements Runnable {
		BlockingQueue<String> queue;

		public Producer(BlockingQueue<String> queue) {
			this.queue = queue;
		}

		public String produce() {
			System.out.println(
					"[" + System.currentTimeMillis() + "][" + Thread.currentThread().getName() + "][produce]:A");
			return "A";
		}

		@Override
		public void run() {
			try {
				while (true) {
					queue.put(produce());
				}
			} catch (InterruptedException ex) {

			}
		}

	}

	static class Consumer implements Runnable {
		BlockingQueue<String> queue;

		public Consumer(BlockingQueue<String> queue) {
			this.queue = queue;
		}

		public void consume(String e) {
			System.out.println(
					"[" + System.currentTimeMillis() + "][" + Thread.currentThread().getName() + "][consume]:" + e);
		}

		@Override
		public void run() {
			try {
				while (true) {
					if (queue.size() <= 5) {

					}
					consume(queue.take());
				}
			} catch (InterruptedException ex) {
			}
		}

	}

}
