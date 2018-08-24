package com.fsmflying.common.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 实现动态代理类
 *
 * @author FangMing
 */
public class A implements InvocationHandler {

    private Object obj;

    public A(Object obj) {
        this.obj = obj;
    }

    public static Object newInstance(Object obj) {
        return java.lang.reflect.Proxy.newProxyInstance(obj.getClass().getClassLoader(), //
                obj.getClass().getInterfaces(), //
                new A(obj));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // TODO Auto-generated method stub
        System.out.println("A.invoke:parameter[proxy]:" + proxy.getClass().getCanonicalName());
        System.out.println("====Start====");
        Object rValue = method.invoke(obj, args);
        System.out.println("====End======");
        return rValue;
    }

}
