package reflect.interceptor;

/**
 * 目标类：目标类是要被拦截的类。它实现了目标类接口。
 */
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
