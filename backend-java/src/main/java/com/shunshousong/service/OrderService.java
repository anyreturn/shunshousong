package com.shunshousong.service;

import com.shunshousong.entity.Order;
import com.shunshousong.exception.OrderNotFoundException;
import com.shunshousong.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单服务类
 * 
 * <p>处理订单相关的业务逻辑</p>
 * 
 * <p>职责：</p>
 * <ul>
 *     <li>订单查询</li>
 *     <li>订单创建</li>
 *     <li>订单状态流转（接单/取货/完成/取消）</li>
 * </ul>
 * 
 * <p>依赖：</p>
 * <ul>
 *     <li>OrderRepository: 数据持久化</li>
 * </ul>
 * 
 * @author shunshousong team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    /**
     * 查询订单列表（支持筛选）
     * 
     * @param status 订单状态（可选）
     * @param type 订单类型（可选）
     * @param limit 返回数量限制
     * @return 订单列表
     */
    public List<Order> findAll(String status, String type, Integer limit) {
        if (status != null && type != null) {
            return orderRepository.findByStatusAndTypeOrderByCreatedAtDesc(status, type);
        } else if (status != null) {
            return orderRepository.findByStatusOrderByCreatedAtDesc(status);
        } else if (type != null) {
            return orderRepository.findByTypeOrderByCreatedAtDesc(type);
        } else {
            return orderRepository.findTop20ByOrderByCreatedAtDesc();
        }
    }

    /**
     * 根据 ID 查询订单
     * 
     * @param id 订单 ID
     * @return 订单实体
     * @throws OrderNotFoundException 订单不存在时抛出
     */
    public Order findOne(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    /**
     * 创建订单
     * 
     * <p>自动生成订单号：SS + 时间戳 + 6 位随机数</p>
     * 
     * @param orderData 订单数据
     * @return 创建的订单实体
     */
    @Transactional
    public Order create(Order orderData) {
        String orderNo = "SS" + System.currentTimeMillis() + 
                String.format("%06d", (int)(Math.random() * 1000000));
        orderData.setOrderNo(orderNo);
        orderData.setStatus("pending");
        return orderRepository.save(orderData);
    }

    /**
     * 接单
     * 
     * <p>状态流转：pending → accepted</p>
     * 
     * @param id 订单 ID
     * @param acceptorId 接单者 ID
     * @return 更新后的订单实体
     * @throws OrderNotFoundException 订单不存在时抛出
     */
    @Transactional
    public Order accept(Long id, Long acceptorId) {
        Order order = findOne(id);
        order.setAcceptorId(acceptorId);
        order.setStatus("accepted");
        order.setAcceptedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    /**
     * 取货
     * 
     * <p>状态流转：accepted → picked</p>
     * 
     * @param id 订单 ID
     * @return 更新后的订单实体
     * @throws OrderNotFoundException 订单不存在时抛出
     */
    @Transactional
    public Order pick(Long id) {
        Order order = findOne(id);
        order.setStatus("picked");
        order.setPickedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    /**
     * 完成订单
     * 
     * <p>状态流转：picked → completed</p>
     * 
     * @param id 订单 ID
     * @param rating 评分（可选）
     * @param comment 评价（可选）
     * @return 更新后的订单实体
     * @throws OrderNotFoundException 订单不存在时抛出
     */
    @Transactional
    public Order complete(Long id, Integer rating, String comment) {
        Order order = findOne(id);
        order.setStatus("completed");
        order.setCompletedAt(LocalDateTime.now());
        if (rating != null) {
            order.setPublisherRating(rating);
        }
        if (comment != null) {
            order.setPublisherComment(comment);
        }
        return orderRepository.save(order);
    }

    /**
     * 取消订单
     * 
     * <p>状态流转：任意状态 → cancelled</p>
     * 
     * @param id 订单 ID
     * @return 更新后的订单实体
     * @throws OrderNotFoundException 订单不存在时抛出
     */
    @Transactional
    public Order cancel(Long id) {
        Order order = findOne(id);
        order.setStatus("cancelled");
        return orderRepository.save(order);
    }
}
