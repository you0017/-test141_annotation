package com.test3;

@MyAnnotation(sayHello = "zy")
public class Test {
    @MyAnnotation(sayHello = "zy")
    public static void main(String[] args) {
        System.out.println("hello world");
    }
}
