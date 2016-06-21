package com.bxtpw.common.tree;

import java.util.*;

/**
 * ����һ�������
 *
 * @author �ļ���
 * @version 0.1
 * @time 2015��12��2�� ����3:50:23
 * @since 0.1
 */
public class TreeBuilder {

    private TreeBuilder() {
    }

    /**
     * ʹ��Ĭ�ϵ�Reader
     *
     * @param nodes
     * @return
     * @author �ļ���
     * @time 2015��12��2�� ����4:44:32
     * @version 0.1
     * @since 0.1
     */
    public static final <T, K> Tree<T, K> buildTree(List<T> nodes) {
        NodeReader<T, K> reader = TreeFactory.createDefaultReader();
        return buildTree(nodes, reader);
    }

    /**
     * <pre>
     * ����һ�ö����, ��֧������:
     * �����ڸ�����������û�и����ڵ��Ԫ���б����Ҹø����ڵ��ܹ���ȡ�����б�
     *
     * </pre>
     *
     * @param nodes
     * @param reader
     * @return
     * @author �ļ���
     * @time 2015��12��2�� ����4:06:54
     * @version 0.1
     * @since 0.1
     */
    public static final <T, K> Tree<T, K> buildTree(List<T> nodes, NodeReader<T, K> reader) {
        return buildTree(nodes, reader, null, null);
    }

    /**
     * ��֧�������������
     *
     * @param nodes
     * @param writer
     * @return
     * @author �ļ���
     * @time 2015��12��2�� ����5:16:02
     * @version 0.1
     * @since 0.1
     */
    public static final <T, K> Tree<T, K> buildTree(List<T> nodes, NodeWriter<T> writer) {
        return buildTree(nodes, null, writer, null);
    }

    /**
     * ��֧�������������
     *
     * @param nodes
     * @param reader
     * @param writer
     * @return
     * @author �ļ���
     * @time 2015��12��2�� ����5:16:02
     * @version 0.1
     * @since 0.1
     */
    public static final <T, K> Tree<T, K> buildTree(List<T> nodes, NodeReader<T, K> reader, NodeWriter<T> writer) {
        return buildTree(nodes, reader, writer, null);
    }

    /**
     * <pre>
     * ����һ�ö����, ֧�����򣬵���δ�ṩ��ȡ��
     *
     * </pre>
     *
     * @param nodes
     * @param comparator
     * @return
     * @author �ļ���
     * @time 2015��12��2�� ����4:50:42
     * @version 0.1
     * @since 0.1
     */
    public static final <T, K> Tree<T, K> buildTree(List<T> nodes, Comparator<T> comparator) {
        NodeReader<T, K> reader = TreeFactory.createDefaultReader();
        return buildTree(nodes, reader, null, comparator);
    }

    /**
     * <pre>
     * ����һ�ö����:
     * �����ڸ�����������û�и����ڵ��Ԫ���б����Ҹø����ڵ��ܹ���ȡ�����б�
     *
     * </pre>
     *
     * @param nodes
     * @param reader
     * @param comparator
     * @return
     * @author �ļ���
     * @time 2015��12��2�� ����3:54:00
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
        // ����Map���洢����-����
        Map<K, T> pkMap = new HashMap<K, T>();
        // ����Map�� �洢ָ�����������ĺ����б�
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
     * ������������Ƚڵ��б�
     *
     * @param nodes
     * @param reader
     * @param writer
     * @param comparator
     * @param pkMap
     * @param parentChildrenMap
     * @return
     * @author �ļ���
     * @time 2015��12��3�� ����9:28:34
     * @version 0.1
     * @since 0.1
     */
    public static final <T, K> List<T> getOrderedAncestorsList(List<T> nodes, NodeReader<T, K> reader, NodeWriter<T> writer, Comparator<T> comparator, Map<K, T> pkMap,
                                                               Map<K, List<T>> parentChildrenMap) {
        // ��������������-����map�͸���-�Ӽ��б�Map
        assignPkAndParentChildrenMap(nodes, reader, pkMap, parentChildrenMap);
        List<T> ancestors = findAncestorsFromNodes(nodes, reader, pkMap);
        // ���������parent��children������
        fixAncestorsRelationship(reader, writer, comparator, pkMap, parentChildrenMap, ancestors);
        return ancestors;
    }

