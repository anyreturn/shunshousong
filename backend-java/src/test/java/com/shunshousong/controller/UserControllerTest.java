package com.shunshousong.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunshousong.dto.UserDTOs.*;
import com.shunshousong.entity.User;
import com.shunshousong.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@DisplayName("UserController 集成测试")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private User testUser;
    private CreateUserDto createDto;
    private RegisterDto registerDto;
    private LoginDto loginDto;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setPhone("13800138000");
        testUser.setEmail("test@example.com");
        testUser.setNickname("测试用户");
        testUser.setAvatar("https://example.com/avatar.jpg");
        testUser.setDeposit(0.0);
        testUser.setBalance(0.0);
        testUser.setCreditScore(100);

        createDto = new CreateUserDto();
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
    @DisplayName("GET /api/users/{id} - 获取单个用户")
    void findOne() throws Exception {
        when(userService.findOne(1L)).thenReturn(testUser);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nickname").value("测试用户"))
                .andExpect(jsonPath("$.phone").value("13800138000"));

        verify(userService, times(1)).findOne(1L);
    }

    @Test
    @DisplayName("GET /api/users/{id} - 用户不存在返回错误")
    void findOne_notFound() throws Exception {
        when(userService.findOne(999L)).thenThrow(new RuntimeException("用户 999 不存在"));

        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().is4xxClientError());

        verify(userService, times(1)).findOne(999L);
    }

    @Test
    @DisplayName("GET /api/users - 获取用户列表")
    void findAll() throws Exception {
        List<User> users = Arrays.asList(testUser, new User());
        when(userService.findAll()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

        verify(userService, times(1)).findAll();
    }

    @Test
    @DisplayName("POST /api/users - 创建用户")
    void create() throws Exception {
        when(userService.create(any(CreateUserDto.class))).thenReturn(testUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nickname").value("测试用户"));

        verify(userService, times(1)).create(any(CreateUserDto.class));
    }

    @Test
    @DisplayName("POST /api/users/register - 用户注册")
    void register() throws Exception {
        when(userService.register(any(RegisterDto.class))).thenReturn(testUser);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        verify(userService, times(1)).register(any(RegisterDto.class));
    }

    @Test
    @DisplayName("POST /api/users/login - 用户登录")
    void login() throws Exception {
        Map<String, Object> loginResult = new HashMap<>();
        loginResult.put("user", testUser);
        loginResult.put("token", "mocked_token_12345");

        when(userService.login(any(LoginDto.class))).thenReturn(loginResult);

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.token").value("mocked_token_12345"));

        verify(userService, times(1)).login(any(LoginDto.class));
    }

    @Test
    @DisplayName("GET /api/users/check-phone/{phone} - 检查手机号可用性")
    void checkPhone_available() throws Exception {
        when(userService.findByPhone("13800138000")).thenReturn(null);

        mockMvc.perform(get("/api/users/check-phone/13800138000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(true));

        verify(userService, times(1)).findByPhone("13800138000");
    }

    @Test
    @DisplayName("GET /api/users/check-phone/{phone} - 手机号已被使用")
    void checkPhone_notAvailable() throws Exception {
        when(userService.findByPhone("13800138000")).thenReturn(testUser);

        mockMvc.perform(get("/api/users/check-phone/13800138000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(false));

        verify(userService, times(1)).findByPhone("13800138000");
    }

    @Test
    @DisplayName("POST /api/users/{id}/deposit - 增加押金")
    void addDeposit() throws Exception {
        testUser.setDeposit(100.0);
        when(userService.addDeposit(eq(1L), eq(100.0))).thenReturn(testUser);

        Map<String, Double> body = new HashMap<>();
        body.put("amount", 100.0);

        mockMvc.perform(post("/api/users/1/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.deposit").value(100.0));

        verify(userService, times(1)).addDeposit(eq(1L), eq(100.0));
    }

    @Test
    @DisplayName("POST /api/users/{id}/rating - 更新评分")
    void updateRating() throws Exception {
        testUser.setCreditScore(50);
        when(userService.updateRating(eq(1L), eq(5))).thenReturn(testUser);

        Map<String, Integer> body = new HashMap<>();
        body.put("rating", 5);

        mockMvc.perform(post("/api/users/1/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.creditScore").value(50));

        verify(userService, times(1)).updateRating(eq(1L), eq(5));
    }
}
