package 生产者消费者.信号量;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

/**
 * 使用信号量实现的生产者消费者
 * Created by Arvin on 2016/5/29.
 */
public class SemaphoreProduceAndConsumeMain {

    public static void main(String[] args) {
        // 生产者数量
        final int PRODUCER_COUNT = 20;
        // 消费者数量
        final int CONSUMER_COUNT = 10;

        final CountDownLatch startState = new CountDownLatch(1);

        final Godown godown = new Godown(Math.max(PRODUCER_COUNT, CONSUMER_COUNT));

        for (int i = 0; i < PRODUCER_COUNT; ++i) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        startState.await();
                        new Producer(godown, (finalI + 1) * 2).run();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }).start();
        }

        for (int i = 0; i < CONSUMER_COUNT; ++i) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        startState.await();
                        new Consumer(godown, (finalI + 1) % 5).run();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }).start();
        }

        startState.countDown();
        System.out.println("All Thread is starting!");
    }
}

/**
 * 工具类
 */
class Utils {

    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss:sss");

    public static final String getCurThread() {
        return "Thread[" + Thread.currentThread().getId() + "] " + DATE_FORMAT.format(new Date()) + "\t";
    }
}

/**
 * 生产者
 */
class Producer implements Runnable {

    /**
     * 仓库
     */
    private final Godown godown;

    /**
     * 要生产的数量
     */
    private final int produceNumber;

    /**
     * 生产者
     *
     * @param godown
     */
    public Producer(Godown godown, int produceNumber) {
        this.godown = godown;
        this.produceNumber = produceNumber;
    }


    @Override
    public void run() {
        try {
            Thread.sleep(this.produceNumber * 200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        godown.produce(this.produceNumber);
    }
}

/**
 * 消费者
 */
class Consumer implements Runnable {

    /**
     * 仓库
     */
    private final Godown godown;

    /**
     * 要消费的数量
     */
    private final int consumeNumber;

    /**
     * @param godown
     */
    public Consumer(Godown godown, int consumeNumber) {
        this.godown = godown;
        this.consumeNumber = consumeNumber > 0 ? consumeNumber : 1;
    }


    @Override
    public void run() {
        godown.consume(this.consumeNumber);
    }
}

/**
 * 仓库
 */
class Godown {
    /**
     * 当前仓库数量
     */
    private volatile int currentSize = 0;
    private final int size;
    /**
     * 生产信号量
     */
    private Semaphore semProduce;
    /**
     * 消费信号量
     */
    private Semaphore semConsume;

    public Godown(int size) {
        this.size = size;
        this.semProduce = new Semaphore(size);
        this.semConsume = new Semaphore(0);
    }

    public int size() {
        return this.size;
    }

    /**
     * 生产相当数量的产品
     *
     * @param produceNumber 要生产的产品数量
     */
    public void produce(int produceNumber) {
        // 获取许可操作
        try {
            this.semProduce.acquire(produceNumber);
            this.semConsume.release(produceNumber);
            System.out.println(Utils.getCurThread() + "生产了 " + produceNumber + " 个产品！");
            this.currentSize += produceNumber;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 消费
     *
     * @param consumeNumber 消费数量
     */
    public void consume(int consumeNumber) {
        try {
            // 获取许可
            this.semConsume.acquire(consumeNumber);
            System.out.println(Utils.getCurThread() + "消费了 " + consumeNumber + " 个产品！");
            // 释放多个许可
            this.semProduce.release(consumeNumber);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
