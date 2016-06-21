package com.bxtpw.common.tree;

import java.io.Serializable;

/**
 * �ڵ���˽ӿڶ���
 *
 * @param <T>
 * @author �ļ���
 * @version 0.1
 * @time 2015��12��2�� ����3:48:21
 * @since 0.1
 */
public interface NodeFilter<T> extends Serializable {

    /**
     * <pre>
     * �ڵ�����ӿڶ��壺
     * ����true��ʾ���˸ýڵ㣬false��ʾ���ܸýڵ�
     * </pre>
     *
     * @param node
     * @return
     * @author �ļ���
     * @time 2015��12��2�� ����3:48:40
     * @version 0.1
     * @since 0.1
     */
    boolean filter(T node);
}
