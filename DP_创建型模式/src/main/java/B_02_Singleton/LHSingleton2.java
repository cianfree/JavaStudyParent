package B_02_Singleton;

/**
 * <pre>
 * 懒汉模式1-双检法
 * </pre>
 * Created by Arvin on 2016/4/26.
 */
public class LHSingleton2 {

    private static LHSingleton2 INSTANCE;

    private LHSingleton2() {
        System.out.println("Create LHSingleton2 Instance!");
    }

    public static LHSingleton2 getInstance() {
        if (null == INSTANCE) {
            synchronized (LHSingleton2.class) {
                if (null == INSTANCE) {
                    INSTANCE = new LHSingleton2();
                }
            }
        }
        return INSTANCE;
    }

    static LHSingleton2 INSTANCE1;
    static LHSingleton2 INSTANCE2;

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

        System.out.println(INSTANCE1);
        System.out.println(INSTANCE2);

        System.out.println(INSTANCE1 == INSTANCE2);
    }
}
