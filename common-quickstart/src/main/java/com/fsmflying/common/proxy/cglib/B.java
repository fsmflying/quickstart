package com.fsmflying.common.proxy.cglib;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class B implements MethodInterceptor {

    Object target;

    public B() {

    }

    public B(Object target) {
        this.target = target;
    }

    public Object newInstance(Object target) {
        this.target = target;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.target.getClass());
        // 回调方法
        enhancer.setCallback(this);
        // 创建代理对象
        return enhancer.create();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("事务开始");
        Object rValue = proxy.invokeSuper(obj, args);
        System.out.println("事务结束");
        return rValue;
    }

}
