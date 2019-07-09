package collections;

import org.junit.Test;


import java.util.*;

public class ListT {
    private List<String> list= new ArrayList<>();


    /**
     *  在集合foreach迭代中使用另一个线程改变集合指向，迭代的集合仍是旧值
     *  因为迭代器中留存了list的副本 使用iterator时与原list无关
     *  @see  ArrayList.Itr#next()
     *  @see  test.Outer
     * */
    @Test
    public void testMapEquals() {
        for(Integer i=0;i<1000000;i++){
            list.add(i.toString());
        }
        Thread2 t=new Thread2();
        t.setDaemon(true);
        t.start();
        System.out.println("asd");
        int count=0;
        for(String str :list){
            if(count<100000&&count%10==0)
                System.out.println(str+" "+list.size());
            if(count==50000) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
            count++;
        }
        System.out.print("+"+count);


    }


    class Thread2 extends Thread{
        @Override
        public void run(){

            while(true){
                List<String> newList= new ArrayList<>(2);
                for(Double i=0.5;i<100000;i++){
                    newList.add(i.toString());
                }
                System.out.println("-");
                list=newList;
            }
        }
    }
}
