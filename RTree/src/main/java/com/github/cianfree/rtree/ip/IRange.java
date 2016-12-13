package com.github.cianfree.rtree.ip;

/**
 * @author Arvin
 * @time 2016/12/12 22:19
 */
public interface IRange extends Comparable<IRange> {

    IRange merge(IRange range);

    boolean include(IRange range);
}
