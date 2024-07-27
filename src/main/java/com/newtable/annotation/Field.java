package com.newtable.annotation;



import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Field {
    public String value();//可以指定属性名 //不写就自己原名
    public String foreignKey() default "";//外键  格式是  表明-键
}
