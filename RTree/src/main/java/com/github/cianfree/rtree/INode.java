package com.github.cianfree.rtree;

import java.util.List;

/**
 * R-Tree node interface
 *
 * @author Arvin
 * @time 2016/12/12 9:48
 */
public interface INode {

    /**
     * 获取孩子节点列表
     */
    List<INode> getChildren();


    /**
     * 检测一个节点是个包含另一个节点
     *
     * @param node 目标节点
     */
    boolean include(INode node);

    /**
     * 获取父节点， 如果没有就返回null
     */
    INode getParent();
}
