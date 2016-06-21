package 构建高效且可伸缩的结果缓存;

import java.math.BigInteger;

/**
 * Created by Arvin on 2016/5/29.
 */
public class ExpensiveComputable implements Computable<String, BigInteger> {
    @Override
    public BigInteger compute(String arg) throws InterruptedException {
        return new BigInteger(arg);
    }
}
