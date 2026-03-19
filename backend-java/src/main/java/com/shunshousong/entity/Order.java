package com.shunshousong.entity;

import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String orderNo;

    @Column(nullable = false)
    private Long publisherId;

    @Column(nullable = true)
    private Long acceptorId;

    @Column(nullable = false)
    private String type; // 'deliver' | 'pickup'

    @Column(nullable = false)
    private String status = "pending"; // 'pending' | 'accepted' | 'picked' | 'delivering' | 'completed' | 'cancelled'

    @Column(columnDefinition = "TEXT", nullable = false)
    private String pickupAddress;

    @Column(precision = 10, scale = 8, nullable = true)
    private Double pickupLat;

    @Column(precision = 11, scale = 8, nullable = true)
    private Double pickupLng;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String deliveryAddress;

    @Column(precision = 10, scale = 8, nullable = true)
    private Double deliveryLat;

    @Column(precision = 11, scale = 8, nullable = true)
    private Double deliveryLng;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String description;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String images;

    @Column(precision = 10, scale = 2, nullable = false)
    private Double reward;

    @Column(nullable = true)
    private String expectedTime;

    @Column(nullable = true)
    private LocalDateTime acceptedAt;

    @Column(nullable = true)
    private LocalDateTime pickedAt;

    @Column(nullable = true)
    private LocalDateTime completedAt;

    @Column(nullable = true)
    private Integer publisherRating;

    @Column(nullable = true)
    private Integer acceptorRating;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String publisherComment;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String acceptorComment;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
