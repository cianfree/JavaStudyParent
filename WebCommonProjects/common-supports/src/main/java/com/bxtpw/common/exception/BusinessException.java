package com.bxtpw.common.exception;

/**
 * 业务异常
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/3/3 9:08
 * @since 0.1
 */
public class BusinessException extends RuntimeException implements JsonAndCodeExceptionInterface<BusinessException> {

    private static final long serialVersionUID = -7812870075815028889L;

    /**
     * 错误编码， 默认是500
     */
    protected String errorCode = ErrorCode.INTERNAL_500;

    /**
     * 是否是Json格式的消息， 默认不是
     */
    protected boolean isJson = false;

    @Override
    public String getErrorCode() {
        return this.errorCode;
    }

    @Override
    public BusinessException setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    @Override
    public BusinessException setJson(boolean isJson) {
        this.isJson = isJson;
        return this;
    }

    @Override
    public boolean isJson() {
        return this.isJson;
    }

    public BusinessException() {
    }

    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(String errorCode, boolean isJson, String message) {
        super(message);
        this.errorCode = errorCode;
        this.isJson = isJson;
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
