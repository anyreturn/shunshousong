package com.shunshousong.service;

import com.shunshousong.entity.Order;
import com.shunshousong.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    public List<Order> findAll() {
        return orderRepository.findAll();
    }
    
    public List<Order> findByStatus(String status) {
        return orderRepository.findByStatusOrderByCreatedAtDesc(status);
    }
    
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }
    
    public Order create(Order order) {
        order.setOrderNo("SS" + System.currentTimeMillis() + generateRandomString(6));
        order.setStatus("pending");
        order.setCreatedAt(Instant.now());
        order.setUpdatedAt(Instant.now());
        return orderRepository.save(order);
    }
    
    public Order accept(Long id, Long acceptorId) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setAcceptorId(acceptorId);
            order.setStatus("accepted");
            order.setAcceptedAt(Instant.now());
            return orderRepository.save(order);
        }
        return null;
    }
    
    public Order pick(Long id) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus("picked");
            order.setPickedAt(Instant.now());
            return orderRepository.save(order);
        }
        return null;
    }
    
    public Order complete(Long id, Integer rating, String comment) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus("completed");
            order.setCompletedAt(Instant.now());
            if (rating != null) order.setPublisherRating(rating);
            if (comment != null) order.setPublisherComment(comment);
            return orderRepository.save(order);
        }
        return null;
    }
    
    public Order cancel(Long id) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus("cancelled");
            return orderRepository.save(order);
        }
        return null;
    }
    
    private String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
