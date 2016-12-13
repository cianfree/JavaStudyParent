package com.github.cianfree.rtree.ip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Arvin
 * @time 2016/12/13 9:41
 */
public class IPRTree {

    /** 孩子节点数量限制 */
    private final int childLimit;

    /** 根节点 */
    private INode<IPData> root;

    public IPRTree(Integer childLimit) {
        this.childLimit = null == childLimit || childLimit < 2 ? 2 : childLimit;
    }

    /**
     * 是否是空树
     */
    protected boolean isEmpty() {
        return null == root;
    }

    /**
     * <pre>
     * 插入节点：
     *  1. 向空树中插入节点，把要插入的节点作为根节点
     *  2. 非空树中插入，找到插入点，如果返回null，说明当前 Tree 没有包含指定的范围，重新建立一个新的根节点，把这两个节点添加到该根节点中
     *  3. 非空树中插入，找到插入点，返回不为null，检测该节点孩子节点是否会溢出
     *      3.1 未溢出，直接添加进去，根据 Range 对孩子节点进行排序，结束
     *      3.2 溢出，父节点分化出两个，再把孩子节点分化成两部分，重新设置分化出来的两个父节点的 Range， 同样的操作继续往上，直到根节点
     *
     * </pre>
     *
     * @param node 节点
     */
    public void insert(IPNode node) {
        if (isEmpty()) {    // 空树
            insertIntoEmptyTree(node);
        } else {
            // 选择要插入的节点
            INode<IPData> chooseNode = chooseInsertNode(this.root, node);
            insert(node, chooseNode);
        }
    }

    /**
     * 插入节点
     *
     * @param node       要插入的节点
     * @param chooseNode 插入点
     */
    private void insert(IPNode node, INode<IPData> chooseNode) {
        if (null == chooseNode) {  // 说明当前 Tree 没有包含新加入节点的范围，需要构造新的根节点，并把当前根节点和要添加的节点添加到新的根节点孩子列表下
            insertNotIncludeNode(node);
        } else {
            // 找到了最小 包含该范围的 节点
            List<INode<IPData>> children = chooseNode.getChildren();
            // 检测是否溢出
            if (children.size() < this.childLimit) { // 没有溢出，直接添加并进行排序
                children.add(node);
                node.setParent(chooseNode);
                Collections.sort(children); // 范围排序
            } else {    // 溢出了，需要进行分化节点
                insertForOverflow(chooseNode, node);
            }
        }
    }

    /**
     * 插入一个会溢出的节点，父节点分化出两个，再把孩子节点分化成两部分，重新设置分化出来的两个父节点的 Range， 同样的操作继续往上，直到根节点
     *
     * @param chooseNode 插入的位置
     * @param node       要插入的节点
     */
    private void insertForOverflow(INode<IPData> chooseNode, IPNode node) {
        // 节点分化
        IPNode divChooseNode = new IPNode();
        // 将孩子节点进行分化两个list
        List<INode<IPData>> allChildren = new ArrayList<>(chooseNode.getChildren());
        allChildren.add(node);
        Collections.sort(allChildren); // 排序
        // 将 children 拆分两份
        int endIndex = (allChildren.size() - 1) / 2;
        for (int i = 0; i <= endIndex; ++i) {
            INode<IPData> divNode = allChildren.get(i);
            divNode.setParent(divChooseNode);
            divChooseNode.getChildren().add(divNode);
        }
        // 设定范围
        divChooseNode.setRange(mergeRangeList(divChooseNode.getChildren()));

        // 先清空 选择的节点的 children
        chooseNode.getChildren().clear();
        for (int i = endIndex + 1; i < allChildren.size(); ++i) {
            INode<IPData> tempNode = allChildren.get(i);
            tempNode.setParent(chooseNode);
            chooseNode.getChildren().add(tempNode);
        }
        // 设定新的范围
        chooseNode.setRange(mergeRangeList(chooseNode.getChildren()));
        divChooseNode.setData(chooseNode.getData());
        // 继续把 divChooseNode 添加到 parent 节点中
        insert(divChooseNode, chooseNode.getParent());
    }

