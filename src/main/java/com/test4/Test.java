package com.test4;

import com.test4.MyAnnotation;

@MyAnnotation
public class Test {
    @MyAnnotation
    public static void main(String[] args) {
        System.out.println("hello world");
    }
}
