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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * UserService 单元测试
 * 
 * <p>测试覆盖：</p>
 * <ul>
 *     <li>用户查询（findAll, findOne, findByPhone, findByEmail）</li>
 *     <li>用户创建（create, register）</li>
 *     <li>用户认证（login）</li>
 *     <li>用户操作（addDeposit, updateRating）</li>
 *     <li>异常处理（UserNotFoundException, UserAlreadyExistsException, AuthenticationException）</li>
 * </ul>
 * 
 * @author shunshousong team
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserService 单元测试")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private CreateUserDto createDto;
    private RegisterDto registerDto;
    private LoginDto loginDto;
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("password123");
        
        // 创建测试用户
        testUser = new User();
        testUser.setId(1L);
        testUser.setPhone("13800138000");
        testUser.setEmail("test@example.com");
        testUser.setPassword(encodedPassword);
        testUser.setNickname("测试用户");
        testUser.setAvatar("https://example.com/avatar.jpg");
        testUser.setDeposit(AppConstants.DEFAULT_DEPOSIT);
        testUser.setBalance(AppConstants.DEFAULT_BALANCE);
        testUser.setCreditScore(AppConstants.DEFAULT_CREDIT_SCORE);

        // 创建测试数据
        createDto = new CreateUserDto();
        createDto.setOpenid("wx_openid_123");
        createDto.setPhone("13800138000");
        createDto.setEmail("test@example.com");
        createDto.setPassword("password123");
        createDto.setNickname("新用户");

        registerDto = new RegisterDto();
        registerDto.setPhone("13900139000");
        registerDto.setPassword("password123");
        registerDto.setNickname("注册用户");
        registerDto.setIsAgreementAccepted(true);

        loginDto = new LoginDto();
        loginDto.setPhone("13800138000");
        loginDto.setPassword("password123");
    }

    // ==================== 查询用户 ====================

    @Test
    @DisplayName("findAll - 返回前 50 个用户（按创建时间倒序）")
    void findAll() {
        // Given
        List<User> users = Arrays.asList(testUser, new User());
        when(userRepository.findTop50ByOrderByCreatedAtDesc()).thenReturn(users);

        // When
        List<User> result = userService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findTop50ByOrderByCreatedAtDesc();
    }

    @Test
    @DisplayName("findOne - 找到存在的用户")
    void findOne_whenUserExists() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        User result = userService.findOne(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("测试用户", result.getNickname());
    }

    @Test
    @DisplayName("findOne - 用户不存在时抛出 UserNotFoundException")
    void findOne_whenUserNotExists() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        UserNotFoundException exception = assertThrows(
            UserNotFoundException.class, 
            () -> userService.findOne(999L)
        );
        assertEquals("用户 999 不存在", exception.getMessage());
        assertEquals("USER_NOT_FOUND", exception.getErrorCode());
    }

    @Test
    @DisplayName("findByPhone - 找到用户")
    void findByPhone() {
        // Given
        when(userRepository.findByPhone("13800138000")).thenReturn(Optional.of(testUser));

        // When
        User result = userService.findByPhone("13800138000");

        // Then
        assertNotNull(result);
        assertEquals("13800138000", result.getPhone());
    }

    @Test
    @DisplayName("findByPhone - 用户不存在返回 null")
    void findByPhone_whenNotExists() {
        // Given
        when(userRepository.findByPhone("13800138000")).thenReturn(Optional.empty());

        // When
        User result = userService.findByPhone("13800138000");

        // Then
        assertNull(result);
    }

    @Test
    @DisplayName("findByEmail - 找到用户")
    void findByEmail() {
        // Given
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        // When
        User result = userService.findByEmail("test@example.com");

        // Then
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    // ==================== 创建用户 ====================

    @Test
    @DisplayName("create - OpenID 已存在时返回现有用户")
    void create_whenOpenidExists() {
        // Given
        when(userRepository.findByOpenid("wx_openid_123")).thenReturn(Optional.of(testUser));

        // When
        User result = userService.create(createDto);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("create - OpenID 不存在时创建新用户")
    void create_whenOpenidNotExists() {
        // Given
        when(userRepository.findByOpenid("wx_openid_123")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User result = userService.create(createDto);

        // Then
        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("register - 手机号已注册时抛出 UserAlreadyExistsException")
    void register_whenPhoneExists() {
        // Given
        when(userRepository.findByPhone("13900139000")).thenReturn(Optional.of(testUser));

        // When & Then
        UserAlreadyExistsException exception = assertThrows(
            UserAlreadyExistsException.class, 
            () -> userService.register(registerDto)
        );
        assertEquals("该手机号已注册：13900139000", exception.getMessage());
        assertEquals("USER_ALREADY_EXISTS", exception.getErrorCode());
    }

    @Test
    @DisplayName("register - 未同意协议时抛出 IllegalArgumentException")
    void register_whenAgreementNotAccepted() {
        // Given
        registerDto.setIsAgreementAccepted(false);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> userService.register(registerDto)
        );
        assertEquals("请同意用户协议", exception.getMessage());
    }

    @Test
    @DisplayName("register - 成功注册新用户")
    void register_success() {
        // Given
        when(userRepository.findByPhone("13900139000")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User result = userService.register(registerDto);

        // Then
        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    // ==================== 用户登录 ====================

    @Test
    @DisplayName("login - 成功登录并返回 Token")
    void login_success() {
        // Given
        when(userRepository.findByPhone("13800138000")).thenReturn(Optional.of(testUser));

        // When
        Map<String, Object> result = userService.login(loginDto);

        // Then
        assertNotNull(result);
        assertTrue(result.containsKey("user"));
        assertTrue(result.containsKey("token"));
        assertNotNull(result.get("token"));
    }

    @Test
    @DisplayName("login - 用户不存在时抛出 UserNotFoundException")
    void login_whenUserNotExists() {
        // Given
        when(userRepository.findByPhone("13800138000")).thenReturn(Optional.empty());

        // When & Then
        UserNotFoundException exception = assertThrows(
            UserNotFoundException.class, 
            () -> userService.login(loginDto)
        );
        assertEquals("用户 13800138000 不存在", exception.getMessage());
        assertEquals("USER_NOT_FOUND", exception.getErrorCode());
    }

    @Test
    @DisplayName("login - 密码错误时抛出 AuthenticationException")
    void login_whenWrongPassword() {
        // Given
        User userWithWrongPassword = new User();
        userWithWrongPassword.setId(1L);
        userWithWrongPassword.setPhone("13800138000");
        userWithWrongPassword.setPassword("$2a$10$differentHashThatDoesNotMatch");
        
        when(userRepository.findByPhone("13800138000")).thenReturn(Optional.of(userWithWrongPassword));

        // When & Then
        AuthenticationException exception = assertThrows(
            AuthenticationException.class, 
            () -> userService.login(loginDto)
        );
        assertEquals("密码错误", exception.getMessage());
        assertEquals("WRONG_PASSWORD", exception.getErrorCode());
    }

    // ==================== 用户操作 ====================

    @Test
    @DisplayName("addDeposit - 成功增加押金")
    void addDeposit() {
        // Given
        when(userRepository.incrementDeposit(1L, 100.0)).thenReturn(1);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        User result = userService.addDeposit(1L, 100.0);

        // Then
        assertNotNull(result);
        verify(userRepository, times(1)).incrementDeposit(1L, 100.0);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("addDeposit - 用户不存在时抛出异常")
    void addDeposit_whenUserNotExists() {
        // Given
        when(userRepository.incrementDeposit(1L, 100.0)).thenReturn(1);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        UserNotFoundException exception = assertThrows(
            UserNotFoundException.class, 
            () -> userService.addDeposit(1L, 100.0)
        );
        assertEquals("用户 1 不存在", exception.getMessage());
    }

    @Test
    @DisplayName("updateRating - 成功更新评分（平均分）")
    void updateRating() {
        // Given
        testUser.setCreditScore(100);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User result = userService.updateRating(1L, 5);

        // Then
        assertNotNull(result);
        // (100 + 5) / 2 = 52.5 -> 53 (四舍五入)
        assertEquals(53, result.getCreditScore());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("updateRating - 用户不存在时抛出异常")
    void updateRating_whenUserNotExists() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        UserNotFoundException exception = assertThrows(
            UserNotFoundException.class, 
            () -> userService.updateRating(1L, 5)
        );
        assertEquals("用户 1 不存在", exception.getMessage());
    }
}
