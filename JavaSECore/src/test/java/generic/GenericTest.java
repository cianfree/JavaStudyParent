package generic;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     泛型测试
 * 作用：
 * 使之类型安全，把运行时的错误提前到编译期间，让错误提前暴露。
 *  关于<? extends E>	? 表示通配符，表示对于所有的，继承自E的类型或者是E的类型（向下限定）
 *  关于<? super E> ? 表示传入参数必须是E，或者是E的父类型（向上限定）
 * 泛型方法
 * public <T> T query(Class<T> clazz) {
 *  // 在这里，返回的类型参数是根据传入的参数来决定的
 *  // 也就是说，根据clazz的泛型参数来确定的
 * }
 * 使用泛型方法的时候，必须在参数列表中有泛型，否则无法判断返回的类型另外，只用引用类型才能作为泛型的参数。
 * 注意：泛型在编译成class文件之后是没有任何的泛型类型信息的。
 *
 * </pre>
 * <p>
 * Created by Arvin on 2016/4/25.
 */
public class GenericTest {

    public static void main(String[] args) {
        sayHi(new ArrayList<I1>());

        //sayHi2(new ArrayList<I3>());
        sayHi2(new ArrayList<I2>());
    }

    /**
     * I1 或继承 I1 的类型
     *
     * @param t
     */
    public static void sayHi(List<? extends I1> t) {
        System.out.println("HI, " + t);
    }

    public static void sayHi2(List<? super I2> t) {
        System.out.println("SayHi2: " + t);
    }
}

/**
 * 定义接口
 */
interface I1 {

}

interface I2 extends I1 {

}

interface I3 extends I2 {

}


/**
 * 类型只能限定是 T
 *
 * @param <T>
 */
class G1<T> {

}

