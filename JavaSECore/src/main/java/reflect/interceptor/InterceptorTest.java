package reflect.interceptor;

/**
 * 测试拦截器
 */
public class InterceptorTest {


    public static void main(String[] args) {
        ITarget targetProxy = ProxyHelper.createProxy(new Target(), ITarget.class, new InfoInterceptor());

        System.out.println(targetProxy.sayHi("Arvin"));

        System.out.println("Sum(1,2,3,4,5) = " + targetProxy.sum(1, 2, 3, 4, 5));

    }

}
