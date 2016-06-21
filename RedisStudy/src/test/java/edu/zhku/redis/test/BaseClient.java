package edu.zhku.redis.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import redis.clients.jedis.Jedis;

/**
 * Created by Arvin on 2016/6/18.
 */
public class BaseClient {

    protected Jedis jedis;

    protected final String HOST = "192.168.137.90";
    protected final int PORT = 6379;
    // 超时时间，单位为毫秒
    protected final int TIMEOUT = 1000;

    protected final Logger logger = LogManager.getLogger(this.getClass());

    @Before
    public void before() {
        jedis = new Jedis(HOST, PORT, TIMEOUT);
        logger.info("Init Jedis!");
    }

    @After
    public void after() {
        logger.info("Closing Redis!");
        jedis.close();
        logger.info("Closed Redis!");
    }
}
