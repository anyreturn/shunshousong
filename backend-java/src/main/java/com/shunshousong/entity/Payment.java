package com.shunshousong.entity;

import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId", insertable = false, updatable = false)
    private Order order;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String type; // 'pay' | 'refund' | 'withdraw' | 'reward'

    @Column(precision = 10, scale = 2, nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String status = "pending"; // 'pending' | 'success' | 'failed'

    @Column(nullable = true)
    private String transactionId;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String description;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
