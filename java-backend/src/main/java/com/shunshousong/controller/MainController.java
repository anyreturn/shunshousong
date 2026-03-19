package com.shunshousong.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class MainController {
    
    @GetMapping("/")
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("👋 欢迎来到顺手送 API 服务！\n\n" +
                "📚 API 端点:\n" +
                "  GET  /api/orders      - 获取订单列表\n" +
                "  GET  /api/orders/:id  - 获取订单详情\n" +
                "  POST /api/orders      - 创建订单\n" +
                "  POST /api/orders/:id/accept - 接单\n" +
                "  POST /api/orders/:id/complete - 完成订单\n" +
                "  GET  /api/users/:id   - 获取用户信息\n" +
                "  POST /api/users       - 创建用户\n" +
                "  GET  /health          - 健康检查");
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("timestamp", Instant.now().toString());
        response.put("service", "顺手送 Java 后端");
        return ResponseEntity.ok(response);
    }
}
