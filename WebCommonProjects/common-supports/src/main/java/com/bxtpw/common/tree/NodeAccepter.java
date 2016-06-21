package com.bxtpw.common.tree;

import java.io.Serializable;

/**
 * 节点接受决策器
 *
 * @param <T>
 * @author 夏集球
 * @version 0.1
 * @time 2015年12月2日 下午3:48:21
 * @since 0.1
 */
public interface NodeAccepter<T> extends Serializable {

    /**
     * <pre>
     * 表示是否接受
     * </pre>
     *
     * @param node
     * @return
     * @author 夏集球
     * @time 2015年12月2日 下午3:48:40
     * @version 0.1
     * @since 0.1
     */
    boolean accept(T node);
}
