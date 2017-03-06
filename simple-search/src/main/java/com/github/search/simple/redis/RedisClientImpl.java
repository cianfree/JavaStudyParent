package com.github.search.simple.redis;

import redis.clients.jedis.Jedis;

/**
 * Redis 客户端
 * @author Arvin
 * @time 2017/1/9 22:24
 */
public class RedisClientImpl implements JedisProvider {

    /** 提供者 */
    private JedisProvider jedisProvider;

    public RedisClientImpl(JedisProvider jedisProvider) {
        this.jedisProvider = jedisProvider;
    }

    public RedisClientImpl() {
    }

    public JedisProvider getJedisProvider() {
        return jedisProvider;
    }

    public void setJedisProvider(JedisProvider jedisProvider) {
        this.jedisProvider = jedisProvider;
    }


    @Override
    public Jedis getJedis() {
        return jedisProvider.getJedis();
    }

    @Override
    public void closeJedis(Jedis jedis) {
        jedisProvider.closeJedis(jedis);
    }
}
