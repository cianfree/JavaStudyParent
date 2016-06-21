package com.bxtpw.common.tree;

import java.io.Serializable;

/**
 * 节点过滤接口定义
 *
 * @param <T>
 * @author 夏集球
 * @version 0.1
 * @time 2015年12月2日 下午3:48:21
 * @since 0.1
 */
public interface NodeFilter<T> extends Serializable {

    /**
     * <pre>
     * 节点遍历接口定义：
     * 返回true表示过滤该节点，false表示接受该节点
     * </pre>
     *
     * @param node
     * @return
     * @author 夏集球
     * @time 2015年12月2日 下午3:48:40
     * @version 0.1
     * @since 0.1
     */
    boolean filter(T node);
}
