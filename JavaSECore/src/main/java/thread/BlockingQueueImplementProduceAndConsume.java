package thread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 使用阻塞队列实现生产者和消费者模型
 */
public class BlockingQueueImplementProduceAndConsume {

    public static void main(String[] args) {

        BQGodown<Integer> godown = new BQGodown<>(10);

        BQConsumer c1 = new BQConsumer(50, godown);
        BQConsumer c2 = new BQConsumer(20, godown);
        BQConsumer c3 = new BQConsumer(30, godown);
        BQProducer p1 = new BQProducer(10, godown);
        BQProducer p2 = new BQProducer(10, godown);
        BQProducer p3 = new BQProducer(10, godown);
        BQProducer p4 = new BQProducer(10, godown);
        BQProducer p5 = new BQProducer(10, godown);
        BQProducer p6 = new BQProducer(10, godown);

        c1.start();
        c2.start();
        c3.start();
        p1.start();
        p2.start();
        p3.start();
        p4.start();
        p5.start();
        p6.start();
    }
}

/**
 * 仓库
 */
class BQGodown<E> {

    public final BlockingQueue<E> bqueue;

    public BQGodown() {
        this.bqueue = new ArrayBlockingQueue<>(10);
    }

    /**
     * 仓库大小
     *
     * @param size
     */
    public BQGodown(int size) {
        this.bqueue = new ArrayBlockingQueue<>(size);
    }

    /**
     * 生产指定数量的商品
     *
     * @param elements 要生产的商品数量
     */
    public void producce(Collection<E> elements) {

        for (E element : elements) {
            try {
                bqueue.put(element);
                System.out.println("生产：" + element);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 消费指定数量的商品
     *
     * @param neednum 要消费的商品数量
     */
    public synchronized void consume(int neednum) {
        int curnum = 0;
        while (curnum < neednum) {
            try {
                E e = bqueue.take();
                System.out.println("消费： " + e);
                ++curnum;
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }
}

/**
 * 生产者
 */
class BQProducer extends Thread {
    /**
     * 要生产的数量
     */
    private int neednum;
    /**
     * 仓库
     */
    private BQGodown<Integer> godown;

    public BQProducer(int neednum, BQGodown<Integer> godown) {
        this.godown = godown;
        this.neednum = neednum;
    }

    @Override
    public void run() {
        List<Integer> cols = new ArrayList<>();
        for (int i = 0; i < neednum; ++i) {
            cols.add(i);
        }
        godown.producce(cols);
    }
}

/**
 * 消费者
 */
class BQConsumer extends Thread {
    /**
     * 要消费的数量
     */
    private int neednum;
    /**
     * 仓库
     */
    private BQGodown<Integer> godown;

    public BQConsumer(int neednum, BQGodown<Integer> godown) {
        this.godown = godown;
        this.neednum = neednum;
    }

    @Override
    public void run() {
        godown.consume(neednum);
    }
}