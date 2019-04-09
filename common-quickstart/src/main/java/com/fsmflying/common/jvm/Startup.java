package com.fsmflying.common.jvm;

import java.util.ArrayList;
import java.util.List;

public class Startup {

    static class User {
        private int _1MB = 1024 * 1024;
        byte[] size = new byte[_1MB];
        String name;
        String sex;

        @Override
        public String toString() {
            return "User [name=" + name + ", sex=" + sex + "]";
        }

    }

    public static void main(String[] args){
        List<User> list = new ArrayList<User>();
        int i=0;
        while(true){
            System.err.println("Eden 添加的次数:" + (++i));
            list.add(new User());
        }
    }
}
