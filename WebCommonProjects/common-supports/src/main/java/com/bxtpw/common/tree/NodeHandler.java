package com.bxtpw.common.tree;

import java.io.Serializable;

/**
 * �ڵ㴦��ӿ�
 *
 * @param <T>
 * @author �ļ���
 * @version 0.1
 * @time 2015��12��2�� ����3:47:15
 * @since 0.1
 */
public interface NodeHandler<T> extends Serializable {

    /**
     * �ڵ㴦��ӿ�
     *
     * @param node
     * @param iterLevel �����Ĳ��
     * @author �ļ���
     * @time 2015��12��2�� ����3:47:33
     * @version 0.1
     * @since 0.1
     */
    void handle(T node, int iterLevel);
}
