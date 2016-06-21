package thread;

/**
 * 生产者-消费者-仓储模型测试
 * <p>
 * wait, notifyAll实现
 */
public class ProduceAndConsumeTest {

    public static void main(String[] args) {
        Godown godown = new Godown(30);
        Consumer c1 = new Consumer(50, godown);
        Consumer c2 = new Consumer(20, godown);
        Consumer c3 = new Consumer(30, godown);
        Producer p1 = new Producer(10, godown);
        Producer p2 = new Producer(10, godown);
        Producer p3 = new Producer(10, godown);
        Producer p4 = new Producer(10, godown);
        Producer p5 = new Producer(10, godown);
        Producer p6 = new Producer(10, godown);
        Producer p7 = new Producer(80, godown);

        c1.start();
        c2.start();
        c3.start();
        p1.start();
        p2.start();
        p3.start();
        p4.start();
        p5.start();
        p6.start();
        p7.start();

    }
}

/**
 * 仓库
 */
class Godown {

    /**
     * 最大仓库存储量
     */
    public static final int MAX_SIZE = 100;
    /**
     * 当前库存量
     */
    public int curnum;

    public Godown() {
    }

    public Godown(int curnum) {
        this.curnum = curnum;
    }

    /**
     * 生产指定数量的商品
     *
     * @param neednum 要生产的商品数量
     */
    public synchronized void producce(int neednum) {
        // 检查是否需要生产
        while (curnum + neednum > MAX_SIZE) {
            System.out.println("要生产的产品数量" + neednum + "超过剩余库存量" + (MAX_SIZE - curnum) + "，暂时不能执行生产任务!");
            System.out.println();
            try {
                // 当前生产线程先等待，等消费者消费完了再执行
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 满足生产条件，则进行生产，这里简单的更改当前库存量
        curnum += neednum;
        System.out.println("已经生产了" + neednum + "个产品，现仓储量为" + curnum);
        // 唤醒在此对象监视器上等待的所有线程
        notifyAll();
    }

    /**
     * 消费指定数量的商品
     *
     * @param neednum 要消费的商品数量
     */
    public synchronized void consume(int neednum) {
        // 检查有没有商品，没有的话就等待生产之后再消费
        while (curnum < neednum) {
            System.out.println("没有足够的商品可以消费，进入等待！");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 有足够的进行消费，那么进行消费
        curnum -= neednum;
        System.out.println("已经消费了" + neednum + "个产品，现仓储量为" + curnum);
        // 唤醒在此对象监视器上等待的所有线程
        notifyAll();
    }
}

/**
 * 生产者
 */
class Producer extends Thread {
    /**
     * 要生产的数量
     */
    private int neednum;
    /**
     * 仓库
     */
    private Godown godown;

    public Producer(int neednum, Godown godown) {
        this.godown = godown;
        this.neednum = neednum;
    }

    @Override
    public void run() {
        godown.producce(neednum);
    }
}

/**
 * 消费者
 */
class Consumer extends Thread {
    /**
     * 要消费的数量
     */
    private int neednum;
    /**
     * 仓库
     */
    private Godown godown;

    public Consumer(int neednum, Godown godown) {
        this.godown = godown;
        this.neednum = neednum;
    }

    @Override
    public void run() {
        godown.consume(neednum);
    }
}