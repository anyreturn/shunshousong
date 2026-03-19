package com.shunshousong.entity;

import javax.persistence.*;
import java.time.Instant;

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

    private Long acceptorId;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String status = "pending";

    @Column(nullable = false, columnDefinition = "TEXT")
    private String pickupAddress;

    private Double pickupLat;
    private Double pickupLng;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String deliveryAddress;

    private Double deliveryLat;
    private Double deliveryLng;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double reward;

    private String expectedTime;
    private Instant acceptedAt;
    private Instant pickedAt;
    private Instant completedAt;
    private Integer publisherRating;
    private Integer acceptorRating;

    @Column(columnDefinition = "TEXT")
    private String publisherComment;

    @Column(columnDefinition = "TEXT")
    private String acceptorComment;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    @Override
    public String toString() {
        return "Order{id=" + id + ", orderNo='" + orderNo + "', status='" + status + "'}";
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public Long getPublisherId() { return publisherId; }
    public void setPublisherId(Long publisherId) { this.publisherId = publisherId; }
    public Long getAcceptorId() { return acceptorId; }
    public void setAcceptorId(Long acceptorId) { this.acceptorId = acceptorId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPickupAddress() { return pickupAddress; }
    public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }
    public Double getPickupLat() { return pickupLat; }
    public void setPickupLat(Double pickupLat) { this.pickupLat = pickupLat; }
    public Double getPickupLng() { return pickupLng; }
    public void setPickupLng(Double pickupLng) { this.pickupLng = pickupLng; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public Double getDeliveryLat() { return deliveryLat; }
    public void setDeliveryLat(Double deliveryLat) { this.deliveryLat = deliveryLat; }
    public Double getDeliveryLng() { return deliveryLng; }
    public void setDeliveryLng(Double deliveryLng) { this.deliveryLng = deliveryLng; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getReward() { return reward; }
    public void setReward(Double reward) { this.reward = reward; }
    public String getExpectedTime() { return expectedTime; }
    public void setExpectedTime(String expectedTime) { this.expectedTime = expectedTime; }
    public Instant getAcceptedAt() { return acceptedAt; }
    public void setAcceptedAt(Instant acceptedAt) { this.acceptedAt = acceptedAt; }
    public Instant getPickedAt() { return pickedAt; }
    public void setPickedAt(Instant pickedAt) { this.pickedAt = pickedAt; }
    public Instant getCompletedAt() { return completedAt; }
    public void setCompletedAt(Instant completedAt) { this.completedAt = completedAt; }
    public Integer getPublisherRating() { return publisherRating; }
    public void setPublisherRating(Integer publisherRating) { this.publisherRating = publisherRating; }
    public Integer getAcceptorRating() { return acceptorRating; }
    public void setAcceptorRating(Integer acceptorRating) { this.acceptorRating = acceptorRating; }
    public String getPublisherComment() { return publisherComment; }
    public void setPublisherComment(String publisherComment) { this.publisherComment = publisherComment; }
    public String getAcceptorComment() { return acceptorComment; }
    public void setAcceptorComment(String acceptorComment) { this.acceptorComment = acceptorComment; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
