package 同步工具类.闭锁;

import java.util.concurrent.CountDownLatch;

/**
 * <pre>
 * 计算多个并行线程总的执行时间
 * 1. 使用CountDownLatch来控制程序的开始
 * 2. 使用CountDownLatch来await所有并行线程的执行完成状态
 * </pre>
 * Created by Arvin on 2016/5/28.
 */
public class CalculateConcurrentThreadTotalExecuteTime {

    public static void main(String[] args) {

        final int TASK_COUNT = 10;
        Runnable[] tasks = new Runnable[TASK_COUNT];

        for (int i = 0; i < TASK_COUNT; ++i) {
            final int finalI = i;
            tasks[i] = new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("第 " + finalI + " 个任务开始执行！");
                        Thread.sleep(finalI * 100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            };
        }

        // 统计时间
        for (int i = 0; i < 10; ++i) {
            System.out.println("第 " + i + " 次执行时间： " + calculateConcurrentExecuteTime(tasks) + " 毫秒");
        }

    }

    /**
     * 任务列表，计算这些任务并行执行的时间
     *
     * @param tasks 要执行的并行任务
     */
    public static long calculateConcurrentExecuteTime(Runnable... tasks) {
        if (tasks == null || tasks.length < 1) return 0L;
        final CountDownLatch startState = new CountDownLatch(1);
        final CountDownLatch endState = new CountDownLatch(tasks.length);

        // 执行任务
        for (final Runnable task : tasks) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        startState.await(); // 等待开始状态
                        try {
                            task.run();
                        } finally {
                            // 表示执行完成了
                            endState.countDown();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                }
            }).start();
        }
        // 开始时间
        long begTime = System.currentTimeMillis();
        // 告诉所有线程，开始执行
        startState.countDown();
        // 等待所有线程执行完成的通知
        try {
            endState.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        long endTime = System.currentTimeMillis();
        return endTime - begTime;
    }
}
