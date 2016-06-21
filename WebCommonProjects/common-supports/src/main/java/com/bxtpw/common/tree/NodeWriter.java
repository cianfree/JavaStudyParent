package com.bxtpw.common.tree;

import java.io.Serializable;
import java.util.List;

/**
 * 节点元素存储器
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015年12月2日 下午3:41:26
 * @since 0.1
 */
public interface NodeWriter<T> extends Serializable {

    /**
     * 设置父级元素
     *
     * @param obj
     * @param parent
     * @return
     * @author 夏集球
     * @time 2015年12月2日 下午3:42:07
     * @version 0.1
     * @since 0.1
     */
    void setParent(T obj, T parent);

    /**
     * 设置子级元素
     *
     * @param obj
     * @param children
     * @return
     * @author 夏集球
     * @time 2015年12月2日 下午3:42:36
     * @version 0.1
     * @since 0.1
     */
    void setChildren(T obj, List<T> children);

}
