package map.compare;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author 夏集球
 * @time 2016/9/19$ 16:26$
 */
public class MapCompareTest {

    private static final Map<Integer, Integer> hashMap = new HashMap<Integer, Integer>();

    private static final Map<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<Integer, Integer>();

    /**
     * 初始化数据
     */
    public static void initData(int size) {
        hashMap.clear();
        concurrentHashMap.clear();
        for (int i = 0; i < size; ++i) {
            hashMap.put(i, i);
            concurrentHashMap.put(i, i);
        }
    }

    /**
     * 并发访问测试
     *
     * @param concurrentThreadCount 并发访问线程
     */
    public static void testConcurrentGet(int concurrentThreadCount) {
        System.out.println("数据量[" + hashMap.size() + "], 并发[" + concurrentThreadCount + "] 下的结果：");
        System.out.println("HashMap: \t\t\t" + getConcurrentGetAvgTime(concurrentThreadCount, hashMap));
        System.out.println("ConcurrentHashMap: \t" + getConcurrentGetAvgTime(concurrentThreadCount, concurrentHashMap));
        System.out.println("==============================================================");
    }

    /**
     * 获取并发访问的平均时间
     *
     * @param concurrentThreadCount 并发数
     * @param map 要测试的Map
     */
    public static double getConcurrentGetAvgTime(int concurrentThreadCount, final Map<Integer, Integer> map) {
        final CountDownLatch countDownLatch = new CountDownLatch(concurrentThreadCount);
        final Integer key = 0;
        long begTime = System.currentTimeMillis();
        for (int i = 0; i < concurrentThreadCount; ++i) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    map.get(key);
                    countDownLatch.countDown();
                }
            }).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - begTime;
        return (double) totalTime / concurrentThreadCount;
    }

    public static void testWith(int size, int concurrentThreadCount) {
        initData(size);
        testConcurrentGet(concurrentThreadCount);
    }

    public static void main(String[] args) {

        testWith(100000, 100);
        testWith(100000, 1000);
        testWith(100000, 10000);
        testWith(100000, 10000);

        testWith(200000, 100);
        testWith(200000, 1000);
        testWith(200000, 10000);
        testWith(200000, 10000);

        testWith(300000, 100);
        testWith(300000, 1000);
        testWith(300000, 10000);
        testWith(300000, 10000);
    }

}
