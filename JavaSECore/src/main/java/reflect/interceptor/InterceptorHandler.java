package reflect.interceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 处理器类：正是处理器把拦截器和目标类耦合在一起。
 */
public class InterceptorHandler implements InvocationHandler {

    /**
     * 被代理的对象
     */
    private Object target;

    private Interceptor interceptor;

    public InterceptorHandler(Object target, Interceptor interceptor) {
        this.target = target;
        this.interceptor = interceptor;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("被代理的类： " + target.getClass());

        Object result;
        interceptor.before();

        result = method.invoke(target, args);
        interceptor.after();

        return result;
    }
}
