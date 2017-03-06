package 锁;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Arvin
 * @time 2017/2/13 16:38
 */
public class ReadWriteLockTest {

    public static void main(String[] args) {
        ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

        ReentrantReadWriteLock.WriteLock wLock = rwLock.writeLock();

        final CountDownLatch latch = new CountDownLatch(2);

        try {
            readLock(latch, rwLock, 1);
            readLock(latch, rwLock, 2);

            Thread.sleep(500);

            // 此时不能获得写锁
            wLock.lock();
            System.out.println("获得了写锁！");
            latch.await();
            System.out.println("结束");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            wLock.unlock();
        }
    }

    public static void readLock(final CountDownLatch latch, final ReentrantReadWriteLock rwLock, final int i) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ReentrantReadWriteLock.ReadLock rLock1 = rwLock.readLock();
                    rLock1.lock();
                    System.out.println("获得读锁 " + i);
                    Thread.sleep(1000);
                    rLock1.unlock();
                    System.out.println("释放了读锁 " + i);
                    latch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
