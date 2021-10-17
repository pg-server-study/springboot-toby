package com.example.springtoby.chapter2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ContextConfiguration
public class SpringContextTest {

    @Autowired
    ApplicationContext context;

    static ApplicationContext contextObject = null;

    @Test
    public void test1() {
        assertThat(contextObject == null || contextObject == this.context).isTrue();
        contextObject = this.context;
    }

    @Test
    public void test2() {
        assertThat(contextObject == null || contextObject == this.context).isTrue();
        contextObject = this.context;
    }

    @Test
    public void test3() {
        assertThat(contextObject == null || contextObject == this.context).isTrue();
        contextObject = this.context;
    }

}
