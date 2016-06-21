package edu.zhku.redis.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ResourceBundle;

/**
 * 池化Jedis客户端
 */
public class PooledRedisClientTest {

    protected final String HOST = "192.168.137.90";
    protected final int PORT = 6379;
    // 超时时间，单位为毫秒
    protected final int TIMEOUT = 1000;

    protected final Logger logger = LogManager.getLogger(this.getClass());

    protected JedisPool pool = null;

    @Before
    public void before() {
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

        pool = new JedisPool(config, bundle.getString("redis.ip"), Integer.valueOf(bundle.getString("redis.port")));
    }

    @After
    public void after() {
        pool.close();
        logger.info("Jedis Pool was closed!");
    }

    @Test
    public void testPool() {
        Jedis jedis = pool.getResource();

        System.out.println("name is " + jedis.get("name"));

        pool.returnResource(jedis);
    }

}
