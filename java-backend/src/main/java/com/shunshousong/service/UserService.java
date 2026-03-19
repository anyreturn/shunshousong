package com.shunshousong.service;

import com.shunshousong.entity.User;
import com.shunshousong.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    public Optional<User> findByOpenid(String openid) {
        return userRepository.findByOpenid(openid);
    }
    
    public User create(User user) {
        Optional<User> existing = userRepository.findByOpenid(user.getOpenid());
        if (existing.isPresent()) {
            return existing.get();
        }
        user.setCreditScore(100);
        user.setDeposit(0.0);
        user.setBalance(0.0);
        user.setIsVerified(false);
        user.setCompletedOrders(0);
        user.setTotalEarnings(0.0);
        return userRepository.save(user);
    }
    
    public User addDeposit(Long id, Double amount) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setDeposit(user.getDeposit() + amount);
            return userRepository.save(user);
        }
        return null;
    }
    
    public User updateCreditScore(Long id, Integer score) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setCreditScore(score);
            return userRepository.save(user);
        }
        return null;
    }
}
