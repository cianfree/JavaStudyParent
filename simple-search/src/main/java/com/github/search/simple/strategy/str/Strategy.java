package com.github.search.simple.strategy.str;

/**
 * 搜索策略
 *
 * @author Arvin
 * @time 2016/10/11$ 10:12$
 */
public interface Strategy {

    /**
     * 是否符合要求, 并返回相似度
     *
     * @param keyword      查询关键字
     * @param checkContent 要检查的内容
     * @return 如果符合要求，那么返回一个大于等于0且小于1的相似度值，否则返回小于0的值
     */
    float accept(String keyword, String checkContent);
}
