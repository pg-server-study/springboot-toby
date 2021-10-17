package com.example.springtoby.chapter2;

import org.junit.jupiter.api.*;

public class JUnitAnnotation {

    @BeforeAll
    static void beforeAll() {
        System.out.println("테스트가 실행되기 전 단 한번만 실행 됩니다.");
        System.out.println();
    }

    @BeforeEach
    public void beforeEach() {
        System.out.println("테스트 코드별로 실행되기 전에 실행 됩니다.");
    }

    @Test
    public void firstTest() {
        System.out.println("첫번째 테스트");
    }

    @Test
    public void secondTest() {
        System.out.println("두번째 테스트");
    }

    @AfterEach
    public void afterEach() {
        System.out.println("테스트 코드별로 실행된 후 실행 됩니다.");
        System.out.println();
    }

    @AfterAll
    static void afterAll() {
        System.out.println("테스트가 종료되기 전 단 한번만 실행 됩니다.");
    }

}
