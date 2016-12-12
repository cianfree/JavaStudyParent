package com.github.cianfree.rtree;

/**
 * R-Tree 的操作
 *
 * @author Arvin
 * @time 2016/12/12 9:58
 */
public interface IRTree {

    /**
     * 插入节点
     *
     * @param node 节点
     */
    void insert(LeafNode node);

    /**
     * 删除指定节点
     *
     * @param node 要删除的节点
     */
    void delete(LeafNode node);

    /**
     * 搜索指定节点对应的节点
     *
     * @param node 要搜索的节点
     */
    INode search(LeafNode node);

}
