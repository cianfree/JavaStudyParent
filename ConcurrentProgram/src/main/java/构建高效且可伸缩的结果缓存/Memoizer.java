package 构建高效且可伸缩的结果缓存;

import java.util.Map;
import java.util.concurrent.*;

/**
 * <pre>
 *  终极版本
 * </pre>
 * Created by Arvin on 2016/5/29.
 */
public class Memoizer<A, V> implements Computable<A, V> {

    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();
    private final Computable<A, V> computable;

    public Memoizer(Computable<A, V> computable) {
        this.computable = computable;
    }

    @Override
    public V compute(final A arg) throws InterruptedException {
        while (true) {
            Future<V> f = cache.get(arg);
            if (f == null) {
                FutureTask<V> task = new FutureTask<V>(new Callable<V>() {
                    @Override
                    public V call() throws Exception {
                        return computable.compute(arg);
                    }
                });
                f = cache.putIfAbsent(arg, task);
                if (f == null) {
                    f = task;
                    task.run();
                }
            }
            try {
                return f.get();
            } catch (ExecutionException e) {
                Thread.currentThread().interrupt();
            }
            return null;
        }
    }
}
