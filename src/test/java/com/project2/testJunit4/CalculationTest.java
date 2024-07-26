package com.project2.testJunit4;

import org.apache.log4j.Logger;
import org.junit.*;


/**
 * 单元测试类
 *  junit的测试的生命周期
 */
public class CalculationTest {
    private static Logger log = Logger.getLogger(CalculationTest.class);
    static Calculation js;

    public CalculationTest() {
        log.info("CalculationTest()");
        js = new Calculation();
        System.out.println("CalculationTest");
    }

    @BeforeClass
    public static void beforeClass(){
        log.info("beforeClass()");
        js = new Calculation();
        System.out.println("beforeClass");
    }

    @BeforeClass
    public static void beforeClass2(){
        log.info("beforeClass2()");
        js = new Calculation();
        System.out.println("beforeClass2");
    }

    @Before
    public void before(){
        log.info("before()");
        js = new Calculation();
        System.out.println("before");
    }
    @Before
    public void before2(){
        log.info("before2()");
        js = new Calculation();
        System.out.println("before2");
    }


    @Test
    public void testAdd() {
        Assert.assertEquals(7,js.add(3,4));
        System.out.println("testAdd");
    }

    @Test
    public void testSub() {
        Assert.assertEquals(1,js.sub(3,2));
        System.out.println("testSub");
    }

    @After
    public void after(){
        log.info("after()");
        js = new Calculation();
        System.out.println("after");
    }

    @AfterClass
    public static void afterClass(){
        log.info("afterClass()");
        js = new Calculation();
        System.out.println("afterClass");
    }
}//只执行一次 @BeforeClass()   ->构造方法->@Before->@Test -> @After ->     @AfterClass
            //                  后面是@Test几次就调几次                      这个只运行一次