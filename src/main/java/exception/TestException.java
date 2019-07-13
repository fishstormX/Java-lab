package exception;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


/**
 * 一些常见的RunTimeException的产生
 *
 * */
public class TestException {

    @Test
    public void testStackOverFlowError(){
       String s="Hello,Carson";
       testStackOverFlowError();
        Object obj=new Object();
    }

    @Test
    public void testCla1ssCastException(){

        System.out.println(Long.MAX_VALUE+1);
    }

    @Test
    public void testClassCastException(){
        Object obj=new Object();
        String str=(String)obj;
    }

    @Test
    public void testOutOfMemoryError(){
        List<String> list =new ArrayList<>();
        while(true){
           list.add("Hello,John");
       }

    }

    @Test
    public void testOutOfMemoryError2(){
        String s="OMD";
        while(true){
            s=s+s;
        }
    }

    @Test
    public void testNullPointerException(){
        TestException testException=null;
        testException.method();
    }
    public void method(){
        System.out.println("hello");
    }

    @Test
    public void testNumberFormatException(){
       Integer.parseInt("一");
    }

    @Test
    public void testArithmeticException(){
        int c=5/0;
    }
}
