package 构建高效且可伸缩的结果缓存;

import java.util.Map;
import java.util.concurrent.*;

/**
 * <pre>
 * 版本1： 使用FutureTask来避免Memoizer_2中的问题
 *
 * 存在的问题：
 *  1. 仍然可能出现Memoizer_2中的问题，只是概率非常小了
 *
 * </pre>
 * Created by Arvin on 2016/5/29.
 */
public class Memoizer_3<A, V> implements Computable<A, V> {

    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();
    private final Computable<A, V> computable;

    public Memoizer_3(Computable<A, V> computable) {
        this.computable = computable;
    }

    @Override
    public V compute(final A arg) throws InterruptedException {
        Future<V> f = cache.get(arg);
        if (f == null) {
            FutureTask<V> task = new FutureTask<V>(new Callable<V>() {
                @Override
                public V call() throws Exception {
                    return computable.compute(arg);
                }
            });
            f = task;
            cache.put(arg, task);
            task.run(); // 运行结果
        }
        try {
            return f.get();
        } catch (ExecutionException e) {
            Thread.currentThread().interrupt();
        }
        return null;
    }
}
