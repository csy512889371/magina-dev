package com.rjsoft.magina.spi.common.exception;


/**
 * 基础异常
 */
public class BusinessException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    //所属模块
    private String module;

    /**
     * 错误码
     */
    private String code;

    /**
     * Ba
     * 错误码对应的参数
     */
    private Object[] args;

    /**
     * 错误消息
     */
    private String defaultMessage;


    public BusinessException(String module, String code, Object[] args, String defaultMessage) {
        this.module = module;
        this.code = code;
        this.args = args;
        this.defaultMessage = defaultMessage;
    }

    public BusinessException(String module, String code, Object[] args) {
        this(module, code, args, null);
    }

    public BusinessException(String module, String defaultMessage) {
        this(module, null, null, defaultMessage);
    }

    public BusinessException(String code, Object[] args) {
        this(null, code, args, null);
    }

    public BusinessException(String defaultMessage) {
        this(null, null, null, defaultMessage);
    }


    public String getModule() {
        return module;
    }

    public String getCode() {
        return code;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    @Override
    public String toString() {
        return this.getClass() + "{" +
                "module='" + module + '\'' +
                ", message='" + getMessage() + '\'' +
                '}';
    }
}
