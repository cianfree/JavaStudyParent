package com.bxtpw.common.tree;

import java.io.Serializable;
import java.util.List;

/**
 * �ڵ�Ԫ�ش洢��
 *
 * @author �ļ���
 * @version 0.1
 * @time 2015��12��2�� ����3:41:26
 * @since 0.1
 */
public interface NodeWriter<T> extends Serializable {

    /**
     * ���ø���Ԫ��
     *
     * @param obj
     * @param parent
     * @return
     * @author �ļ���
     * @time 2015��12��2�� ����3:42:07
     * @version 0.1
     * @since 0.1
     */
    void setParent(T obj, T parent);

    /**
     * �����Ӽ�Ԫ��
     *
     * @param obj
     * @param children
     * @return
     * @author �ļ���
     * @time 2015��12��2�� ����3:42:36
     * @version 0.1
     * @since 0.1
     */
    void setChildren(T obj, List<T> children);

}
