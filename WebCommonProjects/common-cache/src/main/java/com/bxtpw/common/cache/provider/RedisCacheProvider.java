package com.bxtpw.common.cache.provider;

import com.bxtpw.common.cache.CacheProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.SerializationUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.UnsupportedEncodingException;

/**
 * redis的缓存实现
 *
 * @author 黎杰
 * @version 0.1
 * @time 2015年4月27日 下午2:47:49
 * @since 0.1
 */
public class RedisCacheProvider implements CacheProvider {

    private static final Logger LOGGER = LogManager.getLogger(RedisCacheProvider.class);

    private JedisPool jedisPool;
    /**
     * 设置redis数据库ID
     */
    private int dataBaseId;

    private static final String ENCODING_CHARSET = "ISO-8859-1";

    private String readCache(String key, String filed) {
        Jedis jedis = getRedis();
        String text = StringUtils.isEmpty(filed) ? jedis.get(key) : jedis.hget(key, filed);
        jedisPool.returnResource(jedis);
        return text;
    }

    private Jedis getRedis() {
        Jedis result = jedisPool.getResource();
        result.select(this.dataBaseId);
        return result;
    }

    public void removeCache(String cacheKey, String field) {
        Jedis jedis = getRedis();
        if (StringUtils.isEmpty(field)) {
            jedis.del(cacheKey);
        } else {
            jedis.hdel(cacheKey, field);
        }
        jedisPool.returnResource(jedis);
    }

    /**
     * 读取缓存中的对象
     */
    @SuppressWarnings("unchecked")
    public <T> T readCache(String key, String filed, Class<T> returnType) {
        String serializationStr = readCache(key, filed);
        if (null != serializationStr) {
            try {
                return (T) SerializationUtils.deserialize(serializationStr.getBytes(ENCODING_CHARSET));
            } catch (UnsupportedEncodingException e) {
                LOGGER.error(e);
            }
        }
        return null;
    }

    /**
     * 写入缓存
     */
    public void writerCache(String key, String filed, Integer expire, Object retVal) {
        Jedis jedis = getRedis();
        try {
            if (StringUtils.isEmpty(filed)) {
                jedis.set(key, new String(SerializationUtils.serialize(retVal), ENCODING_CHARSET));
            } else {
                jedis.hset(key, filed, new String(SerializationUtils.serialize(retVal), ENCODING_CHARSET));
            }
            if (null != expire) {
                jedis.expire(key, expire);
            }
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e);
        }
        jedisPool.returnResource(jedis);
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public int getDataBaseId() {
        return dataBaseId;
    }

    public void setDataBaseId(int dataBaseId) {
        this.dataBaseId = dataBaseId;
    }

}
