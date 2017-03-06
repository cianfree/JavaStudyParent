package com.github.search.simple.redis;

import com.github.search.simple.Indexer;
import com.github.search.simple.analyzer.Analyzer;
import com.github.search.simple.model.Element;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.Set;

/**
 * 基于 Redis 实现的索引构建器
 *
 * @author Arvin
 * @time 2016/12/27 17:07
 */
public class RedisIndexer extends AbstractIndexerSearcher implements Indexer {

    public RedisIndexer(IRedisClient redisClient) {
        super(redisClient);
    }

    @Override
    public void index(Analyzer analyzer, String rootPath, Element element, String[] customKeys) {
        // 获取要进行索引的字段
        Set<String> contentSet = getNeedIndexContent(element);
        // 分词
        Set<String> keys = analyzerKeys(analyzer, getIndexHashKeyRootPath(rootPath), contentSet, customKeys);
        Jedis jedis = null;
        try {
            jedis = redisClient.getJedis();
            // 管道
            Pipeline pipe = jedis.pipelined();
            // 存储元素
            pipe.hset(getElementHashKeyPath(rootPath), element.getId(), Element.toString(element));
            // 存储索引， 关键字 -> ID集合
            for (String subKey : keys) {
                pipe.sadd(subKey, element.getId());// pipline 将多个操作封装成一个TCP请求包
            }
            pipe.sync(); // 同步到 Redis
        } finally {
            if (jedis != null) {
                redisClient.closeJedis(jedis);
            }
        }
    }

    @Override
    public void index(Analyzer analyzer, String rootPath, Element element) {
        index(analyzer, rootPath, element, null);
    }

    @Override
    public boolean isExists(String rootPath) {
        Set<String> keys = redis.keys(rootPath + "*");
        return null != keys && !keys.isEmpty();
    }

    @Override
    public void clearIndex(String rootPath) {
        redis.del(getElementHashKeyPath(rootPath));
        Set<String> keys = redis.keys(getIndexHashKeyRootPath(rootPath) + "*");
        if (null != keys && !keys.isEmpty()) {
            redis.del(keys.toArray(new String[keys.size()]));
        }
    }
}
