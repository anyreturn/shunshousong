package com.shunshousong.constant;

/**
 * 系统常量
 * 
 * <p>定义系统中使用的常量值</p>
 * 
 * @author shunshousong team
 * @since 1.0.0
 */
public final class AppConstants {
    
    /**
     * Token 有效期（7 天）
     */
    public static final long TOKEN_EXPIRE_DAYS = 7L;
    
    /**
     * Token 有效期（毫秒）
     */
    public static final long TOKEN_EXPIRE_MS = TOKEN_EXPIRE_DAYS * 24 * 60 * 60 * 1000;
    
    /**
     * 默认信用分数
     */
    public static final int DEFAULT_CREDIT_SCORE = 100;
    
    /**
     * 最大查询记录数
     */
    public static final int MAX_QUERY_LIMIT = 50;
    
    /**
     * 默认押金金额
     */
    public static final double DEFAULT_DEPOSIT = 0.0;
    
    /**
     * 默认余额
     */
    public static final double DEFAULT_BALANCE = 0.0;
    
    /**
     * 错误消息
     */
    public static final String ERROR_USER_NOT_FOUND = "用户不存在";
    public static final String ERROR_USER_ALREADY_EXISTS = "该手机号已注册";
    public static final String ERROR_WRONG_PASSWORD = "密码错误";
    public static final String ERROR_AGREEMENT_REQUIRED = "请同意用户协议";
    
    /**
     * 成功消息
     */
    public static final String SUCCESS_LOGIN = "登录成功";
    public static final String SUCCESS_REGISTER = "注册成功";
    
    // 私有构造函数，防止实例化
    private AppConstants() {
        throw new UnsupportedOperationException("这是常量类，不能实例化");
    }
}
