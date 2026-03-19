package com.shunshousong.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunshousong.entity.Payment;
import com.shunshousong.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
@DisplayName("PaymentController 集成测试")
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
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
    @DisplayName("GET /api/payments/{userId} - 获取用户支付记录")
    void findByUser() throws Exception {
        List<Payment> payments = Arrays.asList(testPayment, new Payment());
        when(paymentService.findByUser(50L)).thenReturn(payments);

        mockMvc.perform(get("/api/payments/50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

        verify(paymentService, times(1)).findByUser(50L);
    }

    @Test
    @DisplayName("GET /api/payments/{userId} - 用户没有支付记录")
    void findByUser_noPayments() throws Exception {
        when(paymentService.findByUser(999L)).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/payments/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(paymentService, times(1)).findByUser(999L);
    }

    @Test
    @DisplayName("POST /api/payments - 创建支付记录")
    void create() throws Exception {
        Payment newPayment = new Payment();
        newPayment.setOrderId(100L);
        newPayment.setUserId(50L);
        newPayment.setType("pay");
        newPayment.setAmount(50.0);

        when(paymentService.create(any(Payment.class))).thenAnswer(invocation -> {
            Payment saved = invocation.getArgument(0);
            saved.setId(1L);
            saved.setStatus("pending");
            return saved;
        });

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPayment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.orderId").value(100))
                .andExpect(jsonPath("$.userId").value(50))
                .andExpect(jsonPath("$.type").value("pay"))
                .andExpect(jsonPath("$.amount").value(50.0))
                .andExpect(jsonPath("$.status").value("pending"));

        verify(paymentService, times(1)).create(any(Payment.class));
    }

    @Test
    @DisplayName("POST /api/payments - 创建退款记录")
    void create_refund() throws Exception {
        Payment refundPayment = new Payment();
        refundPayment.setOrderId(100L);
        refundPayment.setUserId(50L);
        refundPayment.setType("refund");
        refundPayment.setAmount(50.0);

        when(paymentService.create(any(Payment.class))).thenReturn(refundPayment);

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refundPayment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("refund"))
                .andExpect(jsonPath("$.amount").value(50.0));

        verify(paymentService, times(1)).create(any(Payment.class));
    }

    @Test
    @DisplayName("POST /api/payments - 创建奖励支付记录")
    void create_reward() throws Exception {
        Payment rewardPayment = new Payment();
        rewardPayment.setOrderId(100L);
        rewardPayment.setUserId(50L);
        rewardPayment.setType("reward");
        rewardPayment.setAmount(100.0);

        when(paymentService.create(any(Payment.class))).thenReturn(rewardPayment);

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rewardPayment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("reward"))
                .andExpect(jsonPath("$.amount").value(100.0));

        verify(paymentService, times(1)).create(any(Payment.class));
    }

    @Test
    @DisplayName("POST /api/payments - 创建提现记录")
    void create_withdraw() throws Exception {
        Payment withdrawPayment = new Payment();
        withdrawPayment.setOrderId(100L);
        withdrawPayment.setUserId(50L);
        withdrawPayment.setType("withdraw");
        withdrawPayment.setAmount(200.0);

        when(paymentService.create(any(Payment.class))).thenReturn(withdrawPayment);

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawPayment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("withdraw"))
                .andExpect(jsonPath("$.amount").value(200.0));

        verify(paymentService, times(1)).create(any(Payment.class));
    }

    @Test
    @DisplayName("POST /api/payments - 创建成功状态的支付记录")
    void create_successStatus() throws Exception {
        Payment successPayment = new Payment();
        successPayment.setOrderId(100L);
        successPayment.setUserId(50L);
        successPayment.setType("pay");
        successPayment.setAmount(50.0);
        successPayment.setStatus("success");
        successPayment.setTransactionId("txn_123456");

        when(paymentService.create(any(Payment.class))).thenReturn(successPayment);

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(successPayment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.transactionId").value("txn_123456"));

        verify(paymentService, times(1)).create(any(Payment.class));
    }
}
