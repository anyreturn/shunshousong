package com.shunshousong.exception;

/**
 * 订单未找到异常
 * 
 * <p>当查询的订单不存在时抛出此异常</p>
 * 
 * @author shunshousong team
 * @since 1.0.0
 */
public class OrderNotFoundException extends BusinessException {
    
    /**
     * 创建订单未找到异常
     * 
     * @param orderId 订单 ID
     */
    public OrderNotFoundException(Long orderId) {
        super("订单 " + orderId + " 不存在", "ORDER_NOT_FOUND");
    }
}
