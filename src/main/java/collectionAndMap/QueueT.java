package collectionAndMap;

import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

public class QueueT {
    @Test
    public void arrQueue(){
        ArrayBlockingQueue<String> queue= new ArrayBlockingQueue<>(200);
        System.out.println(queue.size());
        queue.add("asd");
        queue.add("asd");
        queue.add("asd");
        System.out.println(queue.size());
    }

    @Test
    public void arrBlockingQueue2(){
        ArrayBlockingQueue<String> queue= new ArrayBlockingQueue<>(200);
        final CountDownLatch latch = new CountDownLatch(2);
        Thread t1=new Thread(
                ()->    {
                    String s="";
                    for(int i=0;i<10;i++) {

                    }

                }
        );
        Thread t2=new Thread(
                ()->    {
                    for(Integer i=0;i<10;i++) {
                        try {
                            synchronized ("c"){
                                queue.put(i.toString());
                                System.out.println("阻塞put:"+i+" "+System.currentTimeMillis());
                                Thread.sleep(1000);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 测试阻塞队列ArrayBlockingQueue
     * */
    @Test
    public void arrBlockingQueue() {
        ArrayBlockingQueue<String> queue= new ArrayBlockingQueue<>(200);
         CountDownLatch latch = new CountDownLatch(2);
        System.out.print(latch.getCount());
       Thread t1=new Thread(
               ()->    {
                   for(int i=0;i<10;i++) {
                       try {
                           System.out.println("阻塞take:"+queue.take()+" "+System.currentTimeMillis());
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                   }
                   latch.countDown();
               }
       );
        Thread t2=new Thread(
                ()->    {
                    for(Integer i=0;i<10;i++) {
                        try {
                            synchronized ("c"){
                            queue.put(i.toString());
                            System.out.println("阻塞put:"+i+" "+System.currentTimeMillis());
                            Thread.sleep(1000);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    latch.countDown();
                }
        );
        try {
            t1.start();
            t2.start();
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
