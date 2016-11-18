package Database.sql;

/**
 * @author Arvin
 * @time 2016/11/15 12:33
 */
public class ThreadTest {


    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());

                try {
                    Thread.sleep(1000);

                    // 主线程中进行了修改
                    System.out.println(Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "这里设置线程名称");

        thread.start();

        // 500毫秒后修改线程名称
        Thread.sleep(500);
        thread.setName("新的线程名称！");
    }
}