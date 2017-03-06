package com.github.search.simple.strategy.element;

import com.github.search.simple.model.Element;

/**
 * 元素相似度计算
 *
 * @author Arvin
 * @time 2016/12/27 19:23
 */
public interface ElementStrategy {

    /**
     * 是否符合要求, 并返回相似度
     *
     * @param keyword 查询关键字
     * @param element 要检查的元素
     * @return 如果符合要求，那么返回一个大于等于0且小于1的相似度值，否则返回小于0的值
     */
    float accept(String keyword, Element element);
}
