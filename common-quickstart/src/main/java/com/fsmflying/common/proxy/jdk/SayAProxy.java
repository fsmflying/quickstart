package com.fsmflying.common.proxy.jdk;

public class SayAProxy implements SayA {

	SayA say;

	public SayAProxy(SayA obj) {
		this.say = obj;
	}

	@Override
	public void say(String message) {
		this.say.say(message);
	}

}
