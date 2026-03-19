package com.shunshousong.service;

import com.shunshousong.entity.Payment;
import com.shunshousong.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public List<Payment> findByUser(Long userId) {
        return paymentRepository.findTop50ByUserIdOrderByCreatedAtDesc(userId);
    }

    @Transactional
    public Payment create(Payment paymentData) {
        return paymentRepository.save(paymentData);
    }
}
