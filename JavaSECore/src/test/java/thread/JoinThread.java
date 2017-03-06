package thread;

/**
 * Join方法测试
 * Thread的非静态方法join()让一个线程B“加入”到另外一个线程A的尾部。在A执行完毕之前，B不能工作。例如：
 * <p>
 * Thread t = new MyThread();
 * t.start();
 * t.join();
 * <p>
 * 另外，join()方法还有带超时限制的重载版本。例如t.join(5000);则让线程等待5000毫秒，如果超过这个时间，则停止等待，变为可运行状态。
 */
public class JoinThread {

    public static void main(String[] args) throws InterruptedException {
        testTwo();
    }

    public static void testTwo() {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    System.out.println("线程1第" + i + "次执行！");
                }
                System.out.println(Thread.holdsLock(JoinThread.class));

            }
        });
        t1.start();

        for (int i = 0; i < 20; i++) {
            System.out.println("主线程第" + i + "次执行！");
            if (i > 2) try {
                //t1线程合并到主线程中，主线程停止执行过程，转而执行t1线程，直到t1执行完毕后继续。
                t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static void testOne() throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("T1 ....");
                try {
                    Thread.sleep(1000);
                    System.out.println("T 1 finished");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
        t1.join(500);

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("T 2");
            }
        });
        t2.start();
    }
}
