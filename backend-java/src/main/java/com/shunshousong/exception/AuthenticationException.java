package com.shunshousong.exception;

/**
 * 认证失败异常
 * 
 * <p>当用户认证（登录）失败时抛出此异常</p>
 * 
 * @author shunshousong team
 * @since 1.0.0
 */
public class AuthenticationException extends BusinessException {
    
    /**
     * 创建认证失败异常
     * 
     * @param message 异常消息
     */
    public AuthenticationException(String message) {
        super(message, "AUTHENTICATION_FAILED");
    }
    
    /**
     * 创建密码错误异常
     */
    public AuthenticationException() {
        super("密码错误", "WRONG_PASSWORD");
    }
}
