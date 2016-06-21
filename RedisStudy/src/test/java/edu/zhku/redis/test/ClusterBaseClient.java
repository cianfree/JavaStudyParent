package edu.zhku.redis.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 集群客户端测试
 */
public class ClusterBaseClient {

    protected final Logger logger = LogManager.getLogger(this.getClass());

    protected ShardedJedisPool pool;

    @Before
    public void before() {
        List<JedisShardInfo> shareInfos = Arrays.asList(
                new JedisShardInfo("192.168.137.129", 7000),
                new JedisShardInfo("192.168.137.129", 7001),
                new JedisShardInfo("192.168.137.129", 7002),
                new JedisShardInfo("192.168.137.129", 7003),
                new JedisShardInfo("192.168.137.129", 7004),
                new JedisShardInfo("192.168.137.129", 7005)
        );

        ResourceBundle bundle = ResourceBundle.getBundle("redis");
        if (bundle == null) {
            throw new IllegalArgumentException("[redis.properties] is not found!");
        }

        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(Integer.valueOf(bundle.getString("redis.pool.maxTotal")));
        config.setMaxIdle(Integer.valueOf(bundle.getString("redis.pool.maxIdle")));
        config.setMaxWaitMillis(Long.valueOf(bundle.getString("redis.pool.maxWaitMillis")));
        config.setTestOnBorrow(Boolean.valueOf(bundle.getString("redis.pool.testOnBorrow")));
        config.setTestOnReturn(Boolean.valueOf(bundle.getString("redis.pool.testOnReturn")));

        pool = new ShardedJedisPool(config, shareInfos);
    }

    @After
    public void after() {
        pool.close();
    }
}
