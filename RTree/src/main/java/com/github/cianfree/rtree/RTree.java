package com.github.cianfree.rtree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The R-Tree
 *
 * @author Arvin
 * @time 2016/12/12 9:55
 */
public class RTree implements IRTree {

    /** 根节点 */
    private INode root;

    /** 孩子节点数量限制 */
    private final int childLimit;

    public RTree(int childLimit) {
        this.childLimit = childLimit < 1 ? 3 : childLimit;
    }

    /**
     * 根据要插入的节点，选择一个要插入的节点位置
     *
     * @param checkNode 根节点
     * @param node      要插入的节点
     * @return 如果为空，返回null, 最终会返回一个叶子节点
     */
    private INode chooseLeafNode(INode checkNode, LeafNode node) {
        if (null == checkNode) {
            return null;
        }
        if (checkNode instanceof LeafNode) { // 叶子节点，直接返回
            return checkNode;
        }
        // 如果不是叶子节点，判断要插入的节点和该节点的子节点进行性质比较
        List<INode> childNodes = checkNode.getChildren();
        // 如果没有孩子节点，直接返回
        if (Utils.isEmpty(childNodes)) {
            return checkNode;
        }
        // 如果有孩子，遍历孩子，先判断是什么类型的孩子，如果是叶子节点，返回第一个，否则继续递归查找，判断属于哪一个node
        for (INode treeNode : childNodes) {
            if (treeNode instanceof LeafNode) {
                return treeNode;
            }
            // 判断 要插入的节点是否和当前节点具有同样的性质，如果有则继续递归检查
            if (treeNode.include(node)) {
                // 当前遍历节点，包含了要插入的节点，继续检查最小包含节点
                return chooseLeafNode(treeNode, node);
            }
        }
        // 发现没有一个节点符合，返回 parent 的范围
        return checkNode;
    }


    @Override
    public INode search(LeafNode node) {
        return null;
    }

    @Override
    public void delete(LeafNode node) {

    }

    @Override
    public void insert(LeafNode node) {
        // 选择插入的节点
        INode chooseNode = chooseLeafNode(this.root, node);
        // 如果是null，说明是空的 tree ，需要创建
        if (null == chooseNode) {
            RangeNode rangeNode = new RangeNode();
            List<INode> children = new ArrayList<>();
            children.add(node);
            rangeNode.setChildren(children);
            node.setParent(rangeNode);
            // 设置范围
            rangeNode.setRange(node.getRange());
            this.root = rangeNode;
        } else {
            // chooseNode 肯定是叶子节点，因此下面这个肯定不为空
            RangeNode parentNode = RangeNode.class.cast(chooseNode.getParent());
            // 检查孩子数量
            List<INode> children = parentNode.getChildren();
            if (null == children) {
                children = new LinkedList<>();
                parentNode.setChildren(children);
            }
            if (children.size() < this.childLimit) {    // 数量小于要限制的数量，可直接添加到有序列表中
                children.add(node);
                // 范围合并
            } else {// 数量大于限制数量，溢出了，需要重新整理树，分化节点

            }
        }

    }
}
