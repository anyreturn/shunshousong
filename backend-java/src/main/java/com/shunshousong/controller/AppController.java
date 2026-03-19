package com.shunshousong.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AppController {

    @GetMapping
    public Map<String, String> getApiInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("name", "顺手送 API");
        info.put("version", "1.0.0");
        info.put("status", "running");
        return info;
    }
}
