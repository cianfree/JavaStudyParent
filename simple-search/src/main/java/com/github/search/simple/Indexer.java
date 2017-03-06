package com.github.search.simple;

import com.github.search.simple.analyzer.Analyzer;
import com.github.search.simple.model.Element;

/**
 * 索引接口
 *
 * @author Arvin
 * @time 2016/12/27 17:05
 */
public interface Indexer {

    /**
     * 索引
     *
     * @param analyzer   分词器
     * @param rootPath   根路径
     * @param element    要索引的元素
     * @param customKeys 自定义关键字
     */
    void index(Analyzer analyzer, String rootPath, Element element, String[] customKeys);

    /**
     * 索引元素
     *
     * @param analyzer 分词器
     * @param rootPath 根路径
     * @param element  要索引的元素
     */
    void index(Analyzer analyzer, String rootPath, Element element);

    /**
     * 检测指定的路径是否存在了索引
     *
     * @param rootPath 根路径
     */
    boolean isExists(String rootPath);

    /**
     * 清除指定根路径下的索引
     *
     * @param rootPath 根路径
     */
    void clearIndex(String rootPath);

}
