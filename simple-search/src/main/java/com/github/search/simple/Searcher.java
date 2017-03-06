package com.github.search.simple;

import com.github.search.simple.analyzer.Analyzer;
import com.github.search.simple.model.ResultItem;
import com.github.search.simple.model.SearchResult;
import com.github.search.simple.strategy.element.ElementStrategy;

import java.util.Comparator;

/**
 * 搜索器
 *
 * @author Arvin
 * @time 2016/12/27 17:05
 */
public interface Searcher {

    /**
     * 检索
     *
     * @param rootPath        搜索根路径
     * @param keywords        关键字
     * @param pageNo          页码
     * @param pageSize        每页查询数量
     * @param analyzer        分词器
     * @param elementStrategy 元素与关键字相似度匹配策略
     * @param comparator      比较器
     * @return 始终返回一个非空的值
     */
    SearchResult search(String rootPath, String keywords, int pageNo, int pageSize, Analyzer analyzer, ElementStrategy elementStrategy, Comparator<ResultItem> comparator);
}
