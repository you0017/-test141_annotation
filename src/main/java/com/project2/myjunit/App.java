package com.project2.myjunit;

import com.project2.myjunit.framework.MyJunitRunner;

public class App {
    public static void main(String[] args) {
        MyJunitRunner.run(TestCalculator.class);
    }
}
