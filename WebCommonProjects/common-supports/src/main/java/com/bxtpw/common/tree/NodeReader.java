package com.bxtpw.common.tree;

import java.io.Serializable;
import java.util.List;

/**
 * 节点元素读取器
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015年12月2日 下午3:41:26
 * @since 0.1
 */
public interface NodeReader<T, K> extends Serializable {

    /**
     * 读取主键
     *
     * @param obj
     * @return
     * @author 夏集球
     * @time 2015年12月2日 下午3:56:30
     * @version 0.1
     * @since 0.1
     */
    K getPrimarkKey(T obj);

    /**
     * 获取父级元素
     *
     * @param obj
     * @return
     * @author 夏集球
     * @time 2015年12月2日 下午3:42:07
     * @version 0.1
     * @since 0.1
     */
    T getParent(T obj);

    /**
     * 获取子级元素
     *
     * @param obj
     * @return
     * @author 夏集球
     * @time 2015年12月2日 下午3:42:36
     * @version 0.1
     * @since 0.1
     */
    List<T> getChildren(T obj);

}
