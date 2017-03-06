#20160814
    利用java反射机制实现拦截器
    
    
1 目标类接口：目标类要实现的接口。

    public interface ITarget {
        String sayHi(String name);
        int sum(int... nums);
    }
2 目标类：目标类是要被拦截的类。它实现了目标类接口。

    public class Target implements ITarget {
        @Override
        public String sayHi(String name) {
            System.out.println("正在执行 ： reflect.interceptor.Target.sayHi()");
            return "Hi, " + name;
        }
    
        @Override
        public int sum(int... nums) {
            System.out.println("正在执行 ： reflect.interceptor.Target.sum()");
            if (null != nums && nums.length < 1) return 0;
            int sum = 0;
            for (int num : nums) {
                sum += num;
            }
            return sum;
        }
    }
3 拦截器接口：目标类中的方法被拦截之后，执行拦截器中的方法。

    public interface Interceptor {
        void before();
        void after();
    }

4 处理器类：正是处理器把拦截器和目标类耦合在一起。

    public class InterceptorHandler implements InvocationHandler {
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
5 代理帮助类：程序执行时真正执行的是代理类，代理类是实现了拦截器的流程的类。

    public class ProxyHelper {
    
        /**
         * 创建代理
         */
        public static <T> T createProxy(Object target, Class<T> returnType, Interceptor interceptor) {
            Object proxy = Proxy.newProxyInstance(//
                    target.getClass().getClassLoader(),//
                    target.getClass().getInterfaces(),//
                    new InterceptorHandler(target, interceptor));
            return returnType.cast(proxy);
    
        }
    }
6 测试类：用来测试拦截器成功与否。

    public class InterceptorTest {
    
        public static void main(String[] args) {
            ITarget targetProxy = ProxyHelper.createProxy(new Target(), ITarget.class, new InfoInterceptor());
            System.out.println(targetProxy.sayHi("Arvin"));
            System.out.println("Sum(1,2,3,4,5) = " + targetProxy.sum(1, 2, 3, 4, 5));
        }
    
    }
    
    输出：
    被代理的类： class reflect.interceptor.Target
    拦截器： 执行方法之前进行拦截， before......
    正在执行 ： reflect.interceptor.Target.sayHi()
    拦截器： 执行方法之后进行拦截， after......
    Hi, Arvin
    被代理的类： class reflect.interceptor.Target
    拦截器： 执行方法之前进行拦截， before......
    正在执行 ： reflect.interceptor.Target.sum()
    拦截器： 执行方法之后进行拦截， after......
    Sum(1,2,3,4,5) = 15
注：认真学习java反射机制，这很重要。