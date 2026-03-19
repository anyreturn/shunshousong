package com.shunshousong.controller;

import com.shunshousong.dto.UserDTOs.CreateUserDto;
import com.shunshousong.dto.UserDTOs.LoginDto;
import com.shunshousong.dto.UserDTOs.RegisterDto;
import com.shunshousong.entity.User;
import com.shunshousong.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 * 
 * <p>处理用户相关的 HTTP 请求</p>
 * 
 * <p>接口列表：</p>
 * <ul>
 *     <li>GET /api/users/{id} - 查询单个用户</li>
 *     <li>GET /api/users - 查询所有用户</li>
 *     <li>POST /api/users - 创建用户</li>
 *     <li>POST /api/users/register - 用户注册</li>
 *     <li>POST /api/users/login - 用户登录</li>
 *     <li>GET /api/users/check-phone/{phone} - 检查手机号是否可用</li>
 *     <li>POST /api/users/{id}/deposit - 增加押金</li>
 *     <li>POST /api/users/{id}/rating - 更新评分</li>
 * </ul>
 * 
 * <p>依赖：</p>
 * <ul>
 *     <li>UserService: 用户业务逻辑</li>
 * </ul>
 * 
 * @author shunshousong team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 根据 ID 查询用户
     * 
     * GET /api/users/{id}
     * 
     * @param id 用户 ID
     * @return 用户实体
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findOne(id));
    }

    /**
     * 查询所有用户（前 50 个）
     * 
     * GET /api/users
     * 
     * @return 用户列表
     */
    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    /**
     * 创建用户（通过 OpenID）
     * 
     * POST /api/users
     * 
     * @param dto 创建用户请求
     * @return 用户实体
     */
    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody CreateUserDto dto) {
        return ResponseEntity.ok(userService.create(dto));
    }

    /**
     * 用户注册
     * 
     * POST /api/users/register
     * 
     * @param dto 注册请求
     * @return 用户实体
     */
    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(dto));
    }

    /**
     * 用户登录
     * 
     * POST /api/users/login
     * 
     * @param dto 登录请求
     * @return 包含用户信息和 Token 的响应
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginDto dto) {
        return ResponseEntity.ok(userService.login(dto));
    }

    /**
     * 检查手机号是否可用
     * 
     * GET /api/users/check-phone/{phone}
     * 
     * @param phone 手机号
     * @return { available: true/false }
     */
    @GetMapping("/check-phone/{phone}")
    public ResponseEntity<Map<String, Boolean>> checkPhone(@PathVariable String phone) {
        User user = userService.findByPhone(phone);
        Map<String, Boolean> result = new HashMap<>();
        result.put("available", user == null);
        return ResponseEntity.ok(result);
    }

    /**
     * 增加用户押金
     * 
     * POST /api/users/{id}/deposit
     * 
     * @param id 用户 ID
     * @param body 请求体 { amount: 100.0 }
     * @return 更新后的用户实体
     */
    @PostMapping("/{id}/deposit")
    public ResponseEntity<User> addDeposit(
            @PathVariable Long id,
            @RequestBody Map<String, Double> body) {
        Double amount = body.get("amount");
        return ResponseEntity.ok(userService.addDeposit(id, amount));
    }

    /**
     * 更新用户评分
     * 
     * POST /api/users/{id}/rating
     * 
     * @param id 用户 ID
     * @param body 请求体 { rating: 5 }
     * @return 更新后的用户实体
     */
    @PostMapping("/{id}/rating")
    public ResponseEntity<User> updateRating(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> body) {
        Integer rating = body.get("rating");
        return ResponseEntity.ok(userService.updateRating(id, rating));
    }
}
