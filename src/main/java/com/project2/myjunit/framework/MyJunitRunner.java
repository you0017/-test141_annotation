package com.project2.myjunit.framework;

import com.project2.myjunit.annotations.*;
import org.apache.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 读取单元测试类，以反射方式获取测试类中的所有方法，区分注解
 * 以对应的顺序来调用即可
 */
public class MyJunitRunner {
    private static Logger log = Logger.getLogger(MyJunitRunner.class);
    public static void run(Class cls) {
        List<Method> beforeClassMethods = new ArrayList<>();
        List<Method> beforeMethods = new ArrayList<>();
        List<Method> afterClassMethods = new ArrayList<>();
        List<Method> afterMethods = new ArrayList<>();
        List<Method> ignoreMethods = new ArrayList<>();
        List<Method> testMethods = new ArrayList<>();

        Method[] ms = cls.getDeclaredMethods();
        for (Method m : ms) {
            Annotation[] ans = m.getAnnotations();
            for (Annotation an : ans) {
                log.info("当前方法为："+m.getName()+",注解为:"+an.toString());
                if (an instanceof MyBeforeClass){
                    beforeClassMethods.add(m);
                }else if (an instanceof MyBefore){
                    beforeMethods.add(m);
                }else if (an instanceof MyAfter){
                    afterMethods.add(m);
                }else if (an instanceof MyAfterClass){
                    afterClassMethods.add(m);
                }else if (an instanceof MyIgnore){
                    ignoreMethods.add(m);
                }else if (an instanceof MyTest){
                    testMethods.add(m);
                }
            }
        }

        try {
            //以上完成了单元测试类的解析
            //按生命周期顺序调用
            //1.调用@MyBeforeClass
            for (Method m : beforeClassMethods) {
                m.invoke(null);
            }

            for (Method test : testMethods) {
                Object o = cls.newInstance();
                for (Method m : beforeMethods) {
                    m.invoke(o);
                }
                test.invoke(o);
                for (Method m : afterMethods) {
                    m.invoke(o);
                }
            }

            for (Method m : afterClassMethods) {
                m.invoke(null);
            }
        }catch (Exception e){
            log.error("执行单元测试出错",e);
        }

    }
}
