package com.bxtpw.common.tree;

/**
 * û�и���Ԫ���쳣
 *
 * @author �ļ���
 * @version 0.1
 * @time 2015��12��2�� ����4:01:38
 * @since 0.1
 */
public class NoneParentException extends RuntimeException {

    private static final long serialVersionUID = 2011776053160277823L;

    public NoneParentException() {
        super("None parent exception!");
    }

    public NoneParentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public NoneParentException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoneParentException(String message) {
        super(message);
    }

    public NoneParentException(Throwable cause) {
        super(cause);
    }

}
