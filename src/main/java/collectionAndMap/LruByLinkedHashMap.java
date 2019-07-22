package collectionAndMap;


import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 用LinkedHashMap实现简单的lru
 * 但是因为LinkedHashMap的物理存储与遍历顺序实际上是不相同的，所以此方式实现的lru不能做到最后访问的元素
 * */
public class LruByLinkedHashMap {


    @Test
    public void testLru(){
        Map<String,Integer> cache = new LruCache<>(10,1);
        
        cache.put("key1",1);
        cache.put("key2",2);
        cache.put("key3",3);
        cache.put("key4",4);
        cache.put("key5",5);
        cache.put("key6",6);
        cache.put("key7",7);
        cache.put("key8",8);
        cache.put("key9",9);
        cache.put("key10",10);
        System.out.println(cache);
        cache.get("key5");
        cache.put("car11",11);
        System.out.println(cache);

    }


    class LruCache<K,V> extends LinkedHashMap<K, V> {

        int capacity;
        LruCache(int initialCapacity, float loadFactor){
            super(initialCapacity,loadFactor,true);
            this.capacity=initialCapacity;
        }
        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            // 当元素个数大于了缓存的容量, 就移除元素
            return size()>this.capacity;
        }
    }
}


