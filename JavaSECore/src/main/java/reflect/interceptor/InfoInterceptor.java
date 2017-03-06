package reflect.interceptor;

/**
 * 提示拦截器实现
 */
public class InfoInterceptor implements Interceptor {

    @Override
    public void before() {
        System.out.println("拦截器： 执行方法之前进行拦截， before......");
    }

    @Override
    public void after() {
        System.out.println("拦截器： 执行方法之后进行拦截， after......");
    }
}
