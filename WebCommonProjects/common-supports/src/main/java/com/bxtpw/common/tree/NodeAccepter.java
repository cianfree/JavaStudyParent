package com.bxtpw.common.tree;

import java.io.Serializable;

/**
 * �ڵ���ܾ�����
 *
 * @param <T>
 * @author �ļ���
 * @version 0.1
 * @time 2015��12��2�� ����3:48:21
 * @since 0.1
 */
public interface NodeAccepter<T> extends Serializable {

    /**
     * <pre>
     * ��ʾ�Ƿ����
     * </pre>
     *
     * @param node
     * @return
     * @author �ļ���
     * @time 2015��12��2�� ����3:48:40
     * @version 0.1
     * @since 0.1
     */
    boolean accept(T node);
}
