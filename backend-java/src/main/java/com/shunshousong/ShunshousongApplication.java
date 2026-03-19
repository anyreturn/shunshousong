package com.shunshousong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShunshousongApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShunshousongApplication.class, args);
        System.out.println("🚀 顺手送 API 服务运行在 http://localhost:4000");
        System.out.println("📡 API 文档：http://localhost:4000/api");
    }
}
