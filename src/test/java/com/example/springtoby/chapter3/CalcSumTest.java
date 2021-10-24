package com.example.springtoby.chapter3;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class CalcSumTest {

    @Test
    public void sumOfNumber() throws IOException {
        Calculator calculator = new Calculator();
        int sum = calculator.calcSum(getClass().getResource("numbers.txt").getPath());
        assertThat(sum).isEqualTo(10);
    }

}

class Calculator {
    public Integer calcSum(String filepath) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath));
        Integer sum = 0;
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            sum += Integer.parseInt(line);
        }
        bufferedReader.close();
        return sum;
    }
}