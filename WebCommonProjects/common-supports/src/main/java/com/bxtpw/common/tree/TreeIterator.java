package com.bxtpw.common.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * ������ı���
 *
 * @author �ļ���
 * @version 0.1
 * @time 2015��12��2�� ����3:44:57
 * @since 0.1
 */
public class TreeIterator {

    private TreeIterator() {
    }

    /**
     * ��ȱ���
     *
     * @param ancestor
     * @param handler
     * @author �ļ���
     * @time 2015��12��2�� ����5:25:23
     * @version 0.1
     * @since 0.1
     */
    public static final <T, K> void iterWithWide(T ancestor, NodeHandler<T> handler) {
        NodeReader<T, K> reader = TreeFactory.createDefaultReader();
        iterWithWide(ancestor, reader, handler);
    }

    /**
     * ��ȱ���
     *
     * @param ancestor
     * @param reader
     * @param handler
     * @author �ļ���
     * @time 2015��12��2�� ����5:25:52
     * @version 0.1
     * @since 0.1
     */
    public static final <T, K> void iterWithWide(T ancestor, NodeReader<T, K> reader, NodeHandler<T> handler) {
        iterWithWide(ancestor, reader, null, handler);
    }

    /**
     * ��ȱ���
     *
     * @param ancestor
     * @param reader
     * @param handler
     * @author �ļ���
     * @time 2015��12��2�� ����5:25:52
     * @version 0.1
     * @since 0.1
     */
    public static final <T, K> void iterWithWide(T ancestor, NodeFilter<T> filter, NodeHandler<T> handler) {
        iterWithWide(ancestor, null, filter, handler);
    }

    /**
     * ��ȱ���
     *
     * @param ancestor
     * @param reader
     * @param handler
     * @author �ļ���
     * @time 2015��12��2�� ����5:25:52
     * @version 0.1
     * @since 0.1
     */
    public static final <T, K> void iterWithWide(T ancestor, NodeReader<T, K> reader, NodeFilter<T> filter, NodeHandler<T> handler) {
        if (null != ancestor && !isFilter(ancestor, filter)) {
            List<T> parentList = new ArrayList<T>();
            parentList.add(ancestor);
            List<T> childrensList = new ArrayList<T>();
            int iterLevel = 1;
            while (!parentList.isEmpty()) {
                for (T parentNode : parentList) {
                    handleElement(parentNode, handler, iterLevel);
                    addNodeChildrenToList(parentNode, childrensList, filter, reader);
                    ;
                }
                parentList.clear();
                parentList.addAll(childrensList);
                childrensList.clear();
                ++iterLevel;
            }
        }
    }

    /**
     * ����Ӽ����󵽶�����
     *
     * @param node
     * @param list
     * @param filter
     * @param reader
     * @author �ļ���
     * @time 2015��12��2�� ����5:43:15
     * @version 0.1
     * @since 0.1
     */
    private static final <T, K> void addNodeChildrenToList(T node, List<T> list, NodeFilter<T> filter, NodeReader<T, K> reader) {
        if (null != node) {
            List<T> children = reader.getChildren(node);
            if (isNotEmpty(children)) {
                for (T child : children) {
                    if (!isFilter(child, filter)) {
                        list.add(child);
                    }
                }
            }
        }
    }

    /**
     * �����߼�
     *
     * @param node
     * @param handler
     * @param iterLevel �������
     * @author �ļ���
     * @time 2015��12��2�� ����5:41:54
     * @version 0.1
     * @since 0.1
     */
    private static <T> void handleElement(T node, NodeHandler<T> handler, int iterLevel) {
        if (null != handler) {
            handler.handle(node, iterLevel);
        }
    }

