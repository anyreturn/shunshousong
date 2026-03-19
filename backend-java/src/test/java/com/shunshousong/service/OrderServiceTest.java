package com.shunshousong.service;

import com.shunshousong.entity.Order;
import com.shunshousong.exception.OrderNotFoundException;
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

/**
 * OrderService 单元测试
 * 
 * <p>测试覆盖：</p>
 * <ul>
 *     <li>订单查询（findAll, findOne）</li>
 *     <li>订单创建（create）</li>
 *     <li>订单状态流转（accept, pick, complete, cancel）</li>
 *     <li>异常处理（OrderNotFoundException）</li>
 * </ul>
 * 
 * @author shunshousong team
 * @since 1.0.0
 */
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
        testOrder.setOrderNo("SS1234567890000001");
        testOrder.setPublisherId(1L);
        testOrder.setAcceptorId(null);
        testOrder.setStatus("pending");
        testOrder.setType("deliver");
        testOrder.setDescription("测试描述");
        testOrder.setReward(100.0);
    }

    // ==================== 查询订单 ====================

    @Test
    @DisplayName("findAll - 无条件查询返回前 20 个订单")
    void findAll_noConditions() {
        // Given
        List<Order> orders = Arrays.asList(testOrder, new Order());
        when(orderRepository.findTop20ByOrderByCreatedAtDesc()).thenReturn(orders);

        // When
        List<Order> result = orderService.findAll(null, null, 20);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findTop20ByOrderByCreatedAtDesc();
    }

    @Test
    @DisplayName("findAll - 按状态查询")
    void findAll_byStatus() {
        // Given
        List<Order> orders = Arrays.asList(testOrder);
        when(orderRepository.findByStatusOrderByCreatedAtDesc("pending")).thenReturn(orders);

        // When
        List<Order> result = orderService.findAll("pending", null, 20);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findByStatusOrderByCreatedAtDesc("pending");
    }

    @Test
    @DisplayName("findAll - 按类型查询")
    void findAll_byType() {
        // Given
        List<Order> orders = Arrays.asList(testOrder);
        when(orderRepository.findByTypeOrderByCreatedAtDesc("help")).thenReturn(orders);

        // When
        List<Order> result = orderService.findAll(null, "help", 20);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findByTypeOrderByCreatedAtDesc("help");
    }

    @Test
    @DisplayName("findAll - 按状态和类型查询")
    void findAll_byStatusAndType() {
        // Given
        List<Order> orders = Arrays.asList(testOrder);
        when(orderRepository.findByStatusAndTypeOrderByCreatedAtDesc("pending", "help")).thenReturn(orders);

        // When
        List<Order> result = orderService.findAll("pending", "help", 20);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findByStatusAndTypeOrderByCreatedAtDesc("pending", "help");
    }

    @Test
    @DisplayName("findOne - 找到存在的订单")
    void findOne_whenOrderExists() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        // When
        Order result = orderService.findOne(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("SS1234567890000001", result.getOrderNo());
    }

    @Test
    @DisplayName("findOne - 订单不存在时抛出 OrderNotFoundException")
    void findOne_whenOrderNotExists() {
        // Given
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        OrderNotFoundException exception = assertThrows(
            OrderNotFoundException.class, 
            () -> orderService.findOne(999L)
        );
        assertEquals("订单 999 不存在", exception.getMessage());
        assertEquals("ORDER_NOT_FOUND", exception.getErrorCode());
    }

    // ==================== 创建订单 ====================

    @Test
    @DisplayName("create - 成功创建订单（自动生成订单号）")
    void create() {
        // Given
        Order newOrder = new Order();
        newOrder.setPublisherId(1L);
        newOrder.setDescription("新订单");
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // When
        Order result = orderService.create(newOrder);

        // Then
        assertNotNull(result);
        assertNotNull(result.getOrderNo());
        assertTrue(result.getOrderNo().startsWith("SS"));
        assertEquals("pending", result.getStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    // ==================== 订单状态流转 ====================

    @Test
    @DisplayName("accept - 成功接单（状态：pending → accepted）")
    void accept() {
        // Given
        testOrder.setStatus("pending");
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // When
        Order result = orderService.accept(1L, 2L);

        // Then
        assertNotNull(result);
        assertEquals("accepted", result.getStatus());
        assertEquals(2L, result.getAcceptorId());
        assertNotNull(result.getAcceptedAt());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("accept - 订单不存在时抛出异常")
    void accept_whenOrderNotExists() {
        // Given
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        OrderNotFoundException exception = assertThrows(
            OrderNotFoundException.class, 
            () -> orderService.accept(999L, 2L)
        );
        assertEquals("订单 999 不存在", exception.getMessage());
    }

    @Test
    @DisplayName("pick - 成功取货（状态：accepted → picked）")
    void pick() {
        // Given
        testOrder.setStatus("accepted");
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // When
        Order result = orderService.pick(1L);

        // Then
        assertNotNull(result);
        assertEquals("picked", result.getStatus());
        assertNotNull(result.getPickedAt());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("complete - 成功完成订单（状态：picked → completed）")
    void complete() {
        // Given
        testOrder.setStatus("picked");
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // When
        Order result = orderService.complete(1L, 5, "很好");

        // Then
        assertNotNull(result);
        assertEquals("completed", result.getStatus());
        assertEquals(5, result.getPublisherRating());
        assertEquals("很好", result.getPublisherComment());
        assertNotNull(result.getCompletedAt());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("complete - 完成订单时评分和评论可选")
    void complete_withoutRatingAndComment() {
        // Given
        testOrder.setStatus("picked");
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // When
        Order result = orderService.complete(1L, null, null);

        // Then
        assertNotNull(result);
        assertEquals("completed", result.getStatus());
        assertNotNull(result.getCompletedAt());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("cancel - 成功取消订单（状态：任意 → cancelled）")
    void cancel() {
        // Given
        testOrder.setStatus("pending");
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // When
        Order result = orderService.cancel(1L);

        // Then
        assertNotNull(result);
        assertEquals("cancelled", result.getStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
    }
}
