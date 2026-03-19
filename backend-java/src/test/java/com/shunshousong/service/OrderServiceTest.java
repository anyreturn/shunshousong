package com.shunshousong.service;

import com.shunshousong.entity.Order;
import com.shunshousong.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService 单元测试")
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
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
    @DisplayName("findAll - 无条件查询返回前 20 个订单")
    void findAll_noFilters() {
        List<Order> orders = Arrays.asList(testOrder, new Order());
        when(orderRepository.findTop20ByOrderByCreatedAtDesc()).thenReturn(orders);

        List<Order> result = orderService.findAll(null, null, 20);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findTop20ByOrderByCreatedAtDesc();
    }

    @Test
    @DisplayName("findAll - 按状态查询")
    void findAll_byStatus() {
        List<Order> orders = Arrays.asList(testOrder);
        when(orderRepository.findByStatusOrderByCreatedAtDesc("pending")).thenReturn(orders);

        List<Order> result = orderService.findAll("pending", null, 20);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findByStatusOrderByCreatedAtDesc("pending");
    }

    @Test
    @DisplayName("findAll - 按类型查询")
    void findAll_byType() {
        List<Order> orders = Arrays.asList(testOrder);
        when(orderRepository.findByTypeOrderByCreatedAtDesc("deliver")).thenReturn(orders);

        List<Order> result = orderService.findAll(null, "deliver", 20);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findByTypeOrderByCreatedAtDesc("deliver");
    }

    @Test
    @DisplayName("findAll - 按状态和类型组合查询")
    void findAll_byStatusAndType() {
        List<Order> orders = Arrays.asList(testOrder);
        when(orderRepository.findByStatusAndTypeOrderByCreatedAtDesc("pending", "deliver"))
                .thenReturn(orders);

        List<Order> result = orderService.findAll("pending", "deliver", 20);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository, times(1))
                .findByStatusAndTypeOrderByCreatedAtDesc("pending", "deliver");
    }

    @Test
    @DisplayName("findOne - 找到存在的订单")
    void findOne_whenOrderExists() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        Order result = orderService.findOne(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("SS1234567890", result.getOrderNo());
    }

    @Test
    @DisplayName("findOne - 订单不存在时抛出异常")
    void findOne_whenOrderNotExists() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.findOne(999L);
        });

        assertEquals("订单 999 不存在", exception.getMessage());
    }

    @Test
    @DisplayName("create - 成功创建订单")
    void create() {
        Order newOrder = new Order();
        newOrder.setPublisherId(100L);
        newOrder.setType("deliver");
        newOrder.setPickupAddress("pickup");
        newOrder.setDeliveryAddress("delivery");
        newOrder.setReward(50.0);

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        Order result = orderService.create(newOrder);

        assertNotNull(result);
        assertNotNull(result.getOrderNo());
        assertTrue(result.getOrderNo().startsWith("SS"));
        assertEquals("pending", result.getStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("accept - 成功接单")
    void accept() {
        testOrder.setStatus("pending");
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        Order result = orderService.accept(1L, 200L);

        assertNotNull(result);
        assertEquals(200L, result.getAcceptorId());
        assertEquals("accepted", result.getStatus());
        assertNotNull(result.getAcceptedAt());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("accept - 订单不存在时抛出异常")
    void accept_whenOrderNotExists() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.accept(999L, 200L);
        });

        assertEquals("订单 999 不存在", exception.getMessage());
    }

    @Test
    @DisplayName("pick - 成功取件")
    void pick() {
        testOrder.setStatus("accepted");
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        Order result = orderService.pick(1L);

        assertNotNull(result);
        assertEquals("picked", result.getStatus());
        assertNotNull(result.getPickedAt());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("complete - 成功完成订单（带评分和评论）")
    void complete_withRatingAndComment() {
        testOrder.setStatus("picked");
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        Order result = orderService.complete(1L, 5, "很好的服务");

        assertNotNull(result);
        assertEquals("completed", result.getStatus());
        assertEquals(5, result.getPublisherRating());
        assertEquals("很好的服务", result.getPublisherComment());
        assertNotNull(result.getCompletedAt());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("complete - 完成订单（不带评分和评论）")
    void complete_withoutRatingAndComment() {
        testOrder.setStatus("picked");
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        Order result = orderService.complete(1L, null, null);

        assertNotNull(result);
        assertEquals("completed", result.getStatus());
        assertNull(result.getPublisherRating());
        assertNull(result.getPublisherComment());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("cancel - 成功取消订单")
    void cancel() {
        testOrder.setStatus("pending");
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        Order result = orderService.cancel(1L);

        assertNotNull(result);
        assertEquals("cancelled", result.getStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("cancel - 订单不存在时抛出异常")
    void cancel_whenOrderNotExists() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.cancel(999L);
        });

        assertEquals("订单 999 不存在", exception.getMessage());
    }
}
