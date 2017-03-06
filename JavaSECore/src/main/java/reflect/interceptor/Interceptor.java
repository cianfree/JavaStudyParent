package reflect.interceptor;

/**
 * 拦截器类：目标类中的方法被拦截之后，执行拦截器中的方法。
 */
public interface Interceptor {

    void before();

    void after();
}
