package com.shunshousong.service;

import com.shunshousong.dto.UserDTOs.*;
import com.shunshousong.entity.User;
import com.shunshousong.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findTop50ByOrderByCreatedAtDesc();
    }

    public User findOne(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户 " + id + " 不存在"));
    }

    public User create(CreateUserDto dto) {
        Optional<User> existing = userRepository.findByOpenid(dto.getOpenid());
        if (existing.isPresent()) {
            return existing.get();
        }

        User user = new User();
        user.setOpenid(dto.getOpenid());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setNickname(dto.getNickname());
        user.setAvatar(dto.getAvatar());
        user.setRealName(dto.getRealName());
        user.setIdCard(dto.getIdCard());

        return userRepository.save(user);
    }

    @Transactional
    public User register(RegisterDto dto) {
        Optional<User> existing = userRepository.findByPhone(dto.getPhone());
        if (existing.isPresent()) {
            throw new RuntimeException("该手机号已注册");
        }

        if (!dto.getIsAgreementAccepted()) {
            throw new RuntimeException("请同意用户协议");
        }

        User user = new User();
        user.setPhone(dto.getPhone());
        user.setPassword(dto.getPassword());
        user.setNickname(dto.getNickname());
        user.setAvatar(dto.getAvatar() != null ? dto.getAvatar() : "");

        return userRepository.save(user);
    }

    @Transactional
    public Map<String, Object> login(LoginDto dto) {
        User user = userRepository.findByPhone(dto.getPhone())
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (!user.validatePassword(dto.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 生成简单的 token（实际项目中建议使用 JWT）
        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("id", user.getId());
        tokenData.put("phone", user.getPhone());
        tokenData.put("exp", System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000);

        String token = Base64.getEncoder().encodeToString(
                tokenData.toString().getBytes()
        );

        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("token", token);

        return result;
    }

    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone).orElse(null);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    public User addDeposit(Long id, Double amount) {
        userRepository.incrementDeposit(id, amount);
        return findOne(id);
    }

    @Transactional
    public User updateRating(Long id, Integer rating) {
        User user = findOne(id);
        Integer newScore = Math.round((user.getCreditScore() + rating) / 2.0f);
        user.setCreditScore(newScore);
        return userRepository.save(user);
    }
}
