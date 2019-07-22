package exception;

import org.junit.Test;

public class TestFinallyReturn {
    @Test
    public void testCase1(){
        System.out.print(testFinallyReturn());
    }

    public int testFinallyReturn(){
        int x=0;
        try {
            x=10;
            System.out.print(1/0);
            return x;
        }catch(Exception e){
            x=20;
            return x;
        }finally {
            x=40;
        }
    }
}
