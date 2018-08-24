package com.fsmflying.common.proxy.jdk;

public class ConsoleSayA implements SayA {

    @Override
    public void say(String message) {
        System.out.println("[ConsoleSay]:" + message);
    }

}
