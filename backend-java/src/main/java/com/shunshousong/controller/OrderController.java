package com.shunshousong.controller;

import com.shunshousong.entity.Order;
import com.shunshousong.service.OrderService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 订单控制器
 * 
 * <p>处理订单相关的 HTTP 请求</p>
 * 
 * <p>接口列表：</p>
 * <ul>
 *     <li>GET /api/orders - 查询订单列表</li>
 *     <li>GET /api/orders/{id} - 查询单个订单</li>
 *     <li>POST /api/orders - 创建订单</li>
 *     <li>POST /api/orders/{id}/accept - 接单</li>
 *     <li>POST /api/orders/{id}/pick - 取货</li>
 *     <li>POST /api/orders/{id}/complete - 完成订单</li>
 *     <li>POST /api/orders/{id}/cancel - 取消订单</li>
 * </ul>
 * 
 * <p>依赖：</p>
 * <ul>
 *     <li>OrderService: 订单业务逻辑</li>
 * </ul>
 * 
 * @author shunshousong team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 查询订单列表（支持筛选）
     * 
     * GET /api/orders?status=pending&type=help&limit=20
     * 
     * @param status 订单状态（可选）
     * @param type 订单类型（可选）
     * @param limit 返回数量限制（默认 20）
     * @return 订单列表
     */
    @GetMapping
    public ResponseEntity<List<Order>> findAll(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false, defaultValue = "20") Integer limit) {
        return ResponseEntity.ok(orderService.findAll(status, type, limit));
    }

    /**
     * 根据 ID 查询订单
     * 
     * GET /api/orders/{id}
     * 
     * @param id 订单 ID
     * @return 订单实体
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findOne(id));
    }

    /**
     * 创建订单
     * 
     * POST /api/orders
     * 
     * @param orderData 订单数据
     * @return 创建的订单实体
     */
    @PostMapping
    public ResponseEntity<Order> create(@RequestBody Order orderData) {
        return ResponseEntity.ok(orderService.create(orderData));
    }

    /**
     * 接单
     * 
     * POST /api/orders/{id}/accept
     * 
     * @param id 订单 ID
     * @param body 请求体 { acceptorId: 123 }
     * @return 更新后的订单实体
     */
    @PostMapping("/{id}/accept")
    public ResponseEntity<Order> accept(
            @PathVariable Long id,
            @Valid @RequestBody AcceptBody body) {
        return ResponseEntity.ok(orderService.accept(id, body.getAcceptorId()));
    }

    /**
     * 取货
     * 
     * POST /api/orders/{id}/pick
     * 
     * @param id 订单 ID
     * @return 更新后的订单实体
     */
    @PostMapping("/{id}/pick")
    public ResponseEntity<Order> pick(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.pick(id));
    }

    /**
     * 完成订单
     * 
     * POST /api/orders/{id}/complete
     * 
     * @param id 订单 ID
     * @param body 请求体 { rating: 5, comment: "很好" }
     * @return 更新后的订单实体
     */
    @PostMapping("/{id}/complete")
    public ResponseEntity<Order> complete(
            @PathVariable Long id,
            @Valid @RequestBody CompleteBody body) {
        return ResponseEntity.ok(orderService.complete(id, body.getRating(), body.getComment()));
    }

    /**
     * 取消订单
     * 
     * POST /api/orders/{id}/cancel
     * 
     * @param id 订单 ID
     * @return 更新后的订单实体
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Order> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.cancel(id));
    }

    /**
     * 接单请求体
     */
    @Data
    public static class AcceptBody {
        private Long acceptorId;
    }

    /**
     * 完成订单请求体
     */
    @Data
    public static class CompleteBody {
        private Integer rating;
        private String comment;
    }
}
