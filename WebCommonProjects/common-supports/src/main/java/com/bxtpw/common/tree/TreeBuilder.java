package com.bxtpw.common.tree;

import java.util.*;

/**
 * 构造一个多叉树
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015年12月2日 下午3:50:23
 * @since 0.1
 */
public class TreeBuilder {

    private TreeBuilder() {
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
    public static final <T, K> Tree<T, K> buildTree(List<T> nodes) {
        NodeReader<T, K> reader = TreeFactory.createDefaultReader();
        return buildTree(nodes, reader);
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
    public static final <T, K> Tree<T, K> buildTree(List<T> nodes, NodeReader<T, K> reader) {
        return buildTree(nodes, reader, null, null);
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
    public static final <T, K> Tree<T, K> buildTree(List<T> nodes, NodeWriter<T> writer) {
        return buildTree(nodes, null, writer, null);
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
    public static final <T, K> Tree<T, K> buildTree(List<T> nodes, NodeReader<T, K> reader, NodeWriter<T> writer) {
        return buildTree(nodes, reader, writer, null);
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
    public static final <T, K> Tree<T, K> buildTree(List<T> nodes, Comparator<T> comparator) {
        NodeReader<T, K> reader = TreeFactory.createDefaultReader();
        return buildTree(nodes, reader, null, comparator);
    }

    /**
     * <pre>
     * 构建一棵多叉树:
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
    public static final <T, K> Tree<T, K> buildTree(List<T> nodes, NodeReader<T, K> reader, NodeWriter<T> writer, Comparator<T> comparator) {
        if (isEmpty(nodes)) {
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
        List<T> ancestors = getOrderedAncestorsList(nodes, reader, writer, comparator, //
                pkMap, //
                parentChildrenMap);
        if (isEmpty(ancestors)) {
            throw new NoneParentException();
        }
        if (ancestors.size() > 1) {
            throw new MultiParentException();
        }
        return new Tree<T, K>().setAncestor(ancestors.get(0)).setComparator(comparator).setReader(reader).setWriter(writer).setNodes(nodes);
    }

    /**
     * 返回有序的祖先节点列表
     *
     * @param nodes
     * @param reader
     * @param writer
     * @param comparator
     * @param pkMap
     * @param parentChildrenMap
     * @return
     * @author 夏集球
     * @time 2015年12月3日 上午9:28:34
     * @version 0.1
     * @since 0.1
     */
    public static final <T, K> List<T> getOrderedAncestorsList(List<T> nodes, NodeReader<T, K> reader, NodeWriter<T> writer, Comparator<T> comparator, Map<K, T> pkMap,
                                                               Map<K, List<T>> parentChildrenMap) {
        // 分配对象进入主键-对象map和父级-子级列表Map
        assignPkAndParentChildrenMap(nodes, reader, pkMap, parentChildrenMap);
        List<T> ancestors = findAncestorsFromNodes(nodes, reader, pkMap);
        // 处理管理，把parent和children关联上
        fixAncestorsRelationship(reader, writer, comparator, pkMap, parentChildrenMap, ancestors);
        return ancestors;
    }

    /**
     * 处理关系并排序
     *
     * @param reader
     * @param writer
     * @param comparator
     * @param pkMap
     * @param parentChildrenMap
     * @param ancestors
     * @author 夏集球
     * @time 2015年12月3日 上午9:25:14
     * @version 0.1
     * @since 0.1
     */
    private static <T, K> void fixAncestorsRelationship(NodeReader<T, K> reader, NodeWriter<T> writer, Comparator<T> comparator, Map<K, T> pkMap, Map<K, List<T>> parentChildrenMap,
                                                        List<T> ancestors) {
        if (isNotEmpty(ancestors)) {
            for (T ancestor : ancestors) {
                fixNodeElememtRelationship(ancestor, reader, writer, comparator, pkMap, parentChildrenMap);
            }
        }

        // 排序
        if (null != comparator && isNotEmpty(ancestors)) {
            Collections.sort(ancestors, comparator);
        }
    }

    /**
     * 查找祖先节点
     *
     * @param nodes
     * @param reader
     * @param pkMap
     * @return
     * @author 夏集球
     * @time 2015年12月3日 上午9:20:56
     * @version 0.1
     * @since 0.1
     */
    private static <T, K> List<T> findAncestorsFromNodes(List<T> nodes, NodeReader<T, K> reader, Map<K, T> pkMap) {
        List<T> ancestors = new ArrayList<T>();
        // 第二次遍历，计算始祖用户列表
        for (T node : nodes) {
            T parent = reader.getParent(node);
            if (null != parent) {
                T realParent = pkMap.get(reader.getPrimarkKey(parent));
                if (null == realParent) {
                    ancestors.add(node);
                }
            } else { // 为空，直接为始祖
                ancestors.add(node);
            }
        }
        return ancestors;
    }

    /**
     * 分配对象进入主键-对象map和父级-子级列表Map
     *
     * @param nodes
     * @param reader
     * @param pkMap
     * @param parentChildrenMap
     * @author 夏集球
     * @time 2015年12月3日 上午9:18:42
     * @version 0.1
     * @since 0.1
     */
    private static <K, T> void assignPkAndParentChildrenMap(List<T> nodes, NodeReader<T, K> reader, Map<K, T> pkMap, Map<K, List<T>> parentChildrenMap) {
        // 第一次遍历，初始化pkMap和parentChildrenMap
        for (T node : nodes) {
            // 放到pk-node中
            pkMap.put(reader.getPrimarkKey(node), node);
            // 放到parentChildrenMap中
            T parent = reader.getParent(node);
            if (null != parent) {
                K parentPk = reader.getPrimarkKey(parent);
                List<T> children = parentChildrenMap.get(parentPk);
                if (null == children) {
                    children = new ArrayList<T>();
                }
                children.add(node);
                parentChildrenMap.put(parentPk, children);
            }
        }
    }

    /**
     * 处理关系，把parent和children关联上
     *
     * @param ancestor
     * @param reader
     * @param writer
     * @param pkMap
     * @param parentChildrenMap
     * @author 夏集球
     * @time 2015年12月2日 下午5:08:10
     * @version 0.1
     * @since 0.1
     */
    private static <T, K> void fixNodeElememtRelationship(T ancestor, NodeReader<T, K> reader, NodeWriter<T> writer, Comparator<T> comparator, Map<K, T> pkMap,
                                                          Map<K, List<T>> parentChildrenMap) {
        if (null != ancestor) {
            // 处理parent对象
            T parent = reader.getParent(ancestor);
            if (null != parent) {
                T realParent = pkMap.get(reader.getPrimarkKey(parent));
                writer.setParent(ancestor, realParent);
            }
            // 处理子级列表
            K pk = reader.getPrimarkKey(ancestor);
            List<T> children = parentChildrenMap.get(pk);
            // 如果提供了比较器，进行排序
            if (null != comparator && isNotEmpty(children)) {
                Collections.sort(children, comparator);
            }
            writer.setChildren(ancestor, children);
            // 处理子级的关系
            if (isNotEmpty(children)) {
                for (T child : children) {
                    fixNodeElememtRelationship(child, reader, writer, comparator, pkMap, parentChildrenMap);
                }
            }
        }
    }

    /**
     * 是否为空的集合
     *
     * @param collections
     * @return
     * @author 夏集球
     * @time 2015年12月2日 下午4:55:58
     * @version 0.1
     * @since 0.1
     */
    public static final boolean isEmpty(Collection<?> collections) {
        return null == collections || collections.isEmpty();
    }

    /**
     * 判断集合是否不为空
     *
     * @param collections
     * @return
     * @author 夏集球
     * @time 2015年12月2日 下午4:56:41
     * @version 0.1
     * @since 0.1
     */
    public static final boolean isNotEmpty(Collection<?> collections) {
        return null != collections && !collections.isEmpty();
    }
}
