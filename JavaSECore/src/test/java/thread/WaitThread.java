package thread;

/**
 * 线程等待, 注意，一定要获得了这个对象的锁之后才能执行notify方法
 */
public class WaitThread {

    public static void main(String[] args) throws InterruptedException {

        final WaitThread wt = new WaitThread();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("T...");
                try {
                    synchronized (this) {
                        Thread.sleep(1000);
                        notify();
                        System.out.println("1 秒后。、、");
                        System.out.println("又 2 秒后。、、");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();

        synchronized (t) {
            t.wait();
        }
    }
}
