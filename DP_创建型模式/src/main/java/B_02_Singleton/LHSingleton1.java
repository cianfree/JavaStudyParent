package B_02_Singleton;

/**
 * <pre>
 * 懒汉模式1, 这种写法有严重的多线程安全问题，当第多个线程进来的时候，可能都死判断到INSTANCE==null，同时都执行了new
 * </pre>
 * Created by Arvin on 2016/4/26.
 */
public class LHSingleton1 {

    private static LHSingleton1 INSTANCE;

    private LHSingleton1() {
        System.out.println("Create LHSingleton1 Instance!");
    }

    public static LHSingleton1 getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new LHSingleton1();
        }
        return INSTANCE;
    }

    static LHSingleton1 INSTANCE1;
    static LHSingleton1 INSTANCE2;

    public static void main(String[] args) throws InterruptedException {
        // 模拟多线程下错误情况
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                INSTANCE1 = getInstance();
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                INSTANCE2 = getInstance();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println(INSTANCE1 == INSTANCE2);
    }
}
