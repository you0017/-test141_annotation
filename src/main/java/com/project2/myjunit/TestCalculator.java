package com.project2.myjunit;

import com.project2.myjunit.annotations.*;
import com.project2.testJunit4.Calculation;
import org.apache.log4j.Logger;

public class TestCalculator {
    private static Logger log = Logger.getLogger(TestCalculator.class);
    static Calculation js;

    public TestCalculator() {
        log.info("CalculationTest()");
        js = new Calculation();
    }

    @MyBeforeClass
    public static void beforeClass(){
        log.info("beforeClass()");
    }

    @MyBeforeClass
    public static void beforeClass2(){
        log.info("beforeClass2()");
    }

    @MyBefore
    public void before(){
        log.info("before()");
    }
    @MyBefore
    public void before2(){
        log.info("before2()");
    }


    @MyTest
    public void testAdd() {
        //Assert.assertEquals(7,js.add(3,4));
        log.info("testAdd");
    }

    @MyTest
    public void testSub() {
        //Assert.assertEquals(1,js.sub(3,2));
        log.info("testSub");
    }

    @MyAfter
    public void after(){
        log.info("after()");
    }

    @MyAfterClass
    public static void afterClass(){
        log.info("afterClass()");
    }
}
