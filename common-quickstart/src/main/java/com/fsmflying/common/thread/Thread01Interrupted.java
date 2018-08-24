package com.fsmflying.common.thread;

/**
 * 用5个线程循环输出A,B,C,D,E
 *
 * @author FangMing
 */
public class Thread01Interrupted {
    static final Object lockA = new Object();

    public static void main(String[] args) throws InterruptedException {

        Message m = new Message("E");
        Thread t1 = new Thread(new MyWaitAndNotifyThread(lockA, m, "E", "A"));
        Thread t2 = new Thread(new MyWaitAndNotifyThread(lockA, m, "A", "B"));
        Thread t3 = new Thread(new MyWaitAndNotifyThread(lockA, m, "B", "C"));
        Thread t4 = new Thread(new MyWaitAndNotifyThread(lockA, m, "C", "D"));
        Thread t5 = new Thread(new MyWaitAndNotifyThread(lockA, m, "D", "E"));
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
    }

}

class Message {
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Message(String message) {
        this.message = message;
    }

    public Message() {
    }
}

class MyWaitAndNotifyThread implements Runnable {

    Object lock;
    Message message;
    String waitMessage;
    String outputMessage;

    public MyWaitAndNotifyThread(Object lock, Message message, String waitMessage, String outputMessage) {
        this.lock = lock;
        this.message = message;
        this.waitMessage = waitMessage;
        this.outputMessage = outputMessage;
    }

    @Override
    public void run() {
        synchronized (message) {
            while (true) {
                try {
                    // System.out.println("[" + Thread.currentThread().getName()
                    // + "]{waitMessage:'" + this.waitMessage
                    // + "',outputMessage:'" + this.outputMessage + "'}");
                    if (!(waitMessage == message.getMessage())) {
                        message.wait();
                        // System.out.println("[wait][" +
                        // Thread.currentThread().getName() + "]:" +
                        // this.waitMessage + "->"
                        // + this.outputMessage);
                        continue;
                    } else {
                        // System.out.println("[" +
                        // Thread.currentThread().getName() + "]:" +
                        // this.waitMessage
                        // + "=========>" + this.outputMessage);
                    }
                    System.out.println(outputMessage);
                    this.message.setMessage(outputMessage);
                    this.message.notifyAll();
                    Thread.sleep(200);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
