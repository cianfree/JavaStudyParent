package com.github.search.simple.analyzer;

import java.util.Set;

/**
 * 分词器
 *
 * @author Arvin
 * @time 2016/12/8 9:09
 */
public interface Analyzer {

    /**
     * 分词并返回指定的分词结果集合
     *
     * @param content 要分词的内容
     * @return 始终返回一个 null 的集合
     */
    Set<String> analyzer(String content);

    /**
     * 给每一个分词结果加前缀
     *
     * @param content 要分词的内容
     * @param prefix  前缀
     */
    Set<String> analyzer(String content, String prefix);
}
