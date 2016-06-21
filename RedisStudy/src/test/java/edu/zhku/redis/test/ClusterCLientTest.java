package edu.zhku.redis.test;

import org.junit.Test;
import redis.clients.jedis.ShardedJedis;

/**
 * Created by Arvin on 2016/6/20.
 */
public class ClusterCLientTest extends ClusterBaseClient {

    @Test
    public void testBasicCommand() {
        ShardedJedis jedis = pool.getResource();

        String result = jedis.set("name", "夏集球");
        logger.info("Set name=夏集球, return " + result);

        jedis.close();
    }
}
