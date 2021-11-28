package com.example.springtoby.chapter6;

import com.example.springtoby.proxy.Hello;
import com.example.springtoby.proxy.HelloTarget;
import com.example.springtoby.proxy.HelloUppercase;
import com.example.springtoby.proxy.UppercaseHandler;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;


public class SimpleProxyTest {

    @Test
    public void simpleProxy() {
        Hello hello = new HelloTarget();

        assertThat(hello.sayHello("Toby")).isEqualTo("Hello Toby");
        assertThat(hello.sayHi("Toby")).isEqualTo("Hi Toby");
        assertThat(hello.sayThankYou("Toby")).isEqualTo("Thank You Toby");

    }

    @Test
    public void uppercaseProxy() {

        final String name = "Toby";

        Hello proxiedHello = new HelloUppercase(new HelloTarget());

        assertThat(proxiedHello.sayHello(name)).isEqualTo("HELLO TOBY");
        assertThat(proxiedHello.sayHi(name)).isEqualTo("HI TOBY");
        assertThat(proxiedHello.sayThankYou(name)).isEqualTo("THANK YOU TOBY");
    }

    @Test
    public void dynamicProxy() {

        final String name = "Toby";

        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] {Hello.class},
                new UppercaseHandler(new HelloTarget())
        );

        assertThat(proxiedHello.sayHello(name)).isEqualTo("HELLO TOBY");
        assertThat(proxiedHello.sayHi(name)).isEqualTo("HI TOBY");
        assertThat(proxiedHello.sayThankYou(name)).isEqualTo("THANK YOU TOBY");
    }

}
