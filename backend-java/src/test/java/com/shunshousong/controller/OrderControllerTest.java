package com.shunshousong.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunshousong.entity.Order;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@DisplayName("OrderController 集成测试")
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    private Order testOrder;

    @BeforeEach
    void setUp() {
        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setOrderNo("SS1234567890");
        testOrder.setPublisherId(100L);
        testOrder.setAcceptorId(null);
        testOrder.setType("deliver");
        testOrder.setStatus("pending");
        testOrder.setPickupAddress("北京市朝阳区 pickup 地址");
        testOrder.setDeliveryAddress("北京市海淀区 delivery 地址");
        testOrder.setReward(50.0);
        testOrder.setDescription("测试订单");
    }

    @Test
    @DisplayName("GET /api/orders - 获取订单列表（无条件）")
    void findAll_noFilters() throws Exception {
        List<Order> orders = Arrays.asList(testOrder, new Order());
        when(orderService.findAll(null, null, 20)).thenReturn(orders);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

        verify(orderService, times(1)).findAll(null, null, 20);
    }

    @Test
    @DisplayName("GET /api/orders - 按状态筛选订单")
    void findAll_byStatus() throws Exception {
        List<Order> orders = Arrays.asList(testOrder);
        when(orderService.findAll(eq("pending"), isNull(), eq(20))).thenReturn(orders);

        mockMvc.perform(get("/api/orders")
                        .param("status", "pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(orderService, times(1)).findAll(eq("pending"), isNull(), eq(20));
    }

    @Test
    @DisplayName("GET /api/orders - 按类型筛选订单")
    void findAll_byType() throws Exception {
        List<Order> orders = Arrays.asList(testOrder);
        when(orderService.findAll(isNull(), eq("deliver"), eq(20))).thenReturn(orders);

        mockMvc.perform(get("/api/orders")
                        .param("type", "deliver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(orderService, times(1)).findAll(isNull(), eq("deliver"), eq(20));
    }

    @Test
    @DisplayName("GET /api/orders - 自定义 limit 参数")
    void findAll_withLimit() throws Exception {
        List<Order> orders = Arrays.asList(testOrder);
        when(orderService.findAll(null, null, 50)).thenReturn(orders);

        mockMvc.perform(get("/api/orders")
                        .param("limit", "50"))
                .andExpect(status().isOk());

        verify(orderService, times(1)).findAll(null, null, 50);
    }

    @Test
    @DisplayName("GET /api/orders/{id} - 获取单个订单")
    void findOne() throws Exception {
        when(orderService.findOne(1L)).thenReturn(testOrder);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.orderNo").value("SS1234567890"))
                .andExpect(jsonPath("$.status").value("pending"));

        verify(orderService, times(1)).findOne(1L);
    }

    @Test
    @DisplayName("GET /api/orders/{id} - 订单不存在返回错误")
    void findOne_notFound() throws Exception {
        when(orderService.findOne(999L)).thenThrow(new RuntimeException("订单 999 不存在"));

        mockMvc.perform(get("/api/orders/999"))
                .andExpect(status().is4xxClientError());

        verify(orderService, times(1)).findOne(999L);
    }

    @Test
    @DisplayName("POST /api/orders - 创建订单")
    void create() throws Exception {
        Order newOrder = new Order();
        newOrder.setPublisherId(100L);
        newOrder.setType("deliver");
        newOrder.setPickupAddress("pickup");
        newOrder.setDeliveryAddress("delivery");
        newOrder.setReward(50.0);

        when(orderService.create(any(Order.class))).thenAnswer(invocation -> {
            Order saved = invocation.getArgument(0);
            saved.setId(1L);
            saved.setOrderNo("SS1234567890");
            saved.setStatus("pending");
            return saved;
        });

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newOrder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.orderNo").value("SS1234567890"))
                .andExpect(jsonPath("$.status").value("pending"));

        verify(orderService, times(1)).create(any(Order.class));
    }

    @Test
    @DisplayName("POST /api/orders/{id}/accept - 接单")
    void accept() throws Exception {
        testOrder.setStatus("accepted");
        testOrder.setAcceptorId(200L);
        when(orderService.accept(eq(1L), eq(200L))).thenReturn(testOrder);

        Map<String, Long> body = new HashMap<>();
        body.put("acceptorId", 200L);

        mockMvc.perform(post("/api/orders/1/accept")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("accepted"))
                .andExpect(jsonPath("$.acceptorId").value(200));

        verify(orderService, times(1)).accept(eq(1L), eq(200L));
    }

    @Test
    @DisplayName("POST /api/orders/{id}/pick - 取件")
    void pick() throws Exception {
        testOrder.setStatus("picked");
        when(orderService.pick(1L)).thenReturn(testOrder);

        mockMvc.perform(post("/api/orders/1/pick"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("picked"));

        verify(orderService, times(1)).pick(1L);
    }

    @Test
    @DisplayName("POST /api/orders/{id}/complete - 完成订单（带评分和评论）")
    void complete_withRatingAndComment() throws Exception {
        testOrder.setStatus("completed");
        testOrder.setPublisherRating(5);
        testOrder.setPublisherComment("很好的服务");
        when(orderService.complete(eq(1L), eq(5), eq("很好的服务"))).thenReturn(testOrder);

        Map<String, Object> body = new HashMap<>();
        body.put("rating", 5);
        body.put("comment", "很好的服务");

        mockMvc.perform(post("/api/orders/1/complete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("completed"))
                .andExpect(jsonPath("$.publisherRating").value(5))
                .andExpect(jsonPath("$.publisherComment").value("很好的服务"));

        verify(orderService, times(1)).complete(eq(1L), eq(5), eq("很好的服务"));
    }

    @Test
    @DisplayName("POST /api/orders/{id}/complete - 完成订单（不带评分和评论）")
    void complete_withoutRatingAndComment() throws Exception {
        testOrder.setStatus("completed");
        when(orderService.complete(eq(1L), isNull(), isNull())).thenReturn(testOrder);

        Map<String, Object> body = new HashMap<>();

        mockMvc.perform(post("/api/orders/1/complete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("completed"));

        verify(orderService, times(1)).complete(eq(1L), isNull(), isNull());
    }

    @Test
    @DisplayName("POST /api/orders/{id}/cancel - 取消订单")
    void cancel() throws Exception {
        testOrder.setStatus("cancelled");
        when(orderService.cancel(1L)).thenReturn(testOrder);

        mockMvc.perform(post("/api/orders/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("cancelled"));

        verify(orderService, times(1)).cancel(1L);
    }
}
