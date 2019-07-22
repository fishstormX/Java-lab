package collectionAndMap;


import org.junit.Test;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Map相关的内容
 *
 * */
public class MapT {

    static final void buildMap(Map map){
        map.put("kile",MapT.getInitialIndex("kile"));
        map.put("Carlon",MapT.getInitialIndex("Carlon"));
        map.put("Lilei",MapT.getInitialIndex("Lilei"));
        map.put("Tom",MapT.getInitialIndex("Tom"));
        map.put("carson",MapT.getInitialIndex("carson"));
        map.put("Jack",MapT.getInitialIndex("Jack"));
    }
    static final void buildMap2(Map map){
        map.put("kile2",MapT.getInitialIndex("kile"));
        map.put("Carlon2",MapT.getInitialIndex("Carlon"));
        map.put("Lilei2",MapT.getInitialIndex("Lilei"));
        map.put("Tom2",MapT.getInitialIndex("Tom"));
        map.put("carson2",MapT.getInitialIndex("carson"));
        map.put("Jack2",MapT.getInitialIndex("Jack"));
    }
    /**
     * 反射获取私有成员变量的值
     */
    static final Object getPrivateField(Object instance, String filedName) throws NoSuchFieldException, IllegalAccessException {
        Field field = instance.getClass().getDeclaredField(filedName);
        field.setAccessible(true);
        return field.get(instance);
    }
    /**
     * 引用HashMap的hash方法
     * */
    static final int hash(Object key) {
        int h=(key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
        return h;
    }
    /**
     * 获取HashMap key对应的Bucket序号
     * */
    static final int getInitialIndex(Object key) {
        int h = key.hashCode();
        h = (key == null) ? 0 : (h) ^ (h >>> 16);
        h = (16 - 1) & h;
        return h;
    }
    /**
     * 使用迭代器输出map的元素
     * */
    static final void printEntrys(Map map){
        try {
            Iterator<Map.Entry> i = map.entrySet().iterator();
            while (i.hasNext()) {
                Map.Entry e = i.next();
                Object key = e.getKey();
                Object value = e.getValue();
                System.out.print(key+":"+value+"  ");
            }
            System.out.println();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 使用反射输出map的属性
     * */
    static final void printAttrs(Map map){
        try {
            Object[] o =(Object [])getPrivateField(map,"table");
            System.out.println("---------------- map.size："+map.size()+
                    "   threshold："+getPrivateField(map,"threshold")+
                    "   loadFactor："+getPrivateField(map,"loadFactor")+
                    "   modCount："+getPrivateField(map,"modCount")+
                    "   captain："+o.length

            );
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 探究HashMap的resize时机以及扩容量，为便于观察 使用了有输出信息重写过的HashMap
     * 1.扩容时机threshold取决loadFactory*captain，向下取整,在进行插入操作前如果size=threshold，进行扩容
     * 2.扩容的操作其实是将bucket数组数量翻倍
     * 3.captain只会是2的n次幂，为了便于位运算，依据定义的initialCapacity向上取2^n
     * 4.默认值 capacity-16 loadFactory-0.75f 即threshold-12
     * */
    @Test
    public void testHashMapResize()  {
        HashMap<String ,Integer> map = new HashMap<>();
        //添加元素
        for(int i=0;i<100;i++) {
            map.put("S"+i,i);
            printAttrs(map);
        }
        //移除元素
        for(int i=0;i<100;i++) {
            map.remove("S"+i);
            printAttrs(map);
        }
    }
    /**
     * HashMap的Equals方法是调用迭代器调用了每一个元素key，
     * 在size相等的条件下，map.get(key)与所遍历key的value equals结果均为true即为true
     * 所以Map的比较是没有顺序(即使是LinkedHashMap)、不分类型(即使是hashMap与TreeMap)的
     * 顺序性不是Map的特性，即使使用LinkedHashMap保证了迭代顺序，实际它的存储结构并没有改变
     * 只要key与value列表的组合一致，Map的equals将永远返回true
     * */
    @Test
    public void testMapEquals()  {
        Map<String ,Integer> map = new TreeMap<>();
        buildMap(map);
        //调整了map的顺序，尤其调整了一个bucket的Tom与Lilei，使其迭代顺序不同了
        Map<String ,Integer> map2 = new LinkedHashMap<>();
        buildMap(map2);
        MapT.printEntrys(map);
        MapT.printEntrys(map2);
        System.out.println("---------------- is map equals map2?"+map.equals(map2));
    }
    /***
     * fail-fast
     * */
    @Test
    public void testIteratorExceptions() throws NoSuchFieldException, IllegalAccessException {
        Map<String,Integer> map=new HashMap<>();
        buildMap(map);
        System.out.println("迭代前：" + map);
        System.out.print("foreach :");

        for (Map.Entry entry: map.entrySet()) {
            System.out.print(entry.getKey()+":"+entry.getValue() + " ,");
            //括号中记录这一步的modCount
            System.out.print("("+getPrivateField(map,"modCount")+")");
            //foreach迭代时不能添加删除元素(改变modCount计数)，否则一律会抛出ConcurrentModificationException
            //map.put("newKey",120);
            //map.remove("Lilei");
            if (entry.getKey().equals("Tom")) {
                //但是改变数值可以
                map.put("kile", 0);
            }
        }
    }

    /***
     * 试想，如果迭代时直接改变map指向，会发生什么
     * */
    @Test
    public void testIteratorExceptions2() throws NoSuchFieldException, IllegalAccessException {
        Map<String,Integer> map=new HashMap<>();
        buildMap(map);
        System.out.println("迭代前：" + map);
        System.out.print("foreach :");

        for (Map.Entry entry: map.entrySet()) {
            System.out.print(entry.getKey()+":"+entry.getValue() + " ,");
            System.out.print("("+getPrivateField(map,"modCount")+")");

            if (entry.getKey().equals("Tom")) {
                //此处改变了map的指向，但其实EntrySet作为之前map的子类迭代的仍是之前的map，之前map的modCount也依旧是6
               map=new HashMap<>();
               buildMap2(map);
               map.put("newKey",0);
            }
        }
    }

    /**
     * 测试HashMap与Concurrent在并发下的区别
     * 使用HashMap会出现线程安全问题，高度并发时甚至可能会造成扩容时死循环，所以有时可能会卡死
     * 在确认1000个线程执行完毕后，清点map.size
     * 使用ConcurrentHashMap的size总为1000000，但HashMap基本每次结果都不同且基本不会是我们预想的数字
     * */
    volatile Integer p=0;
    @Test
    public void testConcurrentSafe(){
        //于此切换不同的HashMap
        //Map<String,String> map=new HashMap<>();
        Map<String,String> map=new ConcurrentHashMap<>();
        for(int i=0;i<1000;i++){
            Thread thread = new MapThread1(map,i*1000);
            thread.start();
        }
        try {
            Thread.sleep(5000);
            //确认p为1000保证所有线程执行完毕，不过p的改变也有很小概率发生线程不安全问题，可以多试几次
            //加了锁 没问题了
            System.out.println(p+"个线程执行完毕，map.size():"+map.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class MapThread1 extends Thread {
        Map<String,String> map;
        int n;
        MapThread1(Map map,int n){
            this.map=map;
            this.n=n;
        }
        @Override
        public void run() {
            for(Integer j=0+n;j<1000+n;j++) {
                map.put(j.toString() , "");
            }
            synchronized ("a"){
                p++;
            }
            System.out.println("线程执行完毕"+map.size());
        }
    }
}
