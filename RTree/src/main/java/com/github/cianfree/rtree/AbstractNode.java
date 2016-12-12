package com.github.cianfree.rtree;

import java.util.List;

/**
 * @author Arvin
 * @time 2016/12/12 9:57
 */
public abstract class AbstractNode<SELF> implements INode {

    private List<INode> children;

    private INode parent;

    public abstract SELF getSelf();

    @Override
    public List<INode> getChildren() {
        return children;
    }

    public SELF setChildren(List<INode> children) {
        this.children = children;
        return getSelf();
    }

    @Override
    public INode getParent() {
        return parent;
    }

    public SELF setParent(INode parent) {
        this.parent = parent;
        return getSelf();
    }
}
