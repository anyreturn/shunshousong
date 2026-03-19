package com.shunshousong.service;

import com.shunshousong.constant.AppConstants;
import com.shunshousong.dto.UserDTOs.CreateUserDto;
import com.shunshousong.dto.UserDTOs.LoginDto;
import com.shunshousong.dto.UserDTOs.RegisterDto;
import com.shunshousong.entity.User;
import com.shunshousong.exception.AuthenticationException;
import com.shunshousong.exception.UserAlreadyExistsException;
import com.shunshousong.exception.UserNotFoundException;
import com.shunshousong.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 用户服务类
 * 
 * <p>处理用户相关的业务逻辑</p>
 * 
 * <p>职责：</p>
 * <ul>
 *     <li>用户创建、查询</li>
 *     <li>用户注册、登录</li>
 *     <li>用户押金管理</li>
 *     <li>用户信用评分</li>
 * </ul>
 * 
 * <p>依赖：</p>
 * <ul>
 *     <li>UserRepository: 数据持久化</li>
 * </ul>
 * 
 * @author shunshousong team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 查询前 50 个用户（按创建时间倒序）
     * 
     * @return 用户列表
     */
    public List<User> findAll() {
        return userRepository.findTop50ByOrderByCreatedAtDesc();
    }

    /**
     * 根据 ID 查询用户
     * 
     * @param id 用户 ID
     * @return 用户实体
     * @throws UserNotFoundException 用户不存在时抛出
     */
    public User findOne(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * 根据 OpenID 创建用户
     * 
     * <p>如果 OpenID 已存在，返回现有用户</p>
     * 
     * @param dto 创建用户请求
     * @return 用户实体
     */
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
        user.setDeposit(AppConstants.DEFAULT_DEPOSIT);
        user.setBalance(AppConstants.DEFAULT_BALANCE);
        user.setCreditScore(AppConstants.DEFAULT_CREDIT_SCORE);

        return userRepository.save(user);
    }

    /**
     * 用户注册
     * 
     * <p>业务流程：</p>
     * <ol>
     *     <li>检查手机号是否已注册</li>
     *     <li>验证用户协议是否同意</li>
     *     <li>创建新用户</li>
     * </ol>
     * 
     * @param dto 注册请求
     * @return 用户实体
     * @throws UserAlreadyExistsException 手机号已注册时抛出
     * @throws IllegalArgumentException 未同意用户协议时抛出
     */
    @Transactional
    public User register(RegisterDto dto) {
        // 检查手机号是否已注册
        Optional<User> existing = userRepository.findByPhone(dto.getPhone());
        if (existing.isPresent()) {
            throw new UserAlreadyExistsException(dto.getPhone());
        }

        // 验证用户协议
        if (!dto.getIsAgreementAccepted()) {
            throw new IllegalArgumentException(AppConstants.ERROR_AGREEMENT_REQUIRED);
        }

        User user = new User();
        user.setPhone(dto.getPhone());
        user.setPassword(dto.getPassword());
        user.setNickname(dto.getNickname());
        user.setAvatar(dto.getAvatar() != null ? dto.getAvatar() : "");
        user.setDeposit(AppConstants.DEFAULT_DEPOSIT);
        user.setBalance(AppConstants.DEFAULT_BALANCE);
        user.setCreditScore(AppConstants.DEFAULT_CREDIT_SCORE);

        return userRepository.save(user);
    }

    /**
     * 用户登录
     * 
     * <p>业务流程：</p>
     * <ol>
     *     <li>根据手机号查询用户</li>
     *     <li>验证密码</li>
     *     <li>生成 Token</li>
     * </ol>
     * 
     * @param dto 登录请求
     * @return 包含用户信息和 Token 的 Map
     * @throws UserNotFoundException 用户不存在时抛出
     * @throws AuthenticationException 密码错误时抛出
     */
    @Transactional
    public Map<String, Object> login(LoginDto dto) {
        // 查询用户
        User user = userRepository.findByPhone(dto.getPhone())
                .orElseThrow(() -> new UserNotFoundException(dto.getPhone()));

        // 验证密码
        if (!user.validatePassword(dto.getPassword())) {
            throw new AuthenticationException();
        }

        // 生成 Token
        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("id", user.getId());
        tokenData.put("phone", user.getPhone());
        tokenData.put("exp", System.currentTimeMillis() + AppConstants.TOKEN_EXPIRE_MS);

        String token = Base64.getEncoder().encodeToString(
                tokenData.toString().getBytes()
        );

        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("token", token);

        return result;
    }

    /**
     * 根据手机号查询用户
     * 
     * @param phone 手机号
     * @return 用户实体，不存在时返回 null
     */
    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone).orElse(null);
    }

    /**
     * 根据邮箱查询用户
     * 
     * @param email 邮箱
     * @return 用户实体，不存在时返回 null
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    /**
     * 增加用户押金
     * 
     * @param id 用户 ID
     * @param amount 增加金额
     * @return 更新后的用户实体
     * @throws UserNotFoundException 用户不存在时抛出
     */
    @Transactional
    public User addDeposit(Long id, Double amount) {
        userRepository.incrementDeposit(id, amount);
        return findOne(id);
    }

    /**
     * 更新用户评分
     * 
     * <p>计算方式：(原分数 + 新评分) / 2</p>
     * 
     * @param id 用户 ID
     * @param rating 新评分
     * @return 更新后的用户实体
     * @throws UserNotFoundException 用户不存在时抛出
     */
    @Transactional
    public User updateRating(Long id, Integer rating) {
        User user = findOne(id);
        Integer newScore = Math.round((user.getCreditScore() + rating) / 2.0f);
        user.setCreditScore(newScore);
        return userRepository.save(user);
    }
}
