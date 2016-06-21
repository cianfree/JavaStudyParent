package com.bxtpw.common.tree;

import java.io.Serializable;
import java.util.*;

/**
 * ����ɭ����������
 *
 * @param <T> �ڵ�Ԫ������
 * @param <K> �ڵ�Ԫ�ص�Ψһ�ؼ��ֵ�����
 * @author �ļ���
 * @version 0.1
 * @time 2015��12��3�� ����8:57:34
 * @since 0.1
 */
public class Forest<T, K> implements Serializable {

    private static final long serialVersionUID = -8212085253821857476L;

    /**
     * ����Map���洢����-����
     */
    private Map<K, T> pkMap = new HashMap<>();

    /**
     * �洢������
     */
    private List<T> nodes;

    /**
     * ���Ƚڵ��б�
     */
    private List<T> ancestors;

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
        // �����Ϊ�գ���ô��Ҫ��������
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
