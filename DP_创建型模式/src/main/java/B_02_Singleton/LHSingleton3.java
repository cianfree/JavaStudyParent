package B_02_Singleton;

/**
 * <pre>
 * 懒汉模式1-静态内部类
 * </pre>
 * Created by Arvin on 2016/4/26.
 */
public class LHSingleton3 {

    private static class Holder {
        private static LHSingleton3 INSTANCE = new LHSingleton3();
    }

    private LHSingleton3() {
        System.out.println("Create LHSingleton2 Instance!");
    }

    public static LHSingleton3 getInstance() {
        return Holder.INSTANCE;
    }

    static LHSingleton3 INSTANCE1;
    static LHSingleton3 INSTANCE2;

    public static void main(String[] args) throws InterruptedException, ClassNotFoundException {
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
