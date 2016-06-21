package com.bxtpw.common.tree;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ��������������
 *
 * @param <T> �ڵ�Ԫ������
 * @param <K> �ڵ�Ԫ�ص�Ψһ�ؼ��ֵ�����
 * @author �ļ���
 * @version 0.1
 * @time 2015��12��3�� ����8:57:34
 * @since 0.1
 */
public class Tree<T, K> implements Serializable {

    private static final long serialVersionUID = -8212085253821857476L;

    /**
     * ����Map���洢����-����
     */
    private Map<K, T> pkMap = new HashMap<K, T>();

    /**
     * �洢������
     */
    private List<T> nodes;

    /**
     * ���Ƚڵ�
     */
    private T ancestor;

    /**
     * �ڵ��ȡ��
     */
    private NodeReader<T, K> reader;

    /**
     * �ڵ�����д����
     */
    private NodeWriter<T> writer;

    /**
     * �Ƚ���
     */
    private Comparator<T> comparator;

    public List<T> getNodes() {
        return nodes;
    }

    public Tree<T, K> setNodes(List<T> nodes) {
        this.nodes = nodes;
        return this;
    }

    public T getAncestor() {
        return ancestor;
    }

    public Tree<T, K> setAncestor(T ancestor) {
        this.ancestor = ancestor;
        return this;
    }

    public NodeReader<T, K> getReader() {
        return reader;
    }

    public Tree<T, K> setReader(NodeReader<T, K> reader) {
        this.reader = reader;
        return this;
    }

    public NodeWriter<T> getWriter() {
        return writer;
    }

    public Tree<T, K> setWriter(NodeWriter<T> writer) {
        this.writer = writer;
        return this;
    }

    public Comparator<T> getComparator() {
        return comparator;
    }

    public Tree<T, K> setComparator(Comparator<T> comparator) {
        this.comparator = comparator;
        return this;
    }

    /**
     * ��ȡ����
     *
     * @param primaryKey
     * @return
     * @author �ļ���
     * @time 2015��12��3�� ����9:03:37
     * @version 0.1
     * @since 0.1
     */
    public T getNode(K primaryKey) {
        return pkMap.get(primaryKey);
    }

}
