package com.shunshousong.exception;

/**
 * 用户已存在异常
 * 
 * <p>当尝试创建已存在的用户时抛出此异常</p>
 * 
 * @author shunshousong team
 * @since 1.0.0
 */
public class UserAlreadyExistsException extends BusinessException {
    
    /**
     * 创建用户已存在异常
     * 
     * @param phone 手机号
     */
    public UserAlreadyExistsException(String phone) {
        super("该手机号已注册：" + phone, "USER_ALREADY_EXISTS");
    }
    
    /**
     * 创建用户已存在异常（OpenID）
     * 
     * @param openid 微信 OpenID
     */
    public UserAlreadyExistsException(String openid, boolean isOpenid) {
        super("该 OpenID 已注册：" + openid, "USER_ALREADY_EXISTS");
    }
}
