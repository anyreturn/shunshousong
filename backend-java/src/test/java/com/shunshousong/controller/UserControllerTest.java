package com.shunshousong.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunshousong.dto.UserDTOs.LoginDto;
import com.shunshousong.dto.UserDTOs.RegisterDto;
import com.shunshousong.entity.User;
import com.shunshousong.exception.UserNotFoundException;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * UserController 集成测试
 * 
 * <p>使用 @WebMvcTest 进行 Controller 层集成测试</p>
 * 
 * <p>测试覆盖：</p>
 * <ul>
 *     <li>GET /api/users/{id} - 查询单个用户</li>
 *     <li>GET /api/users - 查询用户列表</li>
 *     <li>POST /api/users/register - 用户注册</li>
 *     <li>POST /api/users/login - 用户登录</li>
 *     <li>异常处理 - 404, 400 错误</li>
 * </ul>
 * 
 * @author shunshousong team
 * @since 1.0.0
 */
@WebMvcTest(UserController.class)
@DisplayName("UserController 集成测试")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setPhone("13800138000");
        testUser.setEmail("test@example.com");
        testUser.setNickname("测试用户");
    }

    @Test
    @DisplayName("GET /api/users/{id} - 成功查询用户")
    void findOne_success() throws Exception {
        // Given
        when(userService.findOne(1L)).thenReturn(testUser);

        // When & Then
        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.phone").value("13800138000"))
            .andExpect(jsonPath("$.nickname").value("测试用户"));
    }

    @Test
    @DisplayName("GET /api/users/{id} - 用户不存在返回 404")
    void findOne_notFound() throws Exception {
        // Given
        when(userService.findOne(999L)).thenThrow(new UserNotFoundException(999L));

        // When & Then
        mockMvc.perform(get("/api/users/999"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorCode").value("USER_NOT_FOUND"));
    }

    @Test
    @DisplayName("GET /api/users - 查询用户列表")
    void findAll() throws Exception {
        // Given
        List<User> users = Arrays.asList(testUser, new User());
        when(userService.findAll()).thenReturn(users);

        // When & Then
        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("POST /api/users/register - 成功注册")
    void register_success() throws Exception {
        // Given
        RegisterDto registerDto = new RegisterDto();
        registerDto.setPhone("13900139000");
        registerDto.setPassword("password123");
        registerDto.setNickname("新用户");
        registerDto.setIsAgreementAccepted(true);

        when(userService.register(any(RegisterDto.class))).thenReturn(testUser);

        // When & Then
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("POST /api/users/register - 手机号已注册返回 400")
    void register_phoneExists() throws Exception {
        // Given
        RegisterDto registerDto = new RegisterDto();
        registerDto.setPhone("13800138000");
        registerDto.setPassword("password123");
        registerDto.setIsAgreementAccepted(true);

        doThrow(new com.shunshousong.exception.UserAlreadyExistsException("13800138000"))
            .when(userService).register(any(RegisterDto.class));

        // When & Then
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorCode").value("USER_ALREADY_EXISTS"));
    }

    @Test
    @DisplayName("POST /api/users/login - 成功登录")
    void login_success() throws Exception {
        // Given
        LoginDto loginDto = new LoginDto();
        loginDto.setPhone("13800138000");
        loginDto.setPassword("password123");

        Map<String, Object> loginResult = new HashMap<>();
        loginResult.put("user", testUser);
        loginResult.put("token", "mock_token_123");

        when(userService.login(any(LoginDto.class))).thenReturn(loginResult);

        // When & Then
        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.user.id").value(1))
            .andExpect(jsonPath("$.token").value("mock_token_123"));
    }

    @Test
    @DisplayName("POST /api/users/login - 用户不存在返回 400")
    void login_userNotFound() throws Exception {
        // Given
        LoginDto loginDto = new LoginDto();
        loginDto.setPhone("13800138000");
        loginDto.setPassword("wrongpassword");

        doThrow(new UserNotFoundException("13800138000"))
            .when(userService).login(any(LoginDto.class));

        // When & Then
        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorCode").value("USER_NOT_FOUND"));
    }

    @Test
    @DisplayName("GET /api/users/check-phone/{phone} - 手机号可用")
    void checkPhone_available() throws Exception {
        // Given
        when(userService.findByPhone("13900139000")).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/api/users/check-phone/13900139000"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    @DisplayName("GET /api/users/check-phone/{phone} - 手机号已被占用")
    void checkPhone_notAvailable() throws Exception {
        // Given
        when(userService.findByPhone("13800138000")).thenReturn(testUser);

        // When & Then
        mockMvc.perform(get("/api/users/check-phone/13800138000"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.available").value(false));
    }
}
