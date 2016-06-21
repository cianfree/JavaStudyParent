package edu.zhku.redis.test;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Created by Arvin on 2016/6/20.
 */
public class JedisClusterTest {

    protected final Logger logger = LogManager.getLogger(this.getClass());

    @Test
    public void testEnv() throws IOException {

        ResourceBundle bundle = ResourceBundle.getBundle("redis");
        if (bundle == null) {
            throw new IllegalArgumentException("[redis.properties] is not found!");
        }

        GenericObjectPoolConfig config = new GenericObjectPoolConfig();

        config.setMaxTotal(Integer.valueOf(bundle.getString("redis.pool.maxTotal")));
        config.setMaxIdle(Integer.valueOf(bundle.getString("redis.pool.maxIdle")));
        config.setMaxWaitMillis(Long.valueOf(bundle.getString("redis.pool.maxWaitMillis")));
        config.setTestOnBorrow(Boolean.valueOf(bundle.getString("redis.pool.testOnBorrow")));
        config.setTestOnReturn(Boolean.valueOf(bundle.getString("redis.pool.testOnReturn")));

        Set<HostAndPort> hostAndPorts = new HashSet<>();
        hostAndPorts.add(new HostAndPort("192.168.137.129", 7000));
        hostAndPorts.add(new HostAndPort("192.168.137.129", 7001));
        hostAndPorts.add(new HostAndPort("192.168.137.129", 7002));
        hostAndPorts.add(new HostAndPort("192.168.137.129", 7003));
        hostAndPorts.add(new HostAndPort("192.168.137.129", 7004));
        hostAndPorts.add(new HostAndPort("192.168.137.129", 7005));

        // 初始化
        JedisCluster cluster = new JedisCluster(hostAndPorts, config);

        String result = cluster.set("name", "夏集球");
        logger.info("Set name=夏集球, return " + result);

        cluster.close();
    }
}
