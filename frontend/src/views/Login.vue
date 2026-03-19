<template>
  <div class="login-container">
    <div class="login-card">
      <h1 class="title">用户登录</h1>
      
      <form @submit.prevent="handleLogin" class="login-form">
        <!-- 手机号 -->
        <div class="form-item">
          <label for="phone">手机号</label>
          <input
            id="phone"
            v-model="form.phone"
            type="tel"
            placeholder="请输入手机号"
            maxlength="11"
          />
          <span v-if="errors.phone" class="error">{{ errors.phone }}</span>
        </div>

        <!-- 密码 -->
        <div class="form-item">
          <label for="password">密码</label>
          <input
            id="password"
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
          />
          <span v-if="errors.password" class="error">{{ errors.password }}</span>
        </div>

        <!-- 提交按钮 -->
        <button type="submit" class="submit-btn" :disabled="loading">
          {{ loading ? '登录中...' : '立即登录' }}
        </button>
      </form>

      <!-- 注册链接 -->
      <div class="register-link">
        还没有账号？<router-link to="/register">立即注册</router-link>
      </div>

      <!-- 消息提示 -->
      <div v-if="message" :class="['message', messageType]">
        {{ message }}
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

const API_BASE = import.meta.env.VITE_API_BASE_URL || 'http://localhost:4000/api';

export default {
  name: 'Login',
  data() {
    return {
      form: {
        phone: '',
        password: '',
      },
      errors: {},
      loading: false,
      message: '',
      messageType: 'success',
    };
  },
  methods: {
    validateForm() {
      this.errors = {};

      const phoneRegex = /^1[3-9]\d{9}$/;
      if (!this.form.phone) {
        this.errors.phone = '请输入手机号';
      } else if (!phoneRegex.test(this.form.phone)) {
        this.errors.phone = '手机号格式不正确';
      }

      if (!this.form.password) {
        this.errors.password = '请输入密码';
      }

      return Object.keys(this.errors).length === 0;
    },

    async handleLogin() {
      if (!this.validateForm()) {
        return;
      }

      this.loading = true;
      this.message = '';

      try {
        const res = await axios.post(`${API_BASE}/users/login`, {
          phone: this.form.phone,
          password: this.form.password,
        });

        const { user, token } = res.data;

        // 保存用户信息和 token
        localStorage.setItem('user', JSON.stringify(user));
        localStorage.setItem('token', token);

        this.messageType = 'success';
        this.message = '登录成功！';

        // 跳转到首页
        setTimeout(() => {
          this.$router.push('/');
        }, 1000);
      } catch (error) {
        this.messageType = 'error';
        if (error.response) {
          this.message = error.response.data.message || '登录失败，请重试';
        } else {
          this.message = '网络错误，请检查网络连接';
        }
      } finally {
        this.loading = false;
      }
    },
  },
};
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.login-card {
  background: white;
  border-radius: 16px;
  padding: 40px;
  width: 100%;
  max-width: 400px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.title {
  text-align: center;
  color: #333;
  margin-bottom: 30px;
  font-size: 28px;
  font-weight: 600;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-item label {
  font-size: 14px;
  color: #555;
  font-weight: 500;
}

.form-item input {
  padding: 12px 16px;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  font-size: 15px;
  transition: all 0.3s;
}

.form-item input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.form-item input::placeholder {
  color: #aaa;
}

.error {
  color: #ff4444;
  font-size: 13px;
}

.submit-btn {
  padding: 14px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  margin-top: 10px;
}

.submit-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
}

.submit-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.register-link {
  text-align: center;
  margin-top: 24px;
  color: #666;
  font-size: 14px;
}

.register-link a {
  color: #667eea;
  text-decoration: none;
  font-weight: 600;
}

.register-link a:hover {
  text-decoration: underline;
}

.message {
  margin-top: 20px;
  padding: 12px;
  border-radius: 8px;
  text-align: center;
  font-size: 14px;
}

.message.success {
  background: #d4edda;
  color: #155724;
  border: 1px solid #c3e6cb;
}

.message.error {
  background: #f8d7da;
  color: #721c24;
  border: 1px solid #f5c6cb;
}
</style>
