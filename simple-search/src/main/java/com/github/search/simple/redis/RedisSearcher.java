package com.github.search.simple.redis;

import com.github.search.simple.Searcher;
import com.github.search.simple.analyzer.Analyzer;
import com.github.search.simple.model.ResultItem;
import com.github.search.simple.model.SearchResult;
import com.github.search.simple.strategy.element.ElementStrategy;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * 搜索器
 *
 * @author Arvin
 * @time 2016/12/27 18:59
 */
public class RedisSearcher extends AbstractIndexerSearcher implements Searcher {

    public RedisSearcher() {
        super(redisClient);
    }

    @Override
    public SearchResult search(String rootPath, String keyword, int pageNo, int pageSize, Analyzer analyzer, ElementStrategy elementStrategy, Comparator<ResultItem> comparator) {
        Jedis jedis = null;
        IJedisPool pool = redis.getJedisPool();
        try {
            jedis = pool.getResource();
            Set<String> possibleKeys = getPossibleKeys(analyzer, getIndexHashKeyRootPath(rootPath), keyword, jedis);
            if (possibleKeys == null || possibleKeys.isEmpty()) {
                return new SearchResult(pageNo, pageSize, 0, new ArrayList<ResultItem>());
            }
            String[] keys = possibleKeys.toArray(new String[possibleKeys.size()]);
            Set<String> possibleIdSet = jedis.sunion(keys);
            if (null != possibleIdSet && !possibleIdSet.isEmpty()) {
                String[] idArray = possibleIdSet.toArray(new String[possibleIdSet.size()]);
                List<String> elementStringList = jedis.hmget(getElementHashKeyPath(rootPath), idArray);
                List<ResultItem> resultList = new ArrayList<>();
                for (String elementString : elementStringList) {
                    Element element = Element.fromString(elementString);
                    // 没有策略的时候，都符合
                    float similarity = null == elementStrategy ? 1 : elementStrategy.accept(keyword, element);
                    if (similarity > 0F) {
                        resultList.add(new ResultItem(element, similarity));
                    }
                }
                // 排序
                sortResultByComparator(comparator, resultList, keyword);
                List<ResultItem> itemList = paging(resultList, pageNo, pageSize);
                return new SearchResult(pageNo, pageSize, resultList.size(), itemList);
            }

        } catch (Exception e) {
            logger.error("搜索错误： 关键字[" + keyword + "] " + e.getMessage());
        } finally {
            if (null != jedis) {
                pool.returnResource(jedis);
            }
        }
        return new SearchResult(pageNo, pageSize, 0, new ArrayList<ResultItem>());
    }


    /**
     * 排序
     *
     * @param comparator 比较器
     * @param itemList   结果列表
     */
    private List<ResultItem> sortResultByComparator(Comparator<ResultItem> comparator, List<ResultItem> itemList, String keyword) {
        try {
            Comparator<ResultItem> realComparator = null == comparator ? ResultItem.defaultComparator : comparator;
            // 此处不使用Collections.sort方法，以免传过来的comparator不符合要求
            LegacyMergeSortUtils.sort(itemList, realComparator);
        } catch (Exception e) {
            logger.warn("搜索排序过程错误(不进行排序)： 关键字[" + keyword + "] -> " + e.getMessage(), e);
        }
        return itemList;
    }

    /**
     * 计算分页数据
     */
    private <T> List<T> paging(List<T> resultList, int pageNo, int pageSize) {
        // 计算分页结果
        int totalCount = resultList.size();
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
        if (pageNo > totalPage) {
            return new ArrayList<>();
        }
        int fromIndex = (pageNo - 1) * pageSize;
        int toIndex = fromIndex + pageSize;
        toIndex = toIndex < totalCount ? toIndex : totalCount;
        return resultList.subList(fromIndex, toIndex);
    }

    /**
     * 获取所有可能的key
     *
     * @param analyzer 分词器
     * @param rootPath 根路径
     * @param keyword  关键字
     * @param jedis    redis client
     */
    private Set<String> getPossibleKeys(Analyzer analyzer, String rootPath, String keyword, Jedis jedis) {
        if (keyword.length() == 1) {    // 只有一个，那么返回包含该字的所有key
            return jedis.keys(rootPath + "*" + keyword + "*"); // 找出所有包含该关键字的keys
        }
        // 通过分词获取
        return analyzerKeys(analyzer, rootPath, keyword);
    }
}
