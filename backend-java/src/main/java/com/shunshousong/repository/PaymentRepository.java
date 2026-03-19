package com.shunshousong.repository;

import com.shunshousong.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findTop50ByUserIdOrderByCreatedAtDesc(Long userId);
}
