package autoboxing;

/**
 * <pre>
 *     对于：Integer a = 128; Integer b = 128;
 *      在-127到128之间的话，a==b 是true,否则是false
 * </pre>
 * Created by Arvin on 2016/4/25.
 */
public class AutoBoxing {

    public static void main(String[] args) {
        Integer a = 128;
        Integer b = 128;
        System.out.println(a == b);    // false
        System.out.println(a.equals(b));    // true
        a = -128;
        b = -128;
        System.out.println(a == b);    // true
        a = -129;
        b = -129;
        System.out.println(a == b);    // false
        a = 127;
        b = 127;
        System.out.println(a == b);    // true
    }
}
