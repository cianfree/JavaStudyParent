package com.bxtpw.common.tree;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ����ɭ��
 *
 * @author �ļ���
 * @version 0.1
 * @time 2015��12��2�� ����3:50:23
 * @since 0.1
 */
public class ForestBuilder {

    private ForestBuilder() {
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
    public static <T, K> Forest<T, K> buildForest(List<T> nodes) {
        NodeReader<T, K> reader = TreeFactory.createDefaultReader();
        return buildForest(nodes, reader);
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
    public static <T, K> Forest<T, K> buildForest(List<T> nodes, NodeReader<T, K> reader) {
        return buildForest(nodes, reader, null, null);
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
    public static <T, K> Forest<T, K> buildForest(List<T> nodes, NodeWriter<T> writer) {
        return buildForest(nodes, null, writer, null);
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
    public static <T, K> Forest<T, K> buildForest(List<T> nodes, NodeReader<T, K> reader, NodeWriter<T> writer) {
        return buildForest(nodes, reader, writer, null);
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
    public static <T, K> Forest<T, K> buildForest(List<T> nodes, Comparator<T> comparator) {
        NodeReader<T, K> reader = TreeFactory.createDefaultReader();
        return buildForest(nodes, reader, null, comparator);
    }

    /**
     * <pre>
     * ����ɭ��:
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
        // ����Map���洢����-����
        Map<K, T> pkMap = new HashMap<K, T>();
        // ����Map�� �洢ָ�����������ĺ����б�
        Map<K, List<T>> parentChildrenMap = new HashMap<K, List<T>>();
        // ��ȡ�ź�������Ƚڵ��б�
        List<T> ancestors = TreeBuilder.getOrderedAncestorsList(nodes, reader, writer, comparator, pkMap, parentChildrenMap);
        return new Forest<T, K>().setPkMap(pkMap).setNodes(nodes).setAncestors(ancestors).setReader(reader).setWriter(writer).setComparator(comparator);
    }

}
