package type;

import org.junit.Test;

/**
 * 一些基本数据类型和包装类的tips
 * */
public class BaseData {
    /**
     * 包装类都有Cache 此处以Integer为例(-128 ~ 127)
     * @see Integer.IntegerCache
     * new的对象依旧引用不同，但直接赋值包装的对象是有缓存的
     * 只要涉及到基本数据类型的比较便成了单纯值的比较,因为对Integer对象做了拆箱操作，即intVlaue()
     * */
    @Test
    public void testCaches(){
        Integer i=new Integer(10);
        Integer i2=10;
        Integer i3=10;
        //即使是long比较结果也是true的
        long long1=10L;
        System.out.println("i==i2?"+(i==i2)+"  i2==i3?"+(i2==i3)+"  i==long1?"+(i==long1));
        i=new Integer(128);
        i2=128;
        i3=128;
        //不像String，这里如果不在缓存区间会新生成Integer对象
        Integer i4=Integer.valueOf(i);
        System.out.println("i==i2?"+(i==i2)+"  i==i3?"+(i2==i3)+" i==i4?"+(i==i4));
    }

    /**
     * 查看各种基本类型（包装类）的占用字节数
     * 首先需要说明：1字节 = 8bit，即8位二进制，所以两位16进制便是1字节
     *
     * */
    @Test
    public void testDataByteLength(){
        //long
        Long aLong = Long.MAX_VALUE;
        int count=0;
        for(;aLong>0;aLong=aLong>>1){
            count++;
        }//包含1个符号位
        System.out.println("Long bit数："+(count+1)+"，字节数: "+(count+1)/8);


        //integer
        Integer integer= Integer.MAX_VALUE;
        integer.toHexString(12);
        count=0;
        for(;integer>0;integer=integer>>1){
            count++;
        }
        System.out.println("Integer bit数："+(count+1)+"，字节数: "+(count+1)/8);


        //short 进行位操作前需转为int 或long
        Short aShort = Short.MAX_VALUE;
        aLong = new Long(aShort);
        count=0;
        for(;aLong>0;aLong=aLong>>1){
            count++;
        }
        System.out.println("Short bit数："+(count+1)+"，字节数: "+(count+1)/8);

        //char
        Character character=Character.MAX_VALUE;
        aLong = new Long(character);
        count=0;
        for(;aLong>0;aLong=aLong>>1){
            count++;
        }
        //Character无符号
        System.out.println("Character bit数："+(count)+"，字节数: "+(count)/8);

        //byte
        Byte aByte=Byte.MAX_VALUE;
        aLong = new Long(aByte);
        count=0;
        for(;aLong>0;aLong=aLong>>1){
            count++;
        }
        //Character无符号
        System.out.println("Byte bit数："+(count+1)+"，字节数: "+(count+1)/8);

        //浮点运算太复杂，直接展示
        System.out.println("Float bit数："+Float.SIZE+"，字节数: "+(Float.SIZE)/8);
        System.out.println("Double bit数："+Double.SIZE+"，字节数: "+(Double.SIZE)/8);
//        Long bit数：64，字节数: 8
//        Integer bit数：32，字节数: 4
//        Short bit数：16，字节数: 2
//        Character bit数：16，字节数: 2
//        Byte bit数：8，字节数: 1
//        Float bit数：32，字节数: 4
//        Double bit数：64，字节数: 8
    }


}
