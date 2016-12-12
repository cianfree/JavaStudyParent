package com.github.cianfree.rtree;

/**
 * R-Tree 的叶子节点
 *
 * @author Arvin
 * @time 2016/12/12 9:51
 */
public class LeafNode extends AbstractNode<LeafNode> {
    @Override
    public LeafNode getSelf() {
        return this;
    }

    @Override
    public boolean include(INode node) {
        return false;
    }
}