    /**
     * 合并多个节点的 Range
     *
     * @param nodeList 节点列表
     */
    private IRange mergeRangeList(List<INode<IPData>> nodeList) {
        if (nodeList.size() == 1) {
            return nodeList.get(0).getRange();
        }
        IRange range = nodeList.get(0).getRange();
        for (int i = 1; i < nodeList.size(); ++i) {
            range = range.merge(nodeList.get(i).getRange());
        }
        return range;
    }

    /**
     * 插入一个当前树不包含的范围节点：
     * <p/>
     * 需要构造新的根节点，并把当前根节点和要添加的节点添加到新的根节点孩子列表下
     *
     * @param node 要插入的节点
     */
    private void insertNotIncludeNode(IPNode node) {
        IPNode newRoot = new IPNode();
        List<INode<IPData>> children = newRoot.getChildren();
        children.add(this.root);
        children.add(node);

        // 把 parent 设置上
        this.root.setParent(newRoot);
        node.setParent(newRoot);

        // 排序，范围从小到大排序
        Collections.sort(children);
        // 设置新节点的范围
        IRange newRange = node.getRange().merge(this.root.getRange());
        newRoot.setRange(newRange);
        // 把根节点设置为新的根节点
        this.root = newRoot;
    }

    /**
     * 选择插入点
     *
     * @param lookupNode 节点
     * @param node       节点
     */
    private INode<IPData> chooseInsertNode(INode<IPData> lookupNode, IPNode node) {
        if (lookupNode == null) {
            throw new RuntimeException("Invalid not where choose insert node!");
        }
        if (lookupNode.getRange().include(node.getRange())) {
            // 相等范围或是包含了，继续往下找，找到合适的最小包含范围为止
            List<INode<IPData>> children = lookupNode.getChildren();
            if (null == children || children.isEmpty()) { // 没有孩子，说明需要添加到这个节点下
                return lookupNode;
            }
            // 有孩子，检测包含该节点的最小范围, node 只可能属于孩子中某一个，或都不属于
            for (INode<IPData> lookupChildNode : children) {
                INode<IPData> insertNodePoint = chooseInsertNode(lookupChildNode, node);
                if (null != insertNodePoint) {
                    return insertNodePoint;
                }
            }
            // 一个都没有找到，说明该范围已经是最小了的，插入点就是该节点
            return lookupNode;
        }
        return null;
    }

    /**
     * 插入到空树中
     *
     * @param node 要插入的节点
     */
    private void insertIntoEmptyTree(IPNode node) {
        this.root = node;
    }

    public IPData search(String ip) {
        long longIp = ipToLong(ip);
        return searchIp(this.root, new IPRange(longIp, longIp));
    }

    private IPData searchIp(INode<IPData> lookupNode, IPRange range) {
        System.out.println(lookupNode.getRange());
        if (lookupNode.getRange().include(range)) {
            List<INode<IPData>> children = lookupNode.getChildren();
            if (!children.isEmpty()) {
                for (INode<IPData> child : children) {
                    IPData data = searchIp(child, range);
                    if (null != data) {
                        return data;
                    }
                }
            }
            return lookupNode.getData();
        }
        return null;
    }

    private long ipToLong(String ip) {
        try {
            String[] array = ip.split("\\.");
            StringBuilder builder = new StringBuilder("1");
            for (String item : array) {
                if (item.length() == 1) {
                    builder.append("00").append(item);
                } else if (item.length() == 2) {
                    builder.append("0").append(item);
                } else {
                    builder.append(item);
                }
            }
            return Long.parseLong(builder.toString());
        } catch (Exception e) {
            System.out.println("IP: " + ip);
            throw new RuntimeException(e);
        }
    }
}
