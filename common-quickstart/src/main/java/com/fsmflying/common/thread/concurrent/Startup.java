package com.fsmflying.common.thread.concurrent;

import java.lang.invoke.MethodHandles.Lookup;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Startup {
    public static void main(String[] args) throws InterruptedException {
        // ReentrantLock lock = new ReentrantLock();
        // Lookup
        // HashMap
        // usingThreadLocal();

        usingThreadPool();
    }

    public static void usingThreadLocal() throws InterruptedException {

        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(3);

        Integer value = new Integer(0);
        MyThread00 t01 = new MyThread00(value, 1, start, end);
        MyThread00 t02 = new MyThread00(value, 2, start, end);
        MyThread00 t03 = new MyThread00(value, 3, start, end);

        // ThreadLocal<Integer> value = new ThreadLocal<Integer>() {
        // @Override
        // protected Integer initialValue() {
        // // return super.initialValue();
        // return 0;
        // }
        // };
        // MyThread01 t01 = new MyThread01(value, 1, start, end);
        // MyThread01 t02 = new MyThread01(value, 2, start, end);
        // MyThread01 t03 = new MyThread01(value, 3, start, end);

        // AtomicInteger value = new AtomicInteger(0);
        // MyThread02 t01 = new MyThread02(value, 1, start, end);
        // MyThread02 t02 = new MyThread02(value, 2, start, end);
        // MyThread02 t03 = new MyThread02(value, 3, start, end);

        t01.start();
        t02.start();
        t03.start();
        start.countDown();

        end.await();
        System.out.println("[" + Thread.currentThread().getName() + "][main]:" + value + "");
    }

    public static void usingThreadPool() throws InterruptedException {
        ExecutorService es01 = Executors.newFixedThreadPool(10);
        // ExecutorService es01 = Executors.newCachedThreadPool();
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(100);
        ThreadLocal<Integer> value = new ThreadLocal<Integer>() {
            @Override
            protected Integer initialValue() {
                return 0;
            }
        };
        for (int i = 0; i < 25; i++) {
            es01.execute(new MyThread01(value, 1, start, end));

        }
        // for (int i = 0; i < 10; i++) {
        // es01.submit(new MyThread01(value, 1, start, end));
        // }

        start.countDown();
        es01.shutdown();
        while (true) {
            if (es01.isTerminated()) {
                System.out.println("====finished========================================");
                break;
            } else {

                Thread.sleep(200);
                continue;
            }
        }

    }
}

class MyThread00 extends Thread {

    Integer local;
    int increment;
    CountDownLatch start;
    CountDownLatch end;

    // public MyThread02(Integer value, int increment, CountDownLatch start,
    // CountDownLatch end) {
    // this.local = new ThreadLocal<Integer>();
    // this.local.set(value);
    // this.increment = increment;
    // this.start = start;
    // this.end = end;
    // }

    public MyThread00(Integer value, int increment, CountDownLatch start, CountDownLatch end) {
        this.local = value;
        this.increment = increment;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        try {
            this.start.await();
            // System.out.println(this.local);
            for (int i = 0; i < 100; i++) {
                this.local = this.local + 1;
                System.out.println(
                        "[" + Thread.currentThread().getName() + "][" + this.increment + "]:" + this.local + "");
            }
            this.end.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MyThread01 extends Thread {

    ThreadLocal<Integer> local;
    int increment;
    CountDownLatch start;
    CountDownLatch end;

    // public MyThread01(Integer value, int increment, CountDownLatch start,
    // CountDownLatch end) {
    // this.local = new ThreadLocal<Integer>();
    // this.local.set(value);
    // this.increment = increment;
    // this.start = start;
    // this.end = end;
    // }

    public MyThread01(ThreadLocal<Integer> value, int increment, CountDownLatch start, CountDownLatch end) {
        this.local = value;
        this.increment = increment;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        try {
            this.start.await();
            // System.out.println(this.local.get());
            for (int i = 0; i < 100; i++) {
                this.local.set(this.local.get() + increment);
                System.out.println(
                        "[" + Thread.currentThread().getName() + "][" + this.increment + "]:" + this.local.get() + "");
            }
            this.end.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MyThread02 extends Thread {

    AtomicInteger local;
    int increment;
    CountDownLatch start;
    CountDownLatch end;

    // public MyThread02(Integer value, int increment, CountDownLatch start,
    // CountDownLatch end) {
    // this.local = new ThreadLocal<Integer>();
    // this.local.set(value);
    // this.increment = increment;
    // this.start = start;
    // this.end = end;
    // }

    public MyThread02(AtomicInteger value, int increment, CountDownLatch start, CountDownLatch end) {
        this.local = value;
        this.increment = increment;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        try {
            this.start.await();
            System.out.println(this.local.get());
            for (int i = 0; i < 100; i++) {
                this.local.set(this.local.get() + increment);
                System.out.println(
                        "[" + Thread.currentThread().getName() + "][" + this.increment + "]:" + this.local.get() + "");
            }
            this.end.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
