package com.shunshousong.controller;

import com.shunshousong.entity.Order;
import com.shunshousong.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> findAll(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false, defaultValue = "20") Integer limit) {
        return ResponseEntity.ok(orderService.findAll(status, type, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findOne(id));
    }

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody Order orderData) {
        return ResponseEntity.ok(orderService.create(orderData));
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<Order> accept(@PathVariable Long id, @RequestBody AcceptBody body) {
        return ResponseEntity.ok(orderService.accept(id, body.getAcceptorId()));
    }

    @PostMapping("/{id}/pick")
    public ResponseEntity<Order> pick(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.pick(id));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Order> complete(@PathVariable Long id, @RequestBody CompleteBody body) {
        return ResponseEntity.ok(orderService.complete(id, body.getRating(), body.getComment()));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Order> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.cancel(id));
    }

    // Inner DTO classes
    public static class AcceptBody {
        private Long acceptorId;
        public Long getAcceptorId() { return acceptorId; }
        public void setAcceptorId(Long acceptorId) { this.acceptorId = acceptorId; }
    }

    public static class CompleteBody {
        private Integer rating;
        private String comment;
        public Integer getRating() { return rating; }
        public void setRating(Integer rating) { this.rating = rating; }
        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }
    }
}
