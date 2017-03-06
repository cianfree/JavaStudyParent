package thread;

/**
 * Created by Arvin on 2016/4/18.
 */
public class SyncFunction {

    public static void main(String[] args) throws InterruptedException {


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    show1();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                show2();
            }
        }).start();


    }


    private synchronized static void show1() throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("show1 is finished");
    }

    private synchronized static void show2() {
        System.out.println("show2 is finished");

    }
}
