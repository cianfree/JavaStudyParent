package B_02_Singleton;

/**
 * <pre>
 * 枚举
 * </pre>
 * Created by Arvin on 2016/4/26.
 */
public enum LHSingleton4 {

    INSTANCE;

    static LHSingleton4 INSTANCE1;
    static LHSingleton4 INSTANCE2;

    public static void main(String[] args) throws InterruptedException, ClassNotFoundException {
        // 模拟多线程下错误情况
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                INSTANCE1 = LHSingleton4.INSTANCE;
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                INSTANCE2 = LHSingleton4.INSTANCE;
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
