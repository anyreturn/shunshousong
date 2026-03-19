package com.shunshousong.entity;

import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = true)
    private String openid;

    @Column(unique = true, nullable = true)
    private String phone;

    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = true)
    private String avatar;

    @Column(nullable = true)
    private String realName;

    @Column(nullable = true)
    private String idCard;

    @Column(precision = 10, scale = 2)
    private Double deposit = 0.0;

    @Column(precision = 10, scale = 2)
    private Double balance = 0.0;

    @Column(nullable = false)
    private Integer creditScore = 100;

    @Column(nullable = false)
    private Boolean isVerified = false;

    @Column(nullable = false)
    private Integer completedOrders = 0;

    @Column(nullable = false)
    private Double totalEarnings = 0.0;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    private void hashPassword() {
        if (password != null && !password.startsWith("$2")) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            password = encoder.encode(password);
        }
    }

    public boolean validatePassword(String password) {
        if (this.password == null) return false;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(password, this.password);
    }
}
