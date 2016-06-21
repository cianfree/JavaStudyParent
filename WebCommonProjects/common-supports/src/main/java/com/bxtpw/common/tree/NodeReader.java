package com.bxtpw.common.tree;

import java.io.Serializable;
import java.util.List;

/**
 * �ڵ�Ԫ�ض�ȡ��
 *
 * @author �ļ���
 * @version 0.1
 * @time 2015��12��2�� ����3:41:26
 * @since 0.1
 */
public interface NodeReader<T, K> extends Serializable {

    /**
     * ��ȡ����
     *
     * @param obj
     * @return
     * @author �ļ���
     * @time 2015��12��2�� ����3:56:30
     * @version 0.1
     * @since 0.1
     */
    K getPrimarkKey(T obj);

    /**
     * ��ȡ����Ԫ��
     *
     * @param obj
     * @return
     * @author �ļ���
     * @time 2015��12��2�� ����3:42:07
     * @version 0.1
     * @since 0.1
     */
    T getParent(T obj);

    /**
     * ��ȡ�Ӽ�Ԫ��
     *
     * @param obj
     * @return
     * @author �ļ���
     * @time 2015��12��2�� ����3:42:36
     * @version 0.1
     * @since 0.1
     */
    List<T> getChildren(T obj);

}
