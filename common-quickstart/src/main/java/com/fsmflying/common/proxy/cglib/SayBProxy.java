package com.fsmflying.common.proxy.cglib;

public class SayBProxy implements SayB {

	SayB say;

	public SayBProxy(SayB obj) {
		this.say = obj;
	}

	@Override
	public void say(String message) {
		this.say.say(message);
	}

}
