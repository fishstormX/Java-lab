package test;

public class Outer {
    private String s="";

    String getS() {
        return s;
    }

     void setS(String s) {
        this.s = s;
    }

     Outer(String s) {
        this.s = s;
    }

     Inner getInner(){
        return new Inner();
    }
    private class Inner{
         void printParam(){
            System.out.println(s);
        }

    }

    /**
     * 内部类有一个对外部类的指向
     * */
    public static void main(String[] args){
        Outer outer=new Outer("1");
        Inner inner=outer.getInner();
        inner.printParam();
        outer=new Outer("2");
        inner.printParam();
        inner=outer.getInner();
        inner.printParam();
    }
}
