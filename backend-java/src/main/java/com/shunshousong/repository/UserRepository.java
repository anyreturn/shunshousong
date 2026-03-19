package com.shunshousong.repository;

import com.shunshousong.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByOpenid(String openid);

    Optional<User> findByPhone(String phone);

    Optional<User> findByEmail(String email);

    List<User> findTop50ByOrderByCreatedAtDesc();

    @Modifying
    @Query("UPDATE User u SET u.deposit = u.deposit + :amount WHERE u.id = :id")
    int incrementDeposit(@Param("id") Long id, @Param("amount") Double amount);
}
