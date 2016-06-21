package 同步工具类.信号量;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

/**
 * Created by Arvin on 2016/5/28.
 */
public class BoundHashSet<T> {

    public static void main(String[] args) throws InterruptedException {
        final int SIZE = 2;
        final BoundHashSet<Integer> numbers = new BoundHashSet<>(new HashSet<Integer>(), new Semaphore(SIZE));

        final int ADD_COUNT = SIZE + 2;

        final CountDownLatch endState = new CountDownLatch(ADD_COUNT);
        final CountDownLatch startState = new CountDownLatch(1);

        int i = 0;
        for (i = 0; i < SIZE; ++i) {
            numbers.add(i);
        }

        for (; i < ADD_COUNT; ++i) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        startState.await();
                        numbers.add(finalI);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        endState.countDown();
                    }
                }
            }).start();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    numbers.remove(0);

                    Thread.sleep(200);
                    numbers.remove(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        System.out.println("准备允许所有线程添加");
        long begTime = System.currentTimeMillis();
        startState.countDown();

        endState.await();
        long endTime = System.currentTimeMillis();

        System.out.println("耗时： " + (endTime - begTime) + " 毫秒！");


    }

    /**
     * 存储数据的集合
     */
    private final Set<T> set;
    /**
     * 边界信号量
     */
    private final Semaphore sem;

    public BoundHashSet(Set<T> set, Semaphore sem) {
        this.set = Collections.synchronizedSet(set);
        this.sem = sem;
    }

    /**
     * 添加元素
     *
     * @param e
     * @return
     * @throws InterruptedException
     */
    public boolean add(T e) throws InterruptedException {
        // 请求许可
        System.out.println("正在请求许可： " + e);
        sem.acquire();
        System.out.println("获得许可： " + e);
        // 是否已经添加了
        boolean wasAdded = false;
        try {
            wasAdded = set.add(e);
            return wasAdded;
        } finally {
            if (!wasAdded) {
                // 如果没有添加成功，释放许可，以便其他进入
                sem.release();
            }
        }
    }

    /**
     * 移除，需要释放信号量
     *
     * @param e
     * @return
     */
    public boolean remove(T e) {
        boolean wasRemoved = set.remove(e);
        if (wasRemoved) sem.release();
        return wasRemoved;
    }
}
