package com.github.cianfree.rtree.ip;

import java.util.List;

/**
 * @author Arvin
 * @time 2016/12/12 22:18
 */
public interface INode<T> {

    T getData();

    void setData(T data);

    INode<T> getParent();

    void setParent(INode<T> parent);

    List<T> getChildren();

    void setChildren(List<INode<T>> children);

    IRange getRange();

    void setRange(IRange range);
}
