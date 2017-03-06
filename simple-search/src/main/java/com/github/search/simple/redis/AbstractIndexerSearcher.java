package com.github.search.simple.redis;

import com.github.search.simple.analyzer.Analyzer;
import com.github.search.simple.model.Element;
import com.github.search.simple.model.Field;
import com.github.search.simple.utils.ObjectUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Arvin
 * @time 2016/12/27 17:09
 */
public abstract class AbstractIndexerSearcher {

    protected Logger logger = LogManager.getLogger(this.getClass());

    /** Redis客户端 */
    protected final IRedisClient redisClient;

    protected AbstractIndexerSearcher(IRedisClient redisClient) {
        this.redisClient = redisClient;
    }

    public IRedisClient getRedisClient() {
        return redisClient;
    }

    /**
     * 获取元素hash key 的存储路径
     */
    protected String getElementHashKeyPath(String rootPath) {
        return rootPath + ":m";
    }

    /**
     * 获取指定key的hash map的key
     *
     * @param rootPath 索引根路径
     */
    protected String getIndexHashKeyRootPath(String rootPath) {
        return rootPath + ":idx:";
    }

    /**
     * 获取指定元素需要进行索引的属性的内容列表
     *
     * @param element 元素
     */
    protected Set<String> getNeedIndexContent(Element element) {
        Set<String> result = new HashSet<>();
        Set<Field> fields = element.getFs();
        for (Field field : fields) {
            if (1 == field.getNi()) {
                result.add(field.getV());
            }
        }
        return result;
    }

    /**
     * 分词
     *
     * @param rootPath   搜索根路径
     * @param contentSet 要进行分词的内容集合
     * @param customKeys 自定义的 Key 列表
     */
    protected Set<String> analyzerKeys(Analyzer analyzer, String rootPath, Set<String> contentSet, String[] customKeys) {
        Set<String> keys = new HashSet<>();
        // 如果存在自定义的key则添加
        String keyPrefix = ObjectUtils.isNotBlank(rootPath) ? rootPath : "";
        if (customKeys != null && customKeys.length > 0) {
            for (String customKey : customKeys) {
                if (ObjectUtils.isNotBlank(customKey)) {
                    keys.add(keyPrefix + customKey);
                }
            }
        }

        for (String content : contentSet) {
            Set<String> subKeys = analyzer.analyzer(content, rootPath);
            if (!subKeys.isEmpty()) {
                keys.addAll(subKeys);
            }
        }
        return keys;
    }

    /**
     * 分词
     *
     * @param content 要分词的内容
     */
    protected Set<String> analyzerKeys(Analyzer analyzer, String rootPath, String content) {
        return analyzer.analyzer(content, rootPath);
    }
}