    /**
     * �Ƿ����
     *
     * @param node
     * @param filter
     * @return
     * @author �ļ���
     * @time 2015��12��2�� ����5:40:34
     * @version 0.1
     * @since 0.1
     */
    private static final <T> boolean isFilter(T node, NodeFilter<T> filter) {
        return null != filter && filter.filter(node);
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
    private static final boolean isNotEmpty(Collection<?> collections) {
        return null != collections && !collections.isEmpty();
    }

    /**
     * ��ȱ���
     *
     * @param ancestor
     * @param handler
     * @author �ļ���
     * @time 2015��12��2�� ����5:25:23
     * @version 0.1
     * @since 0.1
     */
    public static final <T, K> void iterWithDeep(T ancestor, NodeHandler<T> handler) {
        NodeReader<T, K> reader = TreeFactory.createDefaultReader();
        iterWithDeep(ancestor, reader, handler);
    }

    /**
     * ��ȱ���
     *
     * @param ancestor
     * @param reader
     * @param handler
     * @author �ļ���
     * @time 2015��12��2�� ����5:25:52
     * @version 0.1
     * @since 0.1
     */
    public static final <T, K> void iterWithDeep(T ancestor, NodeReader<T, K> reader, NodeHandler<T> handler) {
        iterWithDeep(ancestor, reader, null, handler);
    }

    /**
     * ��ȱ���
     *
     * @param ancestor
     * @param reader
     * @param handler
     * @author �ļ���
     * @time 2015��12��2�� ����5:25:52
     * @version 0.1
     * @since 0.1
     */
    public static final <T, K> void iterWithDeep(T ancestor, NodeFilter<T> filter, NodeHandler<T> handler) {
        iterWithDeep(ancestor, null, filter, handler);
    }

    /**
     * ��ȱ���
     *
     * @param ancestor
     * @param reader
     * @param handler
     * @author �ļ���
     * @time 2015��12��2�� ����5:25:52
     * @version 0.1
     * @since 0.1
     */
    public static final <T, K> void iterWithDeep(T ancestor, NodeReader<T, K> reader, NodeFilter<T> filter, NodeHandler<T> handler) {
        iterWithDeep(ancestor, reader, filter, handler, 1);
    }

    /**
     * ��ȱ���
     *
     * @param ancestor
     * @param reader
     * @param filter
     * @param handler
     * @param iterLevel
     * @author �ļ���
     * @time 2015��12��2�� ����6:25:19
     * @version 0.1
     * @since 0.1
     */
    private static final <T, K> void iterWithDeep(T ancestor, NodeReader<T, K> reader, NodeFilter<T> filter, NodeHandler<T> handler, int iterLevel) {
        if (null != ancestor && !isFilter(ancestor, filter)) {
            handleElement(ancestor, handler, iterLevel); // ����ڵ�
            // ��ȱ�������
            List<T> children = reader.getChildren(ancestor);
            if (isNotEmpty(children)) { // ��������
                for (T child : children) {
                    iterWithDeep(child, reader, filter, handler, ++iterLevel);
                }
            }
        }
    }

    /**
     * �������
     *
     * @param ancestor
     * @param accepter
     * @return
     * @author �ļ���
     * @time 2015��12��2�� ����8:41:50
     * @version 0.1
     * @since 0.1
     */
    public static final <T, K> List<T> findNodes(final T ancestor, final NodeAccepter<T> accepter) {
        return findNodes(ancestor, null, null, accepter);
    }

    /**
     * �������
     *
     * @param ancestor
     * @param reader
     * @param accepter
     * @return
     * @author �ļ���
     * @time 2015��12��2�� ����8:40:25
     * @version 0.1
     * @since 0.1
     */
    public static final <T, K> List<T> findNodes(final T ancestor, final NodeFilter<T> filter, final NodeAccepter<T> accepter) {
        return findNodes(ancestor, null, filter, accepter);
    }

    /**
     * �������
     *
     * @param ancestor
     * @param reader
     * @param accepter
     * @return
     * @author �ļ���
     * @time 2015��12��2�� ����8:40:25
     * @version 0.1
     * @since 0.1
     */
    public static final <T, K> List<T> findNodes(final T ancestor, final NodeReader<T, K> reader, final NodeAccepter<T> accepter) {
        return findNodes(ancestor, reader, null, accepter);
    }

    /**
     * �������������Ľڵ�
     *
     * @param ancestor
     * @param reader
     * @param accepter
     * @return
     * @author �ļ���
     * @time 2015��12��2�� ����8:36:31
     * @version 0.1
     * @since 0.1
     */
    public static final <T, K> List<T> findNodes(final T ancestor, final NodeReader<T, K> reader, final NodeFilter<T> filter, final NodeAccepter<T> accepter) {
        final List<T> resultList = new ArrayList<T>();
        iterWithWide(ancestor, filter, new NodeHandler<T>() {

            private static final long serialVersionUID = 1L;

            @Override
            public void handle(T node, int iterLevel) {
                if (isAccept(node, accepter)) {
                    resultList.add(node);
                }
            }
        });
        return resultList;
    }

    /**
     * �Ƿ����
     *
     * @param node
     * @param filter
     * @return
     * @author �ļ���
     * @time 2015��12��2�� ����5:40:34
     * @version 0.1
     * @since 0.1
     */
    private static final <T> boolean isAccept(T node, NodeAccepter<T> accepter) {
        return null == accepter || accepter.accept(node);
    }

    /**
     * ת����List
     *
     * @param ancestor
     * @return
     * @author �ļ���
     * @time 2015��12��2�� ����8:43:32
     * @version 0.1
     * @since 0.1
     */
    public static final <T> List<T> treeToList(T ancestor) {
        final List<T> resultList = new ArrayList<T>();
        iterWithWide(ancestor, new NodeHandler<T>() {

            private static final long serialVersionUID = 1L;

            @Override
            public void handle(T node, int iterLevel) {
                resultList.add(node);
            }
        });
        return resultList;
    }
}
