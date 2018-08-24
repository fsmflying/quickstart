package com.fsmflying.common.invoke;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
//import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;

public class Startup {
    public static void main(String[] args) throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle mh = lookup.findStatic(Startup.class, "f", MethodType.methodType(int.class, int.class));
        for (int i = 0; i <= 30; i++) {
            System.out.println("f(" + i + "])=" + ((int) mh.invoke(i)));
        }
        // System.out.println("f(" + 46 + "])=" + ((int) mh.invoke(46)));
        System.out.println("max=" + Integer.MAX_VALUE);
        // System.out.println(1 << 31);
    }

    public static int f(int n) {
        if (n == 0)
            return 0;
        else if (n == 1)
            return 1;
        else
            return f(n - 1) + f(n - 2);
    }
}
