package com.bxtpw.common.tree;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 构造森林
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015年12月2日 下午3:50:23
 * @since 0.1
 */
public class ForestBuilder {

    private ForestBuilder() {
    }

    /**
     * 使用默认的Reader
     *
     * @param nodes
     * @return
     * @author 夏集球
     * @time 2015年12月2日 下午4:44:32
     * @version 0.1
     * @since 0.1
     */
    public static <T, K> Forest<T, K> buildForest(List<T> nodes) {
        NodeReader<T, K> reader = TreeFactory.createDefaultReader();
        return buildForest(nodes, reader);
    }

    /**
     * <pre>
     * 构建一棵多叉树, 不支持排序:
     * 返回在给定的数据中没有父级节点的元素列表，并且该父级节点能够读取孩子列表
     *
     * </pre>
     *
     * @param nodes
     * @param reader
     * @return
     * @author 夏集球
     * @time 2015年12月2日 下午4:06:54
     * @version 0.1
     * @since 0.1
     */
    public static <T, K> Forest<T, K> buildForest(List<T> nodes, NodeReader<T, K> reader) {
        return buildForest(nodes, reader, null, null);
    }

    /**
     * 不支持排序的树构建
     *
     * @param nodes
     * @param writer
     * @return
     * @author 夏集球
     * @time 2015年12月2日 下午5:16:02
     * @version 0.1
     * @since 0.1
     */
    public static <T, K> Forest<T, K> buildForest(List<T> nodes, NodeWriter<T> writer) {
        return buildForest(nodes, null, writer, null);
    }

    /**
     * 不支持排序的树构建
     *
     * @param nodes
     * @param reader
     * @param writer
     * @return
     * @author 夏集球
     * @time 2015年12月2日 下午5:16:02
     * @version 0.1
     * @since 0.1
     */
    public static <T, K> Forest<T, K> buildForest(List<T> nodes, NodeReader<T, K> reader, NodeWriter<T> writer) {
        return buildForest(nodes, reader, writer, null);
    }

    /**
     * <pre>
     * 构建一棵多叉树, 支持排序，但是未提供读取器
     *
     * </pre>
     *
     * @param nodes
     * @param comparator
     * @return
     * @author 夏集球
     * @time 2015年12月2日 下午4:50:42
     * @version 0.1
     * @since 0.1
     */
    public static <T, K> Forest<T, K> buildForest(List<T> nodes, Comparator<T> comparator) {
        NodeReader<T, K> reader = TreeFactory.createDefaultReader();
        return buildForest(nodes, reader, null, comparator);
    }

    /**
     * <pre>
     * 构建森林:
     * 返回在给定的数据中没有父级节点的元素列表，并且该父级节点能够读取孩子列表
     *
     * </pre>
     *
     * @param nodes
     * @param reader
     * @param comparator
     * @return
     * @author 夏集球
     * @time 2015年12月2日 下午3:54:00
     * @version 0.1
     * @since 0.1
     */
    public static <T, K> Forest<T, K> buildForest(List<T> nodes, NodeReader<T, K> reader, NodeWriter<T> writer, Comparator<T> comparator) {
        if (TreeBuilder.isEmpty(nodes)) {
            return null;
        }
        if (null == reader) {
            reader = TreeFactory.createDefaultReader();
        }
        if (null == writer) {
            writer = TreeFactory.createDefaultWriter();
        }
        // 构造Map，存储主键-对象
        Map<K, T> pkMap = new HashMap<K, T>();
        // 构造Map， 存储指定对象主键的孩子列表
        Map<K, List<T>> parentChildrenMap = new HashMap<K, List<T>>();
        // 获取排好序的祖先节点列表
        List<T> ancestors = TreeBuilder.getOrderedAncestorsList(nodes, reader, writer, comparator, pkMap, parentChildrenMap);
        return new Forest<T, K>().setPkMap(pkMap).setNodes(nodes).setAncestors(ancestors).setReader(reader).setWriter(writer).setComparator(comparator);
    }

}
