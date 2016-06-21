package edu.zhku.curator.test;

import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMultiLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Curator 包含三种锁的实现：
 * <p>
 * InterProcessLock
 * InterProcessMutex
 * InterProcessMultiLock
 * InterProcessReadWriteLock
 */
public class CuratorDistLockTest extends BaseTest {

    private final String LOCK_PATH = "/curator/lock";
    private final DateFormat format = new SimpleDateFormat("HH:mm:ss:sss");

    /**
     * 分布式互斥锁测试： InterProcessMutex
     */
    @Test
    public void testDistLock() throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                processWithMutexLock(2000);
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                processWithMutexLock(2000);
            }
        });
        t1.start();
        t2.start();

        t1.join();
        t2.join();

    }

    /**
     * 互斥锁业务
     */
    private void processWithMutexLock(long businessTimeMS) {
        // 初始化锁
        InterProcessLock lock = new InterProcessMutex(client, LOCK_PATH);
        processBusiness("互斥", lock, businessTimeMS);
    }


    public void processBusiness(String lockType, InterProcessLock lock, long businessTimeMS) {
        // 尝试获得锁
        try {
            //lock.acquire(); // 阻塞等待，直到获得锁，可能死锁
            System.out.println(currentThread() + "尝试获得[" + lockType + "]锁......");
            if (lock.acquire(5000, TimeUnit.MILLISECONDS)) { // 并非无限制的等待
                System.out.println(currentThread() + "获得[" + lockType + "]锁，开始执行业务！");
                Thread.sleep(businessTimeMS); // 模拟执行业务的时间
                System.out.println(currentThread() + "完成业务！");
            } else {
                System.out.println(currentThread() + "没有获得[" + lockType + "]锁！");
            }
        } catch (Exception e) {
            System.out.println(currentThread() + "未能获得[" + lockType + "]锁！");
        } finally {
            try {
                lock.release();
                System.out.println(currentThread() + "释放了[" + lockType + "]锁！");
            } catch (Exception ignored) {
            }
        }
    }

    public String currentThread() {
        return format.format(new Date()) + "\t" + Thread.currentThread().getName() + ": ";
    }

    // ---------------------------------------------------------------------------------------

    /**
     * 读写锁测试，读写锁属于重入锁，逻辑如下：
     * 允许多个读操作进入，写操作的情况下，不允许读
     */
    @Test
    public void testInterProcessReadWriteLock() throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                processReadBusinessWithRWLock(500);
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                processReadBusinessWithRWLock(500);
            }
        });
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                processWriteBusinessWithRWLock(500);
            }
        });
        Thread t4 = new Thread(new Runnable() {
            @Override
            public void run() {
                processWriteBusinessWithRWLock(500);
            }
        });
        Thread t5 = new Thread(new Runnable() {
            @Override
            public void run() {
                processReadBusinessWithRWLock(500);
            }
        });

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();
        t5.join();
    }

    /**
     * 读写锁业务： 读业务
     * 没有写操作的情况下才能进行读，多个读业务可以同时执行
     */
    public void processReadBusinessWithRWLock(long businessTimeMS) {
        processBusiness("读", new InterProcessReadWriteLock(client, LOCK_PATH).readLock(), businessTimeMS);
    }

    /**
     * 读写锁业务： 写业务
     * 只能有一个进行写操作
     *
     * @param businessTimeMS
     */
    public void processWriteBusinessWithRWLock(long businessTimeMS) {
        processBusiness("写", new InterProcessReadWriteLock(client, LOCK_PATH).writeLock(), businessTimeMS);
    }
}
