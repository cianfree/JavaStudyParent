package 构建高效且可伸缩的结果缓存;

/**
 * Created by Arvin on 2016/5/29.
 */
public interface Computable<A, V> {

    /**
     * 计算
     *
     * @param arg
     * @return
     * @throws InterruptedException
     */
    V compute(A arg) throws InterruptedException;
}
