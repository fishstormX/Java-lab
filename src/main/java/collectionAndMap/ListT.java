package collectionAndMap;

import org.junit.Test;


import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * List的相关内容
 * */
public class ListT {
    private List<String> list= new ArrayList<>();
    /**
     * 反射获取私有成员变量的值
     */
    static final Object getPrivateField(Object instance, String filedName) throws NoSuchFieldException, IllegalAccessException {
        Field field = instance.getClass().getDeclaredField(filedName);
        field.setAccessible(true);
        return field.get(instance);
    }
    /***
     * 为list随便添加点元素
     */
    private void buildList(List i){
       buildList(i,5);
    }
    /***
     * 为list随便添加指定长度的点元素
     */
    private void buildList(List i,int n){
        for(Integer j=0;j<n;j++){
            i.add(j.toString());
        }
    }

    /**
     * 测试list迭代的效率
     * ArrayList LinkedList for 和foreach
     * Linked不能随机访问，使用for循环的时间复杂度为O(n^2/2)
     * 其余为O(n)
     * */
    @Test
    public void testIterEfficiency(){
        ArrayList<String> arrayList=new ArrayList<>();
        buildList(arrayList,100000);
        long timeline=System.currentTimeMillis();
        for(int i=0;i<arrayList.size();i++){
            System.out.print(arrayList.get(i));
        }
        long timeline2=System.currentTimeMillis();
        System.out.println("\n"+"arrayList for："+(timeline2-timeline));
        timeline=System.currentTimeMillis();
        for(String s:arrayList){
            System.out.print(s);
        }
        timeline2=System.currentTimeMillis();
        System.out.println("\n"+"arrayList foreach："+(timeline2-timeline));

        LinkedList<String> linkedList=new LinkedList<>();
        buildList(linkedList,100000);
        timeline=System.currentTimeMillis();
        for(int i=0;i<linkedList.size();i++){
            System.out.print(linkedList.get(i));
        }
        timeline2=System.currentTimeMillis();
        System.out.println("\n"+"linkedList for："+(timeline2-timeline));
        timeline=System.currentTimeMillis();
        for(String s:linkedList){
            System.out.print(s);
        }
        timeline2=System.currentTimeMillis();
        System.out.print("\n"+"linkedList foreach："+(timeline2-timeline));
    }

    @Test
    public void testIteratorExceptions(){
        buildList(list);
        System.out.println("迭代前："+list);
        System.out.print("foreach :");
        for(String str:list){
            System.out.print(str+" ,");
            //foreach迭代时不能添加删除元素(改变modCount计数)，否则一律会抛出ConcurrentModificationException
            //list.add("4");
            //list.remove(2)
            if(str.equals("3")){
                //但是改变数值可以
                list.set(3,"5");
            }
        }
        System.out.print("\niterator :");
        Iterator i=list.iterator();
        while(i.hasNext()){
            String s=i.next().toString();
            System.out.print(s+" ,");
            //是移除上一个，与当前位置无关，通过迭代器可以做到
           i.remove();
        }
        System.out.println("\niterator 结束后:"+list);

        //相当于Map中的ConcurrentHashMap
        list = new CopyOnWriteArrayList<>();
        buildList(list);
        System.out.println("CopyOnWriteArrayList 迭代前："+list);
        System.out.print("foreach :");
        for(String str:list){
            System.out.print(str+" ,");
            //foreach迭代时可以随便修改元素，只要不改变数组指向，因为迭代指向的是旧数组，
            // 否则一律会抛出ConcurrentModificationException
            if(str.equals("1")){
                list=new CopyOnWriteArrayList<>();
                buildList(list);

            }
        }
        System.out.println("CopyOnWriteArrayList 迭代前："+list);
        //fail-safe
        System.out.print("\niterator :");
         i=list.iterator();
        while(i.hasNext()){
            String s=i.next().toString();
            //同上
            if(s.equals("1")){
                //list.remove(2);
                list.add("8");
               /* //但是改变数值可以
                list.set(3,"5");*/
            }
            System.out.print(s+" ,");
            //考虑并发安全 迭代中不能通过iteratorre move，会报UnsupportedOperationException
            //i.remove();
        }
        System.out.println("\nCopyOnWriteArrayList iterator 结束后:"+list);
    }

    /**
     * list的扩容 扩容一半（+ >>）
     * */
    @Test
    public void testListResize(){
        try{
            List<Integer> list1=new ArrayList<>();
            int captain=0;
            for(int i=0;i<100;i++){
                Object[] os = (Object [])getPrivateField(list1,"elementData");

                if(os.length>captain){
                    System.out.println("默认初始容量的数组 size："+list1.size()+"   "+"  captain："+os.length);
                    captain=os.length;
                }
                list1.add(i);
            }
            list1=new ArrayList<>(8);
            captain=0;
            for(int i=0;i<100;i++){
                Object[] os = (Object [])getPrivateField(list1,"elementData");
                if(os.length>captain){
                    System.out.println("初始容量为8的数组 size："+list1.size()+"   "+"  captain："+os.length);
                    captain=os.length;
                }
                list1.add(i);
            }
        }catch(NoSuchFieldException e){

        }catch(IllegalAccessException e){

        }
    }
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
                ArrayList<String> newList= new ArrayList<>(2);
                for(Double i=0.5;i<100000;i++){
                    newList.add(i.toString());
                }
                System.out.println("-");
                list=newList;
            }
        }
    }
}
