package com.shunshousong.service;

import com.shunshousong.entity.Order;
import com.shunshousong.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

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

    public Order findOne(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("订单 " + id + " 不存在"));
    }

    @Transactional
    public Order create(Order orderData) {
        String orderNo = "SS" + System.currentTimeMillis() + 
                String.format("%06d", (int)(Math.random() * 1000000)).toUpperCase();
        orderData.setOrderNo(orderNo);
        orderData.setStatus("pending");
        return orderRepository.save(orderData);
    }

    @Transactional
    public Order accept(Long id, Long acceptorId) {
        Order order = findOne(id);
        order.setAcceptorId(acceptorId);
        order.setStatus("accepted");
        order.setAcceptedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    @Transactional
    public Order pick(Long id) {
        Order order = findOne(id);
        order.setStatus("picked");
        order.setPickedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

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

    @Transactional
    public Order cancel(Long id) {
        Order order = findOne(id);
        order.setStatus("cancelled");
        return orderRepository.save(order);
    }
}
