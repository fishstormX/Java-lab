package type;

import org.junit.Test;

import java.lang.reflect.Field;


/**
 * 有关String的tips
 * */
public class StringT {

    /**
     * 反射获取私有成员变量的值
     */
    static final Object getPrivateField(Object instance, String filedName) throws NoSuchFieldException, IllegalAccessException {
        Field field = instance.getClass().getDeclaredField(filedName);
        field.setAccessible(true);
        return field.get(instance);
    }


    /**
     *  常量池中任何的String都只留有一份
     *  但在堆中可不一定，直接通过="xx"创建的String会直接建立到常量池的引用
     *  而new String则是新建在堆中的对象，引用指向了常量池中的常量
     * */
    @Test
    public void test1() throws NoSuchFieldException, IllegalAccessException {
        //此时常量池有caror 堆中有a对象
        String a=new String ("caror");
        //此时常量池有caror 堆中有a、b对象
        String b=new String ("caror");
        //直接指向常量池
        String c="caror";
        String d="caror";
        //同上
        String e="car"+"or";
        //valueOf调用的就是toString，直接返回this
        String f=String.valueOf(a);
        System.out.println("a==b?"+(a==b)+"   a==c?"+(a==c)+"   c==d?"+(c==d)+"   c==e?"+(c==e)+"   c==f?"+(f==a));
    }
}
