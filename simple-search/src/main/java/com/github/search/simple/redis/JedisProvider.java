package com.github.search.simple.redis;

import redis.clients.jedis.Jedis;

/**
 * Jedis 提供者
 *
 * @author Arvin
 * @time 2017/1/9 22:20
 */
public interface JedisProvider {

    /**
     * 获取连接
     */
    Jedis getJedis();

    /**
     * 关闭连接
     * @param jedis redis 客户端连接
     */
    void closeJedis(Jedis jedis);

}
