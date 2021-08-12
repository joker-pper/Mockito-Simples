package com.joker17.mockito.simples;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

/**
 * 需要引入mockito-inline
 */
public class StaticMethodTest {

    /**
     * mock静态对象,当前线程只能存在一个且只对当前线程生效
     */
    private static MockedStatic<StaticUtils> mockedStaticUtils;

    @BeforeClass
    public static void before() {
        //执行mock前的test
        beforeMockTest();

        //获取mock静态对象
        mockedStaticUtils = Mockito.mockStatic(StaticUtils.class);
    }

    /**
     * 验证未mock前的test
     */
    private static void beforeMockTest() {

        Assert.assertEquals("generate-aaa", StaticUtils.generate("aaa"));

        Assert.assertEquals("generate", StaticUtils.generate());

        boolean beforeStatus = StaticUtils.getStatus();
        StaticUtils.changeStatus();
        boolean afterStatus = StaticUtils.getStatus();
        Assert.assertNotEquals(beforeStatus, afterStatus);
    }

    /**
     * 测试含参数的静态方法(thenReturn)
     */
    @Test
    public void testHasArgWithReturn() throws InterruptedException {
        mockedStaticUtils.when(() -> StaticUtils.generate(Mockito.any())).thenReturn("hello");
        Assert.assertEquals("hello", StaticUtils.generate("aaa"));
    }

    /**
     * 测试含参数的静态方法(thenAnswer)
     */
    @Test
    public void testHasArgWithAnswer() {
        mockedStaticUtils.when(() -> StaticUtils.generate(Mockito.any())).thenAnswer(
                (Answer) invocation -> RandomStringUtils.randomAlphanumeric(8));
        Assert.assertNotEquals("generate-aaa", StaticUtils.generate("aaa"));
    }

    /**
     * 测试不含参数的静态方法(thenReturn)
     */
    @Test
    public void testHasNoArgArgWithReturn() {
        mockedStaticUtils.when(() -> StaticUtils.generate()).thenReturn("hello");
        Assert.assertEquals("hello", StaticUtils.generate());
    }

    /**
     * 测试不含参数的静态方法(thenAnswer)
     */
    @Test
    public void testHasNoArgWithAnswer() {
        mockedStaticUtils.when(() -> StaticUtils.generate()).thenAnswer(
                (Answer) invocation -> RandomStringUtils.randomAlphanumeric(8));
        Assert.assertNotEquals("generate", StaticUtils.generate());
    }

    /**
     * 测试静态void方法(mock对象后默认不会执行真实方法)
     */
    @Test
    public void testVoidMethod() {
        boolean beforeStatus = StaticUtils.getStatus();
        StaticUtils.changeStatus();
        boolean afterStatus = StaticUtils.getStatus();
        Assert.assertEquals(beforeStatus, afterStatus);
    }


    static class StaticUtils {

        private static boolean STATUS = true;

        public static String generate(String args) {
            return "generate-" + args;
        }

        public static String generate() {
            return "generate";
        }

        public static void changeStatus() {
            STATUS = !STATUS;
        }

        /**
         * 用于获取void方法变更的结果值
         *
         * @return
         */
        public static boolean getStatus() {
            return STATUS;
        }

    }

}
