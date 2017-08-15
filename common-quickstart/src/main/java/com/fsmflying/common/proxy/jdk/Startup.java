package com.fsmflying.common.proxy.jdk;

public class Startup {

	public static void main(String[] args) {
		SayA say01 = (SayA) A.newInstance(new ConsoleSayA());
		say01.say("fangming");
		say01.say("wangliang");
		System.out.println("[say01]:" + say01.getClass().getCanonicalName());
	}
}
