package com.shunshousong.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 应用控制器
 * 
 * <p>提供应用基本信息和健康检查接口</p>
 * 
 * <p>接口列表：</p>
 * <ul>
 *     <li>GET /api - 获取应用信息</li>
 * </ul>
 * 
 * @author shunshousong team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api")
public class AppController {

    /**
     * 获取应用信息
     * 
     * GET /api
     * 
     * @return 应用信息 { name, version, status }
     */
    @GetMapping
    public Map<String, String> getApiInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("name", "顺手送 API");
        info.put("version", "1.0.0");
        info.put("status", "running");
        return info;
    }
}
