package thread;

/**
 * Thread.yield() 让步，将当前线程变为可运行状态，让同等优先级的线程可能获得执行的机会
 */
public class YieldThread {

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("T1 start");
                    Thread.sleep(500);
                    //Thread.yield(); // 让步
                    System.out.println("T1 end");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
        System.out.println("T1优先级： " + t1.getPriority());

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("T2 start");
                    Thread.sleep(500);
                    Thread.yield(); // 让步
                    System.out.println("T2 end");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t2.start();

        t1.join();
        t2.join();

    }
}