    /**
     * �����ϵ������
     *
     * @param reader
     * @param writer
     * @param comparator
     * @param pkMap
     * @param parentChildrenMap
     * @param ancestors
     * @author �ļ���
     * @time 2015��12��3�� ����9:25:14
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

        // ����
        if (null != comparator && isNotEmpty(ancestors)) {
            Collections.sort(ancestors, comparator);
        }
    }

    /**
     * �������Ƚڵ�
     *
     * @param nodes
     * @param reader
     * @param pkMap
     * @return
     * @author �ļ���
     * @time 2015��12��3�� ����9:20:56
     * @version 0.1
     * @since 0.1
     */
    private static <T, K> List<T> findAncestorsFromNodes(List<T> nodes, NodeReader<T, K> reader, Map<K, T> pkMap) {
        List<T> ancestors = new ArrayList<T>();
        // �ڶ��α���������ʼ���û��б�
        for (T node : nodes) {
            T parent = reader.getParent(node);
            if (null != parent) {
                T realParent = pkMap.get(reader.getPrimarkKey(parent));
                if (null == realParent) {
                    ancestors.add(node);
                }
            } else { // Ϊ�գ�ֱ��Ϊʼ��
                ancestors.add(node);
            }
        }
        return ancestors;
    }

    /**
     * ��������������-����map�͸���-�Ӽ��б�Map
     *
     * @param nodes
     * @param reader
     * @param pkMap
     * @param parentChildrenMap
     * @author �ļ���
     * @time 2015��12��3�� ����9:18:42
     * @version 0.1
     * @since 0.1
     */
    private static <K, T> void assignPkAndParentChildrenMap(List<T> nodes, NodeReader<T, K> reader, Map<K, T> pkMap, Map<K, List<T>> parentChildrenMap) {
        // ��һ�α�������ʼ��pkMap��parentChildrenMap
        for (T node : nodes) {
            // �ŵ�pk-node��
            pkMap.put(reader.getPrimarkKey(node), node);
            // �ŵ�parentChildrenMap��
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
     * �����ϵ����parent��children������
     *
     * @param ancestor
     * @param reader
     * @param writer
     * @param pkMap
     * @param parentChildrenMap
     * @author �ļ���
     * @time 2015��12��2�� ����5:08:10
     * @version 0.1
     * @since 0.1
     */
    private static <T, K> void fixNodeElememtRelationship(T ancestor, NodeReader<T, K> reader, NodeWriter<T> writer, Comparator<T> comparator, Map<K, T> pkMap,
                                                          Map<K, List<T>> parentChildrenMap) {
        if (null != ancestor) {
            // ����parent����
            T parent = reader.getParent(ancestor);
            if (null != parent) {
                T realParent = pkMap.get(reader.getPrimarkKey(parent));
                writer.setParent(ancestor, realParent);
            }
            // �����Ӽ��б�
            K pk = reader.getPrimarkKey(ancestor);
            List<T> children = parentChildrenMap.get(pk);
            // ����ṩ�˱Ƚ�������������
            if (null != comparator && isNotEmpty(children)) {
                Collections.sort(children, comparator);
            }
            writer.setChildren(ancestor, children);
            // �����Ӽ��Ĺ�ϵ
            if (isNotEmpty(children)) {
                for (T child : children) {
                    fixNodeElememtRelationship(child, reader, writer, comparator, pkMap, parentChildrenMap);
                }
            }
        }
    }

    /**
     * �Ƿ�Ϊ�յļ���
     *
     * @param collections
     * @return
     * @author �ļ���
     * @time 2015��12��2�� ����4:55:58
     * @version 0.1
     * @since 0.1
     */
    public static final boolean isEmpty(Collection<?> collections) {
        return null == collections || collections.isEmpty();
    }

    /**
     * �жϼ����Ƿ�Ϊ��
     *
     * @param collections
     * @return
     * @author �ļ���
     * @time 2015��12��2�� ����4:56:41
     * @version 0.1
     * @since 0.1
     */
    public static final boolean isNotEmpty(Collection<?> collections) {
        return null != collections && !collections.isEmpty();
    }
}
