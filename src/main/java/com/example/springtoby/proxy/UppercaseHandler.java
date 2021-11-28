package com.example.springtoby.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UppercaseHandler implements InvocationHandler {

    Object target;

    public UppercaseHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object ref = method.invoke(target, args);
        if (ref instanceof String && method.getName().startsWith("Say")) {
            return ((String) ref).toUpperCase();
        } else {
            return ref;
        }
    }
}

