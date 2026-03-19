# 顺手送 - 项目部署说明

## 📦 项目状态

✅ **后端服务已启动** - 运行在端口 4000
✅ **数据库已初始化** - SQLite (shunshousong.db)
✅ **API 正常运行** - http://localhost:4000/api

## 🌐 访问链接

### 方案一：LocalTunnel（推荐，无需配置）

**注意**：首次访问需要在浏览器中确认

| 服务 | 链接 | 说明 |
|------|------|------|
| API 后端 | https://shunshousong-api.loca.lt | 后端 API 服务 |
| 管理后台 | https://shunshousong-admin.loca.lt | 管理界面 |
| 用户端 | https://shunshousong-user.loca.lt | 用户界面 |

**使用步骤**：
1. 在浏览器中打开上述链接
2. 点击 "Click to Continue" 确认访问
3. 之后即可正常访问 API

### 方案二：公网 IP 直连（需开放端口）

服务器公网 IP: `8.152.234.182`

```
http://8.152.234.182:4000/api/orders
```

如无法访问，需在阿里云安全组开放 4000 端口。

### 方案三：Nginx 反向代理（生产环境推荐）

```bash
# 安装 nginx
sudo apt install nginx -y

# 创建配置文件
sudo nano /etc/nginx/sites-available/shunshousong

# 配置内容：
server {
    listen 80;
    server_name shunshousong.example.com;  # 替换为你的域名
    
    location /api {
        proxy_pass http://localhost:4000;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_cache_bypass $http_upgrade;
    }
    
    location / {
        root /home/admin/openclaw/workspace/shunshousong;
        try_files $uri $uri/ /index.html;
    }
}

# 启用配置
sudo ln -s /etc/nginx/sites-available/shunshousong /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

## 🚀 启动服务

### 后端服务
```bash
cd ~/openclaw/workspace/shunshousong/backend
npm run start:prod
```

### 前端服务（开发）
```bash
cd ~/openclaw/workspace/shunshousong
# 直接使用 index.html 或
npx serve .
```

### LocalTunnel 隧道
```bash
# API 隧道
npx localtunnel --port 4000 --subdomain shunshousong-api

# 管理后台隧道
npx localtunnel --port 9000 --subdomain shunshousong-admin

# 用户端隧道
npx localtunnel --port 8888 --subdomain shunshousong-user
```

## 📊 API 端点

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/orders | 获取所有订单 |
| GET | /api/orders/:id | 获取订单详情 |
| POST | /api/orders | 创建订单 |
| POST | /api/orders/:id/accept | 接受订单 |
| POST | /api/orders/:id/pick | 取货 |
| POST | /api/orders/:id/complete | 完成订单 |
| POST | /api/orders/:id/cancel | 取消订单 |
| GET | /api/users | 获取用户列表 |
| GET | /api/users/:id | 获取用户详情 |
| POST | /api/users | 创建用户 |
| GET | /api/payments/:userId | 获取用户支付记录 |
| POST | /api/payments | 创建支付记录 |

## 🧪 快速测试

```bash
# 测试 API
curl http://localhost:4000/api/orders

# 创建测试订单
curl -X POST http://localhost:4000/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "publisherId": 1,
    "type": "deliver",
    "pickupAddress": "测试地址 A",
    "deliveryAddress": "测试地址 B",
    "description": "测试订单",
    "reward": 10
  }'
```

## 📝 待办事项

- [ ] 配置域名和 SSL 证书
- [ ] 设置 PM2 进程管理
- [ ] 配置数据库备份
- [ ] 添加用户认证（微信登录）
- [ ] 完善前端页面

---

**创建时间**: 2026-03-16
**服务器**: 阿里云 (8.152.234.182)
