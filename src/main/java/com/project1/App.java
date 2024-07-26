package com.project1;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class App {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        List<Integer> list = new ArrayList<>();
        Collections.addAll(list, 3,2,1,4);

        trackerOptions(list, Order.class);
    }

    private static void trackerOptions(List<Integer> list, Class<Order> orderClass) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Order o = orderClass.newInstance();
        //利用反射取出类
        Method[] ms = orderClass.getDeclaredMethods();//不包括继承的
        for (Integer ordermethodid : list) {
            for (Method m : ms) {
                Step step = m.getAnnotation(Step.class);
                if (step!=null){
                    //取出注解的参数
                    int id = step.id();
                    if (ordermethodid.equals(id)){
                        String description = step.description();
                        System.out.println(id+" "+description);
                        m.invoke(o);
                    }
                }
            }
        }
    }
}
