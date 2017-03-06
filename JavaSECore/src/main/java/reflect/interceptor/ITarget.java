package reflect.interceptor;

/**
 * 定义目标接口，需要被拦截的
 */
public interface ITarget {

    /**
     * Say Hi
     *
     * @param name
     * @return
     */
    String sayHi(String name);


    /**
     * 计算总和
     *
     * @param nums
     * @return
     */
    int sum(int... nums);
}
