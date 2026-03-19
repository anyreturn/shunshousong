package com.shunshousong.controller;

import com.shunshousong.entity.Payment;
import com.shunshousong.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<Payment>> findByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(paymentService.findByUser(userId));
    }

    @PostMapping
    public ResponseEntity<Payment> create(@RequestBody Payment paymentData) {
        return ResponseEntity.ok(paymentService.create(paymentData));
    }
}
