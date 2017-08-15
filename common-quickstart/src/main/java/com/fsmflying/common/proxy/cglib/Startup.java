package com.fsmflying.common.proxy.cglib;


public class Startup {
	public static void main(String[] args) {
//		Say say01 = (Say) A.newInstance(new ConsoleSay());
		B proxy = new B();
		SayB say01 = (SayB)proxy.newInstance(new ConsoleSayB());
		say01.say("fangming");
		say01.say("wangliang");
		System.out.println("[say01]:" + say01.getClass().getCanonicalName());
	}
}
