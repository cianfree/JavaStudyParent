package com.bxtpw.common.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 多叉树的遍历
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015年12月2日 下午3:44:57
 * @since 0.1
 */
public class TreeIterator {

    private TreeIterator() {
    }

    /**
     * 广度遍历
     *
     * @param ancestor
     * @param handler
     * @author 夏集球
     * @time 2015年12月2日 下午5:25:23
     * @version 0.1
     * @since 0.1
     */
    public static final <T, K> void iterWithWide(T ancestor, NodeHandler<T> handler) {
        NodeReader<T, K> reader = TreeFactory.createDefaultReader();
        iterWithWide(ancestor, reader, handler);
    }

    /**
     * 广度遍历
     *
     * @param ancestor
     * @param reader
     * @param handler
     * @author 夏集球
     * @time 2015年12月2日 下午5:25:52
     * @version 0.1
     * @since 0.1
     */
    public static final <T, K> void iterWithWide(T ancestor, NodeReader<T, K> reader, NodeHandler<T> handler) {
        iterWithWide(ancestor, reader, null, handler);
    }

    /**
     * 广度遍历
     *
     * @param ancestor
     * @param reader
     * @param handler
     * @author 夏集球
     * @time 2015年12月2日 下午5:25:52
     * @version 0.1
     * @since 0.1
     */
    public static final <T, K> void iterWithWide(T ancestor, NodeFilter<T> filter, NodeHandler<T> handler) {
        iterWithWide(ancestor, null, filter, handler);
    }

    /**
     * 广度遍历
     *
     * @param ancestor
     * @param reader
     * @param handler
     * @author 夏集球
     * @time 2015年12月2日 下午5:25:52
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
     * 添加子级对象到队列中
     *
     * @param node
     * @param list
     * @param filter
     * @param reader
     * @author 夏集球
     * @time 2015年12月2日 下午5:43:15
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
     * 处理逻辑
     *
     * @param node
     * @param handler
     * @param iterLevel 遍历层次
     * @author 夏集球
     * @time 2015年12月2日 下午5:41:54
     * @version 0.1
     * @since 0.1
     */
    private static <T> void handleElement(T node, NodeHandler<T> handler, int iterLevel) {
        if (null != handler) {
            handler.handle(node, iterLevel);
        }
    }

    /**
     * 是否过滤
     *
     * @param node
     * @param filter
     * @return
     * @author 夏集球
     * @time 2015年12月2日 下午5:40:34
     * @version 0.1
     * @since 0.1
     */
    private static final <T> boolean isFilter(T node, NodeFilter<T> filter) {
        return null != filter && filter.filter(node);
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
    private static final boolean isNotEmpty(Collection<?> collections) {
        return null != collections && !collections.isEmpty();
    }

    /**
     * 广度遍历
     *
     * @param ancestor
     * @param handler
     * @author 夏集球
     * @time 2015年12月2日 下午5:25:23
     * @version 0.1
     * @since 0.1
     */
    public static final <T, K> void iterWithDeep(T ancestor, NodeHandler<T> handler) {
        NodeReader<T, K> reader = TreeFactory.createDefaultReader();
        iterWithDeep(ancestor, reader, handler);
    }

    /**
     * 广度遍历
     *
     * @param ancestor
     * @param reader
     * @param handler
     * @author 夏集球
     * @time 2015年12月2日 下午5:25:52
     * @version 0.1
     * @since 0.1
     */
    public static final <T, K> void iterWithDeep(T ancestor, NodeReader<T, K> reader, NodeHandler<T> handler) {
        iterWithDeep(ancestor, reader, null, handler);
    }

    /**
     * 广度遍历
     *
     * @param ancestor
     * @param reader
     * @param handler
     * @author 夏集球
     * @time 2015年12月2日 下午5:25:52
     * @version 0.1
     * @since 0.1
     */
    public static final <T, K> void iterWithDeep(T ancestor, NodeFilter<T> filter, NodeHandler<T> handler) {
        iterWithDeep(ancestor, null, filter, handler);
    }

    /**
     * 广度遍历
     *
     * @param ancestor
     * @param reader
     * @param handler
     * @author 夏集球
     * @time 2015年12月2日 下午5:25:52
     * @version 0.1
     * @since 0.1
     */
    public static final <T, K> void iterWithDeep(T ancestor, NodeReader<T, K> reader, NodeFilter<T> filter, NodeHandler<T> handler) {
        iterWithDeep(ancestor, reader, filter, handler, 1);
    }

    /**
     * 深度遍历
     *
     * @param ancestor
     * @param reader
     * @param filter
     * @param handler
     * @param iterLevel
     * @author 夏集球
     * @time 2015年12月2日 下午6:25:19
     * @version 0.1
     * @since 0.1
     */
    private static final <T, K> void iterWithDeep(T ancestor, NodeReader<T, K> reader, NodeFilter<T> filter, NodeHandler<T> handler, int iterLevel) {
        if (null != ancestor && !isFilter(ancestor, filter)) {
            handleElement(ancestor, handler, iterLevel); // 处理节点
            // 深度遍历孩子
            List<T> children = reader.getChildren(ancestor);
            if (isNotEmpty(children)) { // 遍历孩子
                for (T child : children) {
                    iterWithDeep(child, reader, filter, handler, ++iterLevel);
                }
            }
        }
    }

    /**
     * 搜索结果
     *
     * @param ancestor
     * @param accepter
     * @return
     * @author 夏集球
     * @time 2015年12月2日 下午8:41:50
     * @version 0.1
     * @since 0.1
     */
    public static final <T, K> List<T> findNodes(final T ancestor, final NodeAccepter<T> accepter) {
        return findNodes(ancestor, null, null, accepter);
    }

    /**
     * 搜索结果
     *
     * @param ancestor
     * @param reader
     * @param accepter
     * @return
     * @author 夏集球
     * @time 2015年12月2日 下午8:40:25
     * @version 0.1
     * @since 0.1
     */
    public static final <T, K> List<T> findNodes(final T ancestor, final NodeFilter<T> filter, final NodeAccepter<T> accepter) {
        return findNodes(ancestor, null, filter, accepter);
    }

    /**
     * 搜索结果
     *
     * @param ancestor
     * @param reader
     * @param accepter
     * @return
     * @author 夏集球
     * @time 2015年12月2日 下午8:40:25
     * @version 0.1
     * @since 0.1
     */
    public static final <T, K> List<T> findNodes(final T ancestor, final NodeReader<T, K> reader, final NodeAccepter<T> accepter) {
        return findNodes(ancestor, reader, null, accepter);
    }

    /**
     * 搜索符合条件的节点
     *
     * @param ancestor
     * @param reader
     * @param accepter
     * @return
     * @author 夏集球
     * @time 2015年12月2日 下午8:36:31
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
     * 是否过滤
     *
     * @param node
     * @param filter
     * @return
     * @author 夏集球
     * @time 2015年12月2日 下午5:40:34
     * @version 0.1
     * @since 0.1
     */
    private static final <T> boolean isAccept(T node, NodeAccepter<T> accepter) {
        return null == accepter || accepter.accept(node);
    }

    /**
     * 转换成List
     *
     * @param ancestor
     * @return
     * @author 夏集球
     * @time 2015年12月2日 下午8:43:32
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
