package com.shunshousong.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunshousong.entity.Order;
import com.shunshousong.exception.OrderNotFoundException;
import com.shunshousong.service.OrderService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * OrderController 集成测试
 * 
 * <p>使用 @WebMvcTest 进行 Controller 层集成测试</p>
 * 
 * @author shunshousong team
 * @since 1.0.0
 */
@WebMvcTest(OrderController.class)
@DisplayName("OrderController 集成测试")
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private Order testOrder;

    @BeforeEach
    void setUp() {
        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setOrderNo("SS1234567890000001");
        testOrder.setPublisherId(1L);
        testOrder.setStatus("pending");
        testOrder.setType("deliver");
        testOrder.setDescription("测试订单");
        testOrder.setReward(100.0);
    }

    @Test
    @DisplayName("GET /api/orders - 查询订单列表")
    void findAll() throws Exception {
        // Given
        List<Order> orders = Arrays.asList(testOrder, new Order());
        when(orderService.findAll(null, null, 20)).thenReturn(orders);

        // When & Then
        mockMvc.perform(get("/api/orders"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("GET /api/orders - 按状态筛选订单")
    void findAll_byStatus() throws Exception {
        // Given
        List<Order> orders = Arrays.asList(testOrder);
        when(orderService.findAll("pending", null, 20)).thenReturn(orders);

        // When & Then
        mockMvc.perform(get("/api/orders")
                .param("status", "pending"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("GET /api/orders/{id} - 成功查询订单")
    void findOne_success() throws Exception {
        // Given
        when(orderService.findOne(1L)).thenReturn(testOrder);

        // When & Then
        mockMvc.perform(get("/api/orders/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.orderNo").value("SS1234567890000001"));
    }

    @Test
    @DisplayName("GET /api/orders/{id} - 订单不存在返回 400")
    void findOne_notFound() throws Exception {
        // Given
        when(orderService.findOne(999L)).thenThrow(new OrderNotFoundException(999L));

        // When & Then
        mockMvc.perform(get("/api/orders/999"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorCode").value("ORDER_NOT_FOUND"));
    }

    @Test
    @DisplayName("POST /api/orders - 成功创建订单")
    void create_success() throws Exception {
        // Given
        Order newOrder = new Order();
        newOrder.setPublisherId(1L);
        newOrder.setDescription("新订单");
        newOrder.setReward(100.0);

        when(orderService.create(any(Order.class))).thenReturn(testOrder);

        // When & Then
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newOrder)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("POST /api/orders/{id}/accept - 成功接单")
    void accept_success() throws Exception {
        // Given
        testOrder.setStatus("accepted");
        testOrder.setAcceptorId(2L);

        when(orderService.accept(1L, 2L)).thenReturn(testOrder);

        OrderController.AcceptBody body = new OrderController.AcceptBody();
        body.setAcceptorId(2L);

        // When & Then
        mockMvc.perform(post("/api/orders/1/accept")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("accepted"))
            .andExpect(jsonPath("$.acceptorId").value(2));
    }

    @Test
    @DisplayName("POST /api/orders/{id}/pick - 成功取货")
    void pick_success() throws Exception {
        // Given
        testOrder.setStatus("picked");
        when(orderService.pick(1L)).thenReturn(testOrder);

        // When & Then
        mockMvc.perform(post("/api/orders/1/pick"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("picked"));
    }

    @Test
    @DisplayName("POST /api/orders/{id}/complete - 成功完成订单")
    void complete_success() throws Exception {
        // Given
        testOrder.setStatus("completed");
        testOrder.setPublisherRating(5);
        testOrder.setPublisherComment("很好");

        when(orderService.complete(eq(1L), anyInt(), anyString())).thenReturn(testOrder);

        OrderController.CompleteBody body = new OrderController.CompleteBody();
        body.setRating(5);
        body.setComment("很好");

        // When & Then
        mockMvc.perform(post("/api/orders/1/complete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("completed"))
            .andExpect(jsonPath("$.publisherRating").value(5));
    }

    @Test
    @DisplayName("POST /api/orders/{id}/cancel - 成功取消订单")
    void cancel_success() throws Exception {
        // Given
        testOrder.setStatus("cancelled");
        when(orderService.cancel(1L)).thenReturn(testOrder);

        // When & Then
        mockMvc.perform(post("/api/orders/1/cancel"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("cancelled"));
    }

    @Test
    @DisplayName("POST /api/orders/{id}/accept - 订单不存在返回 400")
    void accept_notFound() throws Exception {
        // Given
        OrderController.AcceptBody body = new OrderController.AcceptBody();
        body.setAcceptorId(2L);

        when(orderService.accept(999L, 2L)).thenThrow(new OrderNotFoundException(999L));

        // When & Then
        mockMvc.perform(post("/api/orders/999/accept")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorCode").value("ORDER_NOT_FOUND"));
    }
}
