package thread;

/**
 * 给线程起名字，主线程始终是main
 */
public class NamedThread {

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
                Thread.currentThread().setName("MyThread");
                System.out.println(Thread.currentThread().getName());
            }
        });
        t.start();

        System.out.println(Thread.currentThread().getName());
        Thread.currentThread().setName("Main MyThread");
        System.out.println(Thread.currentThread().getName());

        Thread.sleep(100);

        t.start();
    }
}
