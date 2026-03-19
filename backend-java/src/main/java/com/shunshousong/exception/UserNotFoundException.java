package com.shunshousong.exception;

/**
 * 用户未找到异常
 * 
 * <p>当查询的用户不存在时抛出此异常</p>
 * 
 * @author shunshousong team
 * @since 1.0.0
 */
public class UserNotFoundException extends BusinessException {
    
    /**
     * 创建用户未找到异常
     * 
     * @param userId 用户 ID
     */
    public UserNotFoundException(Long userId) {
        super("用户 " + userId + " 不存在", "USER_NOT_FOUND");
    }
    
    /**
     * 创建用户未找到异常
     * 
     * @param phone 手机号
     */
    public UserNotFoundException(String phone) {
        super("用户 " + phone + " 不存在", "USER_NOT_FOUND");
    }
}
