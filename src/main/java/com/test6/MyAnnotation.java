package com.test6;

import lombok.Builder;
import lombok.Data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//Target表明注解的作用目标
//ElementType是一个枚举
//Field 表示该注解可以放在属性上
//method 表示该注解可以放在方法上

@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.CLASS)//字节码有作用
public @interface MyAnnotation {
}

@Data
class A{
    @MyAnnotation
    private String name;
    @MyAnnotation
    public String show(String a){
        return null;
    }
}
