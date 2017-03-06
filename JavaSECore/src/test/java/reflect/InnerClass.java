package reflect;

/**
 * Created by Arvin on 2016/4/26.
 */
public class InnerClass {

    static class Inner {
        static {
            System.out.println("加载了内部类： " + Inner.class);
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
        Class<Inner> clazz = (Class<Inner>) Class.forName("InnerClass.Inner");

        // 正确的写法
        //Class<Inner> clazz = (Class<Inner>) Class.forName("InnerClass$Inner");
        System.out.println(clazz);
    }
}
