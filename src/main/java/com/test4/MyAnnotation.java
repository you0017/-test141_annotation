package com.test4;

//带默认值的注解
public @interface MyAnnotation {
    public String sayHello() default "zy";
}
