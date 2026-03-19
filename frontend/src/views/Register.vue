<template>
  <div class="register-container">
    <div class="register-card">
      <h1 class="title">用户注册</h1>
      
      <form @submit.prevent="handleRegister" class="register-form">
        <!-- 手机号 -->
        <div class="form-item">
          <label for="phone">手机号</label>
          <input
            id="phone"
            v-model="form.phone"
            type="tel"
            placeholder="请输入手机号"
            maxlength="11"
            @blur="checkPhoneAvailability"
          />
          <span v-if="errors.phone" class="error">{{ errors.phone }}</span>
          <span v-if="phoneAvailable === false" class="error">该手机号已被注册</span>
        </div>

        <!-- 密码 -->
        <div class="form-item">
          <label for="password">密码</label>
          <input
            id="password"
            v-model="form.password"
            type="password"
            placeholder="至少 6 位密码"
            minlength="6"
          />
          <span v-if="errors.password" class="error">{{ errors.password }}</span>
        </div>

        <!-- 确认密码 -->
        <div class="form-item">
          <label for="confirmPassword">确认密码</label>
          <input
            id="confirmPassword"
            v-model="form.confirmPassword"
            type="password"
            placeholder="请再次输入密码"
          />
          <span v-if="errors.confirmPassword" class="error">{{ errors.confirmPassword }}</span>
        </div>

        <!-- 昵称 -->
        <div class="form-item">
          <label for="nickname">昵称</label>
          <input
            id="nickname"
            v-model="form.nickname"
            type="text"
            placeholder="请输入昵称"
            minlength="2"
          />
          <span v-if="errors.nickname" class="error">{{ errors.nickname }}</span>
        </div>

        <!-- 头像 (可选) -->
        <div class="form-item">
          <label for="avatar">头像 URL (可选)</label>
          <input
            id="avatar"
            v-model="form.avatar"
            type="url"
            placeholder="请输入头像链接"
          />
        </div>

        <!-- 用户协议 -->
        <div class="form-item checkbox-item">
          <label>
            <input
              v-model="form.isAgreementAccepted"
              type="checkbox"
            />
            <span>我已阅读并同意 <a href="/agreement" target="_blank">《用户协议》</a></span>
          </label>
          <span v-if="errors.isAgreementAccepted" class="error">{{ errors.isAgreementAccepted }}</span>
        </div>

        <!-- 提交按钮 -->
        <button type="submit" class="submit-btn" :disabled="loading || !canSubmit">
          {{ loading ? '注册中...' : '立即注册' }}
        </button>
      </form>

      <!-- 登录链接 -->
      <div class="login-link">
        已有账号？<router-link to="/login">立即登录</router-link>
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
  name: 'Register',
  data() {
    return {
      form: {
        phone: '',
        password: '',
        confirmPassword: '',
        nickname: '',
        avatar: '',
        isAgreementAccepted: false,
      },
      errors: {},
      phoneAvailable: null,
      loading: false,
      message: '',
      messageType: 'success',
    };
  },
  computed: {
    canSubmit() {
      return (
        this.form.phone &&
        this.form.password &&
        this.form.nickname &&
        this.form.isAgreementAccepted
      );
    },
  },
  methods: {
    // 验证表单
    validateForm() {
      this.errors = {};

      // 手机号验证
      const phoneRegex = /^1[3-9]\d{9}$/;
      if (!this.form.phone) {
        this.errors.phone = '请输入手机号';
      } else if (!phoneRegex.test(this.form.phone)) {
        this.errors.phone = '手机号格式不正确';
      }

      // 密码验证
      if (!this.form.password) {
        this.errors.password = '请输入密码';
      } else if (this.form.password.length < 6) {
        this.errors.password = '密码至少需要 6 位';
      }

      // 确认密码验证
      if (this.form.password !== this.form.confirmPassword) {
        this.errors.confirmPassword = '两次输入的密码不一致';
      }

      // 昵称验证
      if (!this.form.nickname) {
        this.errors.nickname = '请输入昵称';
      } else if (this.form.nickname.length < 2) {
        this.errors.nickname = '昵称至少需要 2 位';
      }

      // 协议验证
      if (!this.form.isAgreementAccepted) {
        this.errors.isAgreementAccepted = '请同意用户协议';
      }

      // 手机号可用性
      if (this.phoneAvailable === false) {
        this.errors.phone = '该手机号已被注册';
      }

      return Object.keys(this.errors).length === 0;
    },

    // 检查手机号可用性
    async checkPhoneAvailability() {
      const phoneRegex = /^1[3-9]\d{9}$/;
      if (!phoneRegex.test(this.form.phone)) {
        return;
      }

      try {
        const res = await axios.get(`${API_BASE}/users/check-phone/${this.form.phone}`);
        this.phoneAvailable = res.data.available;
      } catch (error) {
        console.error('检查手机号失败:', error);
      }
    },

    // 处理注册
    async handleRegister() {
      if (!this.validateForm()) {
        return;
      }

      this.loading = true;
      this.message = '';

      try {
        const res = await axios.post(`${API_BASE}/users/register`, {
          phone: this.form.phone,
          password: this.form.password,
          nickname: this.form.nickname,
          avatar: this.form.avatar || '',
          isAgreementAccepted: this.form.isAgreementAccepted,
        });

        this.messageType = 'success';
        this.message = '注册成功！即将跳转到登录页面...';
        
        // 注册成功后跳转到登录页
        setTimeout(() => {
          this.$router.push('/login');
        }, 1500);
      } catch (error) {
        this.messageType = 'error';
        if (error.response) {
          this.message = error.response.data.message || '注册失败，请重试';
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
.register-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.register-card {
  background: white;
  border-radius: 16px;
  padding: 40px;
  width: 100%;
  max-width: 420px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.title {
  text-align: center;
  color: #333;
  margin-bottom: 30px;
  font-size: 28px;
  font-weight: 600;
}

.register-form {
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

.checkbox-item label {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  font-size: 14px;
  color: #666;
}

.checkbox-item input[type="checkbox"] {
  width: 18px;
  height: 18px;
  cursor: pointer;
}

.checkbox-item a {
  color: #667eea;
  text-decoration: none;
}

.checkbox-item a:hover {
  text-decoration: underline;
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

.login-link {
  text-align: center;
  margin-top: 24px;
  color: #666;
  font-size: 14px;
}

.login-link a {
  color: #667eea;
  text-decoration: none;
  font-weight: 600;
}

.login-link a:hover {
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
