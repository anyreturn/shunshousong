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

/**
 * PaymentService 单元测试
 * 
 * <p>测试覆盖：</p>
 * <ul>
 *     <li>支付记录查询（findByUser）</li>
 *     <li>支付记录创建（create）</li>
 * </ul>
 * 
 * @author shunshousong team
 * @since 1.0.0
 */
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
        testPayment.setUserId(1L);
        testPayment.setOrderId(1L);
        testPayment.setAmount(100.0);
        testPayment.setStatus("success");
        testPayment.setType("pay");
    }

    @Test
    @DisplayName("findByUser - 查询用户的支付记录（前 50 条）")
    void findByUser() {
        // Given
        List<Payment> payments = Arrays.asList(testPayment, new Payment());
        when(paymentRepository.findTop50ByUserIdOrderByCreatedAtDesc(1L)).thenReturn(payments);

        // When
        List<Payment> result = paymentService.findByUser(1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(paymentRepository, times(1)).findTop50ByUserIdOrderByCreatedAtDesc(1L);
    }

    @Test
    @DisplayName("create - 成功创建支付记录")
    void create() {
        // Given
        Payment newPayment = new Payment();
        newPayment.setUserId(1L);
        newPayment.setAmount(100.0);
        when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);

        // When
        Payment result = paymentService.create(newPayment);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(100.0, result.getAmount());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }
}
