package com.bxtpw.common.exception;

/**
 * 参数错误异常
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/3/3 9:08
 * @since 0.1
 */
public class InvalidParamException extends RuntimeException implements JsonAndCodeExceptionInterface<InvalidParamException> {

    private static final long serialVersionUID = -7812870075815028889L;

    /**
     * 错误编码， 默认是400
     */
    protected String errorCode = ErrorCode.PARAMS_VALIDATE_400;

    /**
     * 是否是Json格式的消息， 默认不是
     */
    protected boolean isJson = false;

    @Override
    public String getErrorCode() {
        return this.errorCode;
    }

    @Override
    public InvalidParamException setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    @Override
    public InvalidParamException setJson(boolean isJson) {
        this.isJson = isJson;
        return this;
    }

    @Override
    public boolean isJson() {
        return this.isJson;
    }

    public InvalidParamException() {
    }

    public InvalidParamException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public InvalidParamException(String errorCode, boolean isJson, String message) {
        super(message);
        this.errorCode = null == errorCode ? ErrorCode.PARAMS_VALIDATE_400 : errorCode;
        this.isJson = isJson;
    }

    public InvalidParamException(String message) {
        super(message);
    }

    public InvalidParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidParamException(Throwable cause) {
        super(cause);
    }

    public InvalidParamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
