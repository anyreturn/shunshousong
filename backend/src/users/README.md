# 用户模块 API 文档

## 概述

用户模块提供用户注册、登录、信息管理等功能。

**技术栈：**
- NestJS (后端框架)
- TypeORM (数据库 ORM)
- bcryptjs (密码加密)
- class-validator (数据验证)

---

## API 端点

### 1. 用户注册

**POST** `/api/users/register`

**请求体：**
```json
{
  "phone": "13800138000",
  "password": "password123",
  "nickname": "用户名",
  "avatar": "https://example.com/avatar.jpg",
  "isAgreementAccepted": true
}
```

**验证规则：**
- `phone`: 必填，中国大陆手机号格式 (1 开头，11 位)
- `password`: 必填，最少 6 位
- `nickname`: 必填，最少 2 位
- `avatar`: 可选，头像 URL
- `isAgreementAccepted`: 必填，布尔值，是否同意用户协议

**成功响应 (201 Created)：**
```json
{
  "id": 1,
  "phone": "13800138000",
  "nickname": "用户名",
  "avatar": "https://example.com/avatar.jpg",
  "openid": null,
  "email": null,
  "realName": null,
  "idCard": null,
  "deposit": 0,
  "balance": 0,
  "creditScore": 100,
  "isVerified": false,
  "completedOrders": 0,
  "totalEarnings": 0,
  "createdAt": "2026-03-17T00:00:00.000Z",
  "updatedAt": "2026-03-17T00:00:00.000Z"
}
```

**错误响应：**

409 Conflict - 手机号已注册
```json
{
  "statusCode": 409,
  "message": "该手机号已注册",
  "error": "Conflict"
}
```

400 Bad Request - 未同意协议
```json
{
  "statusCode": 400,
  "message": "请同意用户协议",
  "error": "Bad Request"
}
```

400 Bad Request - 验证失败
```json
{
  "statusCode": 400,
  "message": [
    "手机号格式不正确",
    "密码至少需要 6 位"
  ],
  "error": "Bad Request"
}
```

---

### 2. 用户登录

**POST** `/api/users/login`

**请求体：**
```json
{
  "phone": "13800138000",
  "password": "password123"
}
```

**成功响应 (200 OK)：**
```json
{
  "user": {
    "id": 1,
    "phone": "13800138000",
    "nickname": "用户名",
    "avatar": "https://example.com/avatar.jpg",
    "creditScore": 100,
    "isVerified": false
  },
  "token": "eyJpZCI6MSwicGhvbmUiOiIxMzgwMDEzODAwMCIsImV4cCI6MTcxMDc2ODAwMDAwMH0="
}
```

**错误响应：**

404 Not Found - 用户不存在
```json
{
  "statusCode": 404,
  "message": "用户不存在",
  "error": "Not Found"
}
```

400 Bad Request - 密码错误
```json
{
  "statusCode": 400,
  "message": "密码错误",
  "error": "Bad Request"
}
```

---

### 3. 检查手机号可用性

**GET** `/api/users/check-phone/:phone`

**请求参数：**
- `phone`: 手机号

**成功响应 (200 OK)：**

手机号可用
```json
{
  "available": true
}
```

手机号已被注册
```json
{
  "available": false
}
```

---

### 4. 获取用户信息

**GET** `/api/users/:id`

**请求参数：**
- `id`: 用户 ID

**成功响应 (200 OK)：**
```json
{
  "id": 1,
  "phone": "13800138000",
  "nickname": "用户名",
  "avatar": "https://example.com/avatar.jpg",
  "openid": null,
  "email": null,
  "realName": null,
  "idCard": null,
  "deposit": 0,
  "balance": 0,
  "creditScore": 100,
  "isVerified": false,
  "completedOrders": 0,
  "totalEarnings": 0,
  "createdAt": "2026-03-17T00:00:00.000Z",
  "updatedAt": "2026-03-17T00:00:00.000Z"
}
```

---

### 5. 获取所有用户

**GET** `/api/users`

**成功响应 (200 OK)：**
```json
[
  {
    "id": 1,
    "phone": "13800138000",
    "nickname": "用户名",
    ...
  },
  {
    "id": 2,
    "phone": "13900139000",
    "nickname": "用户 2",
    ...
  }
]
```

**注意：** 最多返回最近 50 个用户

---

## 数据模型

### User 实体

| 字段 | 类型 | 说明 |
|------|------|------|
| id | number | 用户 ID (主键) |
| openid | string | 微信 openid (可选) |
| phone | string | 手机号 (唯一) |
| email | string | 邮箱 (可选) |
| password | string | 密码 (加密存储) |
| nickname | string | 昵称 |
| avatar | string | 头像 URL |
| realName | string | 真实姓名 (可选) |
| idCard | string | 身份证号 (可选) |
| deposit | decimal | 押金 |
| balance | decimal | 余额 |
| creditScore | number | 信用分 (默认 100) |
| isVerified | boolean | 是否实名认证 |
| completedOrders | number | 完成订单数 |
| totalEarnings | number | 总收入 |
| createdAt | Date | 创建时间 |
| updatedAt | Date | 更新时间 |

---

## 安全说明

### 密码加密

- 使用 bcryptjs 进行密码加密
- 加密强度：10 轮 salt
- 密码在保存到数据库前自动加密
- 登录时使用 bcrypt.compare 验证密码

### Token 说明

当前实现使用简单的 Base64 编码 token：
```javascript
const token = Buffer.from(JSON.stringify({
  id: user.id,
  phone: user.phone,
  exp: Date.now() + 7 * 24 * 60 * 60 * 1000, // 7 天
})).toString('base64');
```

**⚠️ 注意：** 生产环境建议使用 JWT (jsonwebtoken) 进行更安全的 token 管理。

---

## 使用示例

### 注册流程

```javascript
// 1. 检查手机号是否可用
const checkRes = await fetch('/api/users/check-phone/13800138000');
const { available } = await checkRes.json();

if (!available) {
  console.log('手机号已被注册');
  return;
}

// 2. 注册用户
const registerRes = await fetch('/api/users/register', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    phone: '13800138000',
    password: 'password123',
    nickname: '我的昵称',
    isAgreementAccepted: true,
  }),
});

const user = await registerRes.json();
console.log('注册成功:', user);
```

### 登录流程

```javascript
const loginRes = await fetch('/api/users/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    phone: '13800138000',
    password: 'password123',
  }),
});

const { user, token } = await loginRes.json();
console.log('登录成功');
console.log('用户:', user);
console.log('Token:', token);

// 后续请求携带 token
const profileRes = await fetch(`/api/users/${user.id}`, {
  headers: { 'Authorization': `Bearer ${token}` },
});
```

---

## 测试

运行单元测试：

```bash
cd backend
npm test -- users.service.spec.ts
```

---

## TODO

- [ ] 实现 JWT token 认证
- [ ] 添加邮箱注册支持
- [ ] 添加短信验证码
- [ ] 添加密码重置功能
- [ ] 添加用户头像上传
- [ ] 添加账号注销功能
