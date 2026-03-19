package com.shunshousong.repository;

import com.shunshousong.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findTop20ByOrderByCreatedAtDesc();

    List<Order> findByStatusOrderByCreatedAtDesc(String status);

    List<Order> findByTypeOrderByCreatedAtDesc(String type);

    List<Order> findByStatusAndTypeOrderByCreatedAtDesc(String status, String type);

    @Query("SELECT o FROM Order o WHERE (:status IS NULL OR o.status = :status) " +
           "AND (:type IS NULL OR o.type = :type) " +
           "ORDER BY o.createdAt DESC")
    List<Order> findByFilters(String status, String type);
}
