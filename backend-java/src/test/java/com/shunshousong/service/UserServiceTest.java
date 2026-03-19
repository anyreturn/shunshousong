package com.shunshousong.service;

import com.shunshousong.dto.UserDTOs.*;
import com.shunshousong.entity.User;
import com.shunshousong.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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

    @BeforeEach
    void setUp() {
        // 使用 BCrypt 加密密码
        org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder encoder = 
            new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("password123");
        
        testUser = new User();
        testUser.setId(1L);
        testUser.setPhone("13800138000");
        testUser.setEmail("test@example.com");
        testUser.setPassword(encodedPassword);
        testUser.setNickname("测试用户");
        testUser.setAvatar("https://example.com/avatar.jpg");
        testUser.setDeposit(0.0);
        testUser.setBalance(0.0);
        testUser.setCreditScore(100);

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

    @Test
    @DisplayName("findAll - 返回前 50 个用户")
    void findAll() {
        List<User> users = Arrays.asList(testUser, new User());
        when(userRepository.findTop50ByOrderByCreatedAtDesc()).thenReturn(users);

        List<User> result = userService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findTop50ByOrderByCreatedAtDesc();
    }

    @Test
    @DisplayName("findOne - 找到存在的用户")
    void findOne_whenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        User result = userService.findOne(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("测试用户", result.getNickname());
    }

    @Test
    @DisplayName("findOne - 用户不存在时抛出异常")
    void findOne_whenUserNotExists() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.findOne(999L);
        });

        assertEquals("用户 999 不存在", exception.getMessage());
    }

    @Test
    @DisplayName("create -  openid 已存在时返回现有用户")
    void create_whenOpenidExists() {
        when(userRepository.findByOpenid("wx_openid_123")).thenReturn(Optional.of(testUser));

        User result = userService.create(createDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("create - openid 不存在时创建新用户")
    void create_whenOpenidNotExists() {
        when(userRepository.findByOpenid("wx_openid_123")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.create(createDto);

        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("register - 手机号已注册时抛出异常")
    void register_whenPhoneExists() {
        when(userRepository.findByPhone("13900139000")).thenReturn(Optional.of(testUser));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.register(registerDto);
        });

        assertEquals("该手机号已注册", exception.getMessage());
    }

    @Test
    @DisplayName("register - 未同意协议时抛出异常")
    void register_whenAgreementNotAccepted() {
        registerDto.setIsAgreementAccepted(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.register(registerDto);
        });

        assertEquals("请同意用户协议", exception.getMessage());
    }

    @Test
    @DisplayName("register - 成功注册新用户")
    void register_success() {
        when(userRepository.findByPhone("13900139000")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.register(registerDto);

        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("login - 成功登录")
    void login_success() {
        when(userRepository.findByPhone("13800138000")).thenReturn(Optional.of(testUser));

        Map<String, Object> result = userService.login(loginDto);

        assertNotNull(result);
        assertTrue(result.containsKey("user"));
        assertTrue(result.containsKey("token"));
        assertNotNull(result.get("token"));
    }

    @Test
    @DisplayName("login - 用户不存在时抛出异常")
    void login_whenUserNotExists() {
        when(userRepository.findByPhone("13800138000")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.login(loginDto);
        });

        assertEquals("用户不存在", exception.getMessage());
    }

    @Test
    @DisplayName("login - 密码错误时抛出异常")
    void login_whenWrongPassword() {
        // 模拟密码验证失败
        User userWithWrongPassword = new User();
        userWithWrongPassword.setId(1L);
        userWithWrongPassword.setPhone("13800138000");
        userWithWrongPassword.setPassword("$2a$10$differentPassword");
        
        when(userRepository.findByPhone("13800138000")).thenReturn(Optional.of(userWithWrongPassword));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.login(loginDto);
        });

        assertEquals("密码错误", exception.getMessage());
    }

    @Test
    @DisplayName("findByPhone - 找到用户")
    void findByPhone() {
        when(userRepository.findByPhone("13800138000")).thenReturn(Optional.of(testUser));

        User result = userService.findByPhone("13800138000");

        assertNotNull(result);
        assertEquals("13800138000", result.getPhone());
    }

    @Test
    @DisplayName("findByPhone - 用户不存在返回 null")
    void findByPhone_whenNotExists() {
        when(userRepository.findByPhone("13800138000")).thenReturn(Optional.empty());

        User result = userService.findByPhone("13800138000");

        assertNull(result);
    }

    @Test
    @DisplayName("findByEmail - 找到用户")
    void findByEmail() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        User result = userService.findByEmail("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    @DisplayName("addDeposit - 成功增加押金")
    void addDeposit() {
        when(userRepository.incrementDeposit(1L, 100.0)).thenReturn(1);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        User result = userService.addDeposit(1L, 100.0);

        assertNotNull(result);
        verify(userRepository, times(1)).incrementDeposit(1L, 100.0);
    }

    @Test
    @DisplayName("updateRating - 成功更新评分")
    void updateRating() {
        testUser.setCreditScore(100);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.updateRating(1L, 5);

        assertNotNull(result);
        // (100 + 5) / 2 = 52.5 -> 53 (四舍五入)
        assertEquals(53, result.getCreditScore());
        verify(userRepository, times(1)).save(any(User.class));
    }
}
