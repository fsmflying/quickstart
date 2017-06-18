package com.fsmflying.common.thread;

public class DeadLock {
	
	public static final Object lockA = new Object();
	public static final Object lockB = new Object();
	
//	public synchronized static void C()
//	{
//		System.out.println("C");
//	}
	
	public static void A()
	{
		synchronized(lockA)
		{
			System.out.println("A");
		}
	}
	
	public static void B()
	{
		synchronized(lockB)
		{
			System.out.println("B");
		}
	}
	
	public static void main(String[] args) {
		new Thread(new Runnable(){

			@Override
			public void run() {
				synchronized(lockA)
				{
						try {
							Thread.sleep(2000L);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						B();
//						C();
				}
			}
			
		}).start();
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				synchronized(lockB)
				{
					A();
				}
			}
			
		}).start();
	}
}
