package com.shunshousong.repository;

import com.shunshousong.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatusOrderByCreatedAtDesc(String status);
    List<Order> findByPublisherIdOrderByCreatedAtDesc(Long publisherId);
    List<Order> findByAcceptorIdOrderByCreatedAtDesc(Long acceptorId);
}
