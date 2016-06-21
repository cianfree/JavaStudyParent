package edu.zhku.redis.test.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;

/**
 * Created by Arvin on 2016/6/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext-redis-cluster.xml"})
public class SpringJedisClusterTest {

    @Autowired
    protected JedisCluster cluster;

    @Test
    public void testCluster() throws IOException {

        cluster.del("name");

        String result = cluster.set("name", "Arvin");

        System.out.println(result);

        System.out.println(cluster.get("name"));

        cluster.close();
    }

}
