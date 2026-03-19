package com.shunshousong.exception;

/**
 * 业务异常基类
 * 
 * <p>所有业务相关的异常都应该继承此类</p>
 * 
 * @author shunshousong team
 * @since 1.0.0
 */
public class BusinessException extends RuntimeException {
    
    private final String errorCode;
    
    /**
     * 创建业务异常
     * 
     * @param message 异常消息
     * @param errorCode 错误码
     */
    public BusinessException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    /**
     * 创建业务异常
     * 
     * @param message 异常消息
     * @param errorCode 错误码
     * @param cause 原因异常
     */
    public BusinessException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    /**
     * 获取错误码
     * 
     * @return 错误码
     */
    public String getErrorCode() {
        return errorCode;
    }
}
