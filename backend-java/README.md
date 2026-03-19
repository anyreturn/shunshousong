# 顺手送 Backend - Spring Boot

顺手送 API 服务的 Spring Boot 实现，从 NestJS 迁移而来。

## 技术栈

- **Java 11+**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **SQLite** (数据库)
- **Lombok** (简化代码)

## 项目结构

```
backend-java/
├── src/main/java/com/shunshousong/
│   ├── ShunshousongApplication.java    # 主启动类
│   ├── config/
│   │   ├── CorsConfig.java             # CORS 配置
│   │   └── GlobalExceptionHandler.java # 全局异常处理
│   ├── controller/
│   │   ├── AppController.java          # API 信息
│   │   ├── UserController.java         # 用户相关接口
│   │   ├── OrderController.java        # 订单相关接口
│   │   └── PaymentController.java      # 支付相关接口
│   ├── dto/
│   │   └── UserDTOs.java               # 用户数据传输对象
│   ├── entity/
│   │   ├── User.java                   # 用户实体
│   │   ├── Order.java                  # 订单实体
│   │   └── Payment.java                # 支付实体
│   ├── repository/
│   │   ├── UserRepository.java         # 用户数据访问层
│   │   ├── OrderRepository.java        # 订单数据访问层
│   │   └── PaymentRepository.java      # 支付数据访问层
│   └── service/
│       ├── UserService.java            # 用户业务逻辑
│       ├── OrderService.java           # 订单业务逻辑
│       └── PaymentService.java         # 支付业务逻辑
├── src/main/resources/
│   └── application.yml                 # 应用配置
└── pom.xml                             # Maven 配置
```

## API 接口

### 用户模块 `/api/users`

- `GET /api/users/:id` - 获取用户信息
- `GET /api/users` - 获取用户列表
- `POST /api/users` - 创建用户
- `POST /api/users/register` - 用户注册
- `POST /api/users/login` - 用户登录
- `GET /api/users/check-phone/:phone` - 检查手机号可用性
- `POST /api/users/:id/deposit` - 充值押金
- `POST /api/users/:id/rating` - 更新评分

### 订单模块 `/api/orders`

- `GET /api/orders` - 获取订单列表 (支持 status, type 过滤)
- `GET /api/orders/:id` - 获取订单详情
- `POST /api/orders` - 创建订单
- `POST /api/orders/:id/accept` - 接受订单
- `POST /api/orders/:id/pick` - 取货
- `POST /api/orders/:id/complete` - 完成订单
- `POST /api/orders/:id/cancel` - 取消订单

### 支付模块 `/api/payments`

- `GET /api/payments/:userId` - 获取用户支付记录
- `POST /api/payments` - 创建支付记录

## 运行方式

### 前置要求

- JDK 11+ (推荐 Java 17)
- Maven 3.8+

### 启动服务

```bash
cd backend-java
./mvnw spring-boot:run
```

或使用已安装的 Maven:

```bash
cd backend-java
mvn spring-boot:run
```

### 构建可执行 JAR

```bash
mvn clean package
java -jar target/backend-1.0.0.jar
```

## 配置说明

默认配置在 `src/main/resources/application.yml`:

- **端口**: 4000
- **数据库**: SQLite (shunshousong.db)
- **JPA DDL**: update (自动更新表结构)

## 与 NestJS 版本的兼容性

- ✅ API 接口完全兼容
- ✅ 数据库结构相同 (SQLite)
- ✅ 密码加密方式相同 (BCrypt)
- ✅ Token 生成逻辑相同
- ✅ 业务逻辑一致

## 注意事项

1. 首次运行会自动创建数据库表
2. 生产环境应将 `spring.jpa.hibernate.ddl-auto` 改为 `validate`
3. Token 生成使用简单 Base64 编码，生产环境建议使用 JWT
