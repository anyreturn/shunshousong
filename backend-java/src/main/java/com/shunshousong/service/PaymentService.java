package com.shunshousong.service;

import com.shunshousong.entity.Payment;
import com.shunshousong.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 支付服务类
 * 
 * <p>处理支付相关的业务逻辑</p>
 * 
 * <p>职责：</p>
 * <ul>
 *     <li>支付记录查询</li>
 *     <li>支付记录创建</li>
 * </ul>
 * 
 * <p>依赖：</p>
 * <ul>
 *     <li>PaymentRepository: 数据持久化</li>
 * </ul>
 * 
 * @author shunshousong team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    /**
     * 查询用户的支付记录（前 50 条）
     * 
     * @param userId 用户 ID
     * @return 支付记录列表
     */
    public List<Payment> findByUser(Long userId) {
        return paymentRepository.findTop50ByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * 创建支付记录
     * 
     * @param paymentData 支付数据
     * @return 创建的支付记录
     */
    @Transactional
    public Payment create(Payment paymentData) {
        return paymentRepository.save(paymentData);
    }
}
