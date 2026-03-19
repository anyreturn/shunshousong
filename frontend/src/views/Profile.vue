<template>
  <div class="profile">
    <div class="header">
      <van-image round src="https://fastly.jsdelivr.net/npm/@vant/assets/cat.jpeg" class="avatar" />
      <div class="user-info">
        <div class="nickname">用户 A62C</div>
        <div class="phone">138****1234</div>
      </div>
    </div>

    <div class="stats">
      <div class="stat-item">
        <div class="value">12</div>
        <div class="label">完成订单</div>
      </div>
      <div class="stat-item">
        <div class="value">¥156</div>
        <div class="label">累计收入</div>
      </div>
      <div class="stat-item">
        <div class="value">5.0</div>
        <div class="label">信用评分</div>
      </div>
    </div>

    <div class="menu-list">
      <van-cell-group inset>
        <van-cell title="我的订单" is-link @click="$router.push('/orders')" />
        <van-cell title="我的钱包" is-link>
          <template #right-icon>
            <span class="balance">余额 ¥0.00</span>
          </template>
        </van-cell>
        <van-cell title="保证金" is-link>
          <template #right-icon>
            <span class="deposit">已缴纳 ¥99</span>
          </template>
        </van-cell>
        <van-cell title="实名认证" is-link>
          <template #right-icon>
            <van-tag type="success">已认证</van-tag>
          </template>
        </van-cell>
      </van-cell-group>

      <van-cell-group inset>
        <van-cell title="客服中心" is-link icon="service-o" />
        <van-cell title="关于我们" is-link icon="info-o" />
        <van-cell title="设置" is-link icon="setting-o" />
      </van-cell-group>
    </div>

    <!-- 底部导航 -->
    <van-tabbar v-model="activeTab">
      <van-tabbar-item icon="home-o" @click="$router.push('/')">首页</van-tabbar-item>
      <van-tabbar-item icon="add" @click="$router.push('/publish')">发单</van-tabbar-item>
      <van-tabbar-item icon="orders-o" @click="$router.push('/orders')">接单</van-tabbar-item>
      <van-tabbar-item icon="user-o" @click="$router.push('/profile')">我的</van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const activeTab = ref('profile')
</script>

<style scoped>
.profile {
  padding-bottom: 60px;
  background: #f5f5f5;
  min-height: 100vh;
}
.header {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 32px 16px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}
.avatar {
  width: 70px;
  height: 70px;
  border: 3px solid rgba(255,255,255,0.3);
}
.user-info {
  flex: 1;
}
.nickname {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 4px;
}
.phone {
  font-size: 14px;
  opacity: 0.9;
}
.stats {
  display: flex;
  justify-content: space-around;
  padding: 24px 16px;
  background: white;
  margin: -16px 16px 16px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}
.stat-item {
  text-align: center;
}
.value {
  font-size: 24px;
  font-weight: 600;
  color: #667eea;
  margin-bottom: 4px;
}
.label {
  font-size: 12px;
  color: #999;
}
.menu-list {
  padding: 0 16px;
}
.balance, .deposit {
  color: #999;
  font-size: 14px;
}
</style>
