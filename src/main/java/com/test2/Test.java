package com.test2;

@MyAnnotation
public class Test {
    @MyAnnotation
    private int age;

    @MyAnnotation
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
