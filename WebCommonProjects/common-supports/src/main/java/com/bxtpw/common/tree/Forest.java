package com.bxtpw.common.tree;

import java.io.Serializable;
import java.util.*;

/**
 * 泛型森林数据类型
 *
 * @param <T> 节点元素类型
 * @param <K> 节点元素的唯一关键字的类型
 * @author 夏集球
 * @version 0.1
 * @time 2015年12月3日 上午8:57:34
 * @since 0.1
 */
public class Forest<T, K> implements Serializable {

    private static final long serialVersionUID = -8212085253821857476L;

    /**
     * 构造Map，存储主键-对象
     */
    private Map<K, T> pkMap = new HashMap<>();

    /**
     * 存储的数据
     */
    private List<T> nodes;

    /**
     * 祖先节点列表
     */
    private List<T> ancestors;

    /**
     * 节点读取器
     */
    private NodeReader<T, K> reader;

    /**
     * 节点属性写入器
     */
    private NodeWriter<T> writer;

    /**
     * 比较器
     */
    private Comparator<T> comparator;

    public Map<K, T> getPkMap() {
        return pkMap;
    }

    public Forest<T, K> setPkMap(Map<K, T> pkMap) {
        this.pkMap = pkMap;
        return this;
    }

    public List<T> getNodes() {
        return nodes;
    }

    public Forest<T, K> setNodes(List<T> nodes) {
        this.nodes = nodes;
        return this;
    }

    public List<T> getAncestors() {
        return ancestors;
    }

    public Forest<T, K> setAncestors(List<T> ancestors) {
        this.ancestors = ancestors;
        return this;
    }

    public NodeReader<T, K> getReader() {
        return reader;
    }

    public Forest<T, K> setReader(NodeReader<T, K> reader) {
        this.reader = reader;
        return this;
    }

    public NodeWriter<T> getWriter() {
        return writer;
    }

    public Forest<T, K> setWriter(NodeWriter<T> writer) {
        this.writer = writer;
        return this;
    }

    public Comparator<T> getComparator() {
        return comparator;
    }

    public Forest<T, K> setComparator(Comparator<T> comparator) {
        this.comparator = comparator;
        // 如果不为空，那么需要进行排序
        if (null != comparator) {
            List<T> nodes = new ArrayList<T>();
            if (null != this.ancestors) {
                for (T ancestor : this.ancestors) {
                    List<T> subNodes = TreeIterator.treeToList(ancestor);
                    if (null != subNodes && !subNodes.isEmpty()) {
                        nodes.addAll(subNodes);
                    }
                }
                this.nodes = nodes;
            }
        }
        return this;

    }

    /**
     * 获取主键
     *
     * @param primaryKey
     * @return
     * @author 夏集球
     * @time 2015年12月3日 上午9:03:37
     * @version 0.1
     * @since 0.1
     */
    public T getNode(K primaryKey) {
        return pkMap.get(primaryKey);
    }

}
