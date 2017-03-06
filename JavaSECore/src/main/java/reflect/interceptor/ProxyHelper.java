package reflect.interceptor;

import java.lang.reflect.Proxy;

/**
 * 代理类：程序执行时真正执行的是代理类，代理类是实现了拦截器的流程的类。
 */
public class ProxyHelper {

    /**
     * 创建代理
     *
     * @param target
     * @param returnType
     * @param interceptor
     * @param <T>
     * @return
     */
    public static <T> T createProxy(Object target, Class<T> returnType, Interceptor interceptor) {
        Object proxy = Proxy.newProxyInstance(//
                target.getClass().getClassLoader(),//
                target.getClass().getInterfaces(),//
                new InterceptorHandler(target, interceptor));
        return returnType.cast(proxy);

    }
}
