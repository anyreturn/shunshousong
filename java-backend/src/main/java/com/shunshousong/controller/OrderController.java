package com.shunshousong.controller;

import com.shunshousong.entity.Order;
import com.shunshousong.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type) {
        if (status != null) {
            return ResponseEntity.ok(orderService.findByStatus(status));
        }
        return ResponseEntity.ok(orderService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        return orderService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order created = orderService.create(order);
        return ResponseEntity.ok(created);
    }
    
    @PostMapping("/{id}/accept")
    public ResponseEntity<Order> acceptOrder(@PathVariable Long id, @RequestBody Map<String, Long> body) {
        Long acceptorId = body.get("acceptorId");
        Order accepted = orderService.accept(id, acceptorId);
        return accepted != null ? ResponseEntity.ok(accepted) : ResponseEntity.notFound().build();
    }
    
    @PostMapping("/{id}/pick")
    public ResponseEntity<Order> pickOrder(@PathVariable Long id) {
        Order picked = orderService.pick(id);
        return picked != null ? ResponseEntity.ok(picked) : ResponseEntity.notFound().build();
    }
    
    @PostMapping("/{id}/complete")
    public ResponseEntity<Order> completeOrder(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Integer rating = body.get("rating") != null ? (Integer) body.get("rating") : null;
        String comment = body.get("comment") != null ? (String) body.get("comment") : null;
        Order completed = orderService.complete(id, rating, comment);
        return completed != null ? ResponseEntity.ok(completed) : ResponseEntity.notFound().build();
    }
    
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long id) {
        Order cancelled = orderService.cancel(id);
        return cancelled != null ? ResponseEntity.ok(cancelled) : ResponseEntity.notFound().build();
    }
}
