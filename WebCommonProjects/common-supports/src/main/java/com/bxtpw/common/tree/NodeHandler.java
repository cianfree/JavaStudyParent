package com.bxtpw.common.tree;

import java.io.Serializable;

/**
 * 节点处理接口
 *
 * @param <T>
 * @author 夏集球
 * @version 0.1
 * @time 2015年12月2日 下午3:47:15
 * @since 0.1
 */
public interface NodeHandler<T> extends Serializable {

    /**
     * 节点处理接口
     *
     * @param node
     * @param iterLevel 遍历的层次
     * @author 夏集球
     * @time 2015年12月2日 下午3:47:33
     * @version 0.1
     * @since 0.1
     */
    void handle(T node, int iterLevel);
}
