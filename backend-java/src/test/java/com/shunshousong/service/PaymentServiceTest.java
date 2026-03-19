package com.shunshousong.service;

import com.shunshousong.entity.Payment;
import com.shunshousong.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PaymentService 单元测试")
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Payment testPayment;

    @BeforeEach
    void setUp() {
        testPayment = new Payment();
        testPayment.setId(1L);
        testPayment.setOrderId(100L);
        testPayment.setUserId(50L);
        testPayment.setType("pay");
        testPayment.setAmount(50.0);
        testPayment.setStatus("pending");
        testPayment.setDescription("测试支付");
    }

    @Test
    @DisplayName("findByUser - 返回用户的支付记录")
    void findByUser() {
        List<Payment> payments = Arrays.asList(testPayment, new Payment());
        when(paymentRepository.findTop50ByUserIdOrderByCreatedAtDesc(50L)).thenReturn(payments);

        List<Payment> result = paymentService.findByUser(50L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(paymentRepository, times(1)).findTop50ByUserIdOrderByCreatedAtDesc(50L);
    }

    @Test
    @DisplayName("findByUser - 用户没有支付记录返回空列表")
    void findByUser_noPayments() {
        when(paymentRepository.findTop50ByUserIdOrderByCreatedAtDesc(999L)).thenReturn(Arrays.asList());

        List<Payment> result = paymentService.findByUser(999L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(paymentRepository, times(1)).findTop50ByUserIdOrderByCreatedAtDesc(999L);
    }

    @Test
    @DisplayName("create - 成功创建支付记录")
    void create() {
        Payment newPayment = new Payment();
        newPayment.setOrderId(100L);
        newPayment.setUserId(50L);
        newPayment.setType("pay");
        newPayment.setAmount(50.0);

        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment saved = invocation.getArgument(0);
            saved.setId(1L);
            saved.setStatus("pending");
            return saved;
        });

        Payment result = paymentService.create(newPayment);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("pending", result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    @DisplayName("create - 创建退款记录")
    void create_refund() {
        Payment refundPayment = new Payment();
        refundPayment.setOrderId(100L);
        refundPayment.setUserId(50L);
        refundPayment.setType("refund");
        refundPayment.setAmount(50.0);

        when(paymentRepository.save(any(Payment.class))).thenReturn(refundPayment);

        Payment result = paymentService.create(refundPayment);

        assertNotNull(result);
        assertEquals("refund", result.getType());
        assertEquals(50.0, result.getAmount());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    @DisplayName("create - 创建奖励支付记录")
    void create_reward() {
        Payment rewardPayment = new Payment();
        rewardPayment.setOrderId(100L);
        rewardPayment.setUserId(50L);
        rewardPayment.setType("reward");
        rewardPayment.setAmount(100.0);

        when(paymentRepository.save(any(Payment.class))).thenReturn(rewardPayment);

        Payment result = paymentService.create(rewardPayment);

        assertNotNull(result);
        assertEquals("reward", result.getType());
        assertEquals(100.0, result.getAmount());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    @DisplayName("create - 创建提现记录")
    void create_withdraw() {
        Payment withdrawPayment = new Payment();
        withdrawPayment.setOrderId(100L);
        withdrawPayment.setUserId(50L);
        withdrawPayment.setType("withdraw");
        withdrawPayment.setAmount(200.0);

        when(paymentRepository.save(any(Payment.class))).thenReturn(withdrawPayment);

        Payment result = paymentService.create(withdrawPayment);

        assertNotNull(result);
        assertEquals("withdraw", result.getType());
        assertEquals(200.0, result.getAmount());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }
}
