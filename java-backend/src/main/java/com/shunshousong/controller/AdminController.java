package com.shunshousong.controller;

import com.shunshousong.entity.Order;
import com.shunshousong.entity.User;
import com.shunshousong.repository.OrderRepository;
import com.shunshousong.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 用户统计
        List<User> allUsers = userRepository.findAll();
        stats.put("totalUsers", allUsers.size());
        stats.put("verifiedUsers", allUsers.stream().filter(User::getIsVerified).count());
        
        // 订单统计
        List<Order> allOrders = orderRepository.findAll();
        stats.put("totalOrders", allOrders.size());
        stats.put("pendingOrders", allOrders.stream().filter(o -> "pending".equals(o.getStatus())).count());
        stats.put("completedOrders", allOrders.stream().filter(o -> "completed".equals(o.getStatus())).count());
        stats.put("cancelledOrders", allOrders.stream().filter(o -> "cancelled".equals(o.getStatus())).count());
        
        // 金额统计
        double totalReward = allOrders.stream().mapToDouble(Order::getReward).sum();
        stats.put("totalReward", totalReward);
        
        // 今日数据
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Shanghai"));
        List<Order> todayOrders = allOrders.stream()
            .filter(o -> o.getCreatedAt() != null && 
                o.getCreatedAt().atZone(ZoneId.of("Asia/Shanghai")).toLocalDate().equals(today))
            .collect(Collectors.toList());
        stats.put("todayOrders", todayOrders.size());
        stats.put("todayReward", todayOrders.stream().mapToDouble(Order::getReward).sum());
        
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
    
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderRepository.findAll());
    }
    
    @PostMapping("/users/{id}/verify")
    public ResponseEntity<User> verifyUser(@PathVariable Long id) {
        return userRepository.findById(id)
            .map(user -> {
                user.setIsVerified(true);
                return ResponseEntity.ok(userRepository.save(user));
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/orders/{id}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long id) {
        return orderRepository.findById(id)
            .map(order -> {
                order.setStatus("cancelled");
                return ResponseEntity.ok(orderRepository.save(order));
            })
            .orElse(ResponseEntity.notFound().build());
    }
}
