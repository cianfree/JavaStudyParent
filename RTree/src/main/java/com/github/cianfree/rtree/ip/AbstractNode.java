package com.github.cianfree.rtree.ip;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Arvin
 * @time 2016/12/13 9:47
 */
public abstract class AbstractNode<T> implements INode<T> {

    protected T data;

    protected INode<T> parent;

    protected final List<INode<T>> children = new LinkedList<>();

    protected IRange range;

    @Override
    public T getData() {
        return data;
    }

    @Override
    public void setData(T data) {
        this.data = data;
    }

    @Override
    public INode<T> getParent() {
        return parent;
    }

    @Override
    public void setParent(INode<T> parent) {
        this.parent = parent;
    }

    @Override
    public List<INode<T>> getChildren() {
        return children;
    }

    @Override
    public IRange getRange() {
        return range;
    }

    @Override
    public void setRange(IRange range) {
        this.range = range;
    }

    @Override
    public int compareTo(INode<T> o) {
        return this.getRange().compareTo(o.getRange());
    }
}
