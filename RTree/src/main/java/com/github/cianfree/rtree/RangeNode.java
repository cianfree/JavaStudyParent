package com.github.cianfree.rtree;

/**
 * R-Tree Range node 范围节点
 *
 * @author Arvin
 * @time 2016/12/12 9:49
 */
public class RangeNode extends AbstractNode<RangeNode> {
    @Override
    public RangeNode getSelf() {
        return this;
    }

    @Override
    public boolean include(INode node) {
        return false;
    }
}
