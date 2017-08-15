package com.fsmflying.common.proxy.cglib;

public class ConsoleSayB implements SayB {

	@Override
	public void say(String message) {
		System.out.println("[ConsoleSay]:"+message);
	}

}
