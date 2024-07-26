package com.test5;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//Target表明注解的作用目标
//ElementType是一个枚举
//Field 表示该注解可以放在属性上
//method 表示该注解可以放在方法上

@Target({ElementType.TYPE,ElementType.METHOD,ElementType.FIELD,ElementType.PARAMETER})
public @interface MyAnnotation {
}

@MyAnnotation
class A{
    @MyAnnotation
    private String name;
    @MyAnnotation
    public String show(@MyAnnotation String a){
        return null;
    }
}
