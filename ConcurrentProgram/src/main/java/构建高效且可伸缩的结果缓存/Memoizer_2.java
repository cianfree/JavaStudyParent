package 构建高效且可伸缩的结果缓存;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 * 版本1： 使用ConcurrentHashMap和同步机制来初始化缓存
 *
 * 存在的问题：
 *  1. 明显的先检查后执行，即两个线程同时进入，同一个arg的情况下，计算结果比较长的情况下，value == null为true，那么可能同时重复计算value
 *
 * </pre>
 * Created by Arvin on 2016/5/29.
 */
public class Memoizer_2<A, V> implements Computable<A, V> {

    private final Map<A, V> cache = new ConcurrentHashMap<>();
    private final Computable<A, V> computable;

    public Memoizer_2(Computable<A, V> computable) {
        this.computable = computable;
    }


    @Override
    public V compute(A arg) throws InterruptedException {
        V value = cache.get(arg);
        if (value == null) {
            value = computable.compute(arg);
            cache.put(arg, value);
        }
        return value;
    }
}
