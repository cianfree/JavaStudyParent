package com.bxtpw.common.tree;

/**
 * ���ж������Ԫ���쳣
 *
 * @author �ļ���
 * @version 0.1
 * @time 2015��12��2�� ����4:01:38
 * @since 0.1
 */
public class MultiParentException extends RuntimeException {

    private static final long serialVersionUID = 2011776053160277823L;

    public MultiParentException() {
        super("Multi parent exception!");
    }

    public MultiParentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MultiParentException(String message, Throwable cause) {
        super(message, cause);
    }

    public MultiParentException(String message) {
        super(message);
    }

    public MultiParentException(Throwable cause) {
        super(cause);
    }

}
