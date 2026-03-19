package com.shunshousong.controller;

import com.shunshousong.entity.Payment;
import com.shunshousong.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 支付控制器
 * 
 * <p>处理支付相关的 HTTP 请求</p>
 * 
 * <p>接口列表：</p>
 * <ul>
 *     <li>GET /api/payments/user/{userId} - 查询用户支付记录</li>
 *     <li>POST /api/payments - 创建支付记录</li>
 * </ul>
 * 
 * <p>依赖：</p>
 * <ul>
 *     <li>PaymentService: 支付业务逻辑</li>
 * </ul>
 * 
 * @author shunshousong team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * 查询用户的支付记录（前 50 条）
     * 
     * GET /api/payments/user/{userId}
     * 
     * @param userId 用户 ID
     * @return 支付记录列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Payment>> findByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(paymentService.findByUser(userId));
    }

    /**
     * 创建支付记录
     * 
     * POST /api/payments
     * 
     * @param paymentData 支付数据
     * @return 创建的支付记录
     */
    @PostMapping
    public ResponseEntity<Payment> create(@RequestBody Payment paymentData) {
        return ResponseEntity.ok(paymentService.create(paymentData));
    }
}
