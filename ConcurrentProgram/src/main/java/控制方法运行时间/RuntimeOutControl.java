package 控制方法运行时间;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * 方法运行限制运行时间
 *
 * @author Arvin
 * @time 2017/2/13 19:32
 */
public class RuntimeOutControl {
    //被测试方法
    public void task() throws InterruptedException {
        //睡眠1s
        Thread.sleep(1000);
        for (int i = 0; i < 100000; i++) {
            System.out.println(Thread.currentThread().getName() + " no.:" + i);
        }
        System.out.println("finished:test");
    }

    //测试
    public void test() throws Exception {
        //反射区方法
        Method method = getClass().getDeclaredMethod("task", null);
        //创建线程实例
        Callable call = new CallableTask(this, method, null);
        //使用FutureTask具备返回值的线程监控类
        FutureTask task = new FutureTask(call);

        //创建Thread，用于运行task
        Thread thead = new Thread(task);
        //设置为后台线程
        thead.setDaemon(true);
        //启动线程
        thead.start();
        try {
            //设置timeout时间，查看返回结果
            task.get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            task.cancel(true);
            System.out.println(task);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        RuntimeOutControl roc = new RuntimeOutControl();
        roc.test();

    }

}

/**
 * 实现Callable接口，具备线程能力，用于执行被监控方法
 *
 * @author budingge.com
 */
class CallableTask implements Callable {

    Object cls;// 类实例
    Method method;// 要执行类方法
    List<Object> args;// 参数

    /**
     * 构造器
     *
     * @param cls
     * @param method
     * @param args
     */
    public CallableTask(Object cls, Method method, List<Object> args) {
        super();
        this.cls = cls;
        this.method = method;
        this.args = args;
    }

    @Override
    public Object call() throws Exception {
        Object rs = null;
        // 反射调用方法
        if (args == null) {
            rs = method.invoke(cls);
        } else {
            rs = method.invoke(cls, args.toArray());
        }
        return rs;
    }

}