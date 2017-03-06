package reflect;

/**
 * <pre>
 * 获取Class的几种方式：假设有类：edu.zhku.cian.User
 * （1）直接使用类名类获取
 *      Class<User> clazz = User.class;
 *      这种方式用一种缺陷，就是它不会执行静态代码块，别的方式都会执行静态的代码块
 *
 * （2）使用Class.forName获取, 这是最常用的
 *      Class<User> clazz = Class.forName("edu.zhku.cian.User");
 *      会抛出ClassNotFoundException
 *
 * （3）通过对象的getClass()方法来获取
 *      User user = new User();
 *      Class<User> clazz = user.getClass();
 *      注意：反射只能是反映静态的字节码信息，是无法获得运行时信息的，例如泛型，由于字节码是去类型化的，所以在运行时才能动态获得泛型类型信息。
 *
 * </pre>
 * Created by Arvin on 2016/4/25.
 */
public class GetClassTest {

    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println("采用第一种方式加载Class：");
        Class<GetClass> clazz = GetClass.class;

        System.out.println("采用第三种方式加载Class：");
        GetClass obj = new GetClass();
        clazz = (Class<GetClass>) obj.getClass();

        System.out.println("采用第二种方式加载Class：");
        clazz = (Class<GetClass>) Class.forName("GetClass");

    }
}

class GetClass {

    private static final int count;

    static {
        count = 1;
        System.out.println("静态语句执行了！");
    }
}
