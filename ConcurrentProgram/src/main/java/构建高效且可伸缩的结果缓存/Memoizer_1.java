package 构建高效且可伸缩的结果缓存;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * 版本1： 使用HashMap和同步机制来初始化缓存
 *
 * 存在的问题：
 *  1. 明显的可伸缩性问题，一次只有一个线程进入
 *
 * </pre>
 * Created by Arvin on 2016/5/29.
 */
public class Memoizer_1<A, V> implements Computable<A, V> {

    private final Map<A, V> cache = new HashMap<>();
    private final Computable<A, V> computable;

    public Memoizer_1(Computable<A, V> computable) {
        this.computable = computable;
    }


    @Override
    public synchronized V compute(A arg) throws InterruptedException {
        V value = cache.get(arg);
        if (value == null) {
            value = computable.compute(arg);
            cache.put(arg, value);
        }
        return value;
    }
}
