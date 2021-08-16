package com.joker17.mockito.simples;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class CallRealMethodTest {

    abstract class AbstractClass {

        public String getResult() {
            return "default";
        }

        public String getResult(String text) {
            return text;
        }

    }


    @Test
    public void testHasNoArg() {
        AbstractClass mock = Mockito.mock(AbstractClass.class);

        //mock后默认为null
        Assert.assertEquals(null, mock.getResult());

        //mock指定返回结果
        Mockito.when(mock.getResult()).thenReturn("haha");
        Assert.assertEquals("haha", mock.getResult());

        //设置执行对象实际的方法
        Mockito.doCallRealMethod().when(mock).getResult();
        Assert.assertEquals("default", mock.getResult());
    }


    @Test
    public void testHasArg() {
        AbstractClass mock = Mockito.mock(AbstractClass.class);

        String text = "123";

        //mock后默认为null
        Assert.assertEquals(null, mock.getResult(text));

        //mock指定返回结果
        Mockito.when(mock.getResult(Mockito.anyString())).thenReturn("haha");
        Assert.assertEquals("haha", mock.getResult(text));

        //设置执行对象实际的方法
        Mockito.doCallRealMethod().when(mock).getResult(Mockito.anyString());
        Assert.assertEquals(text, mock.getResult(text));
    }
}
