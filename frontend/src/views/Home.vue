<template>
  <div class="home">
    <!-- 顶部定位 -->
    <div class="header">
      <div class="location">
        <van-icon name="location-o" />
        <span>阳光花园小区</span>
        <van-icon name="arrow" />
      </div>
      <van-icon name="bell" class="notification" />
    </div>

    <!-- 快捷入口 -->
    <div class="quick-actions">
      <div class="action-card" @click="$router.push('/publish')">
        <div class="icon">📦</div>
        <div class="text">发单</div>
        <div class="sub">帮我送</div>
      </div>
      <div class="action-card" @click="$router.push('/orders')">
        <div class="icon">🙋</div>
        <div class="text">接单</div>
        <div class="sub">赚零花</div>
      </div>
    </div>

    <!-- 附近订单 -->
    <div class="orders-section">
      <div class="section-header">
        <span>附近订单</span>
        <span class="more">更多 ></span>
      </div>
      
      <div class="order-list">
        <div class="order-card" v-for="order in orders" :key="order.id" @click="$router.push(`/order/${order.id}`)">
          <div class="order-header">
            <span class="type">{{ order.type === 'deliver' ? '📦 帮送' : '📥 帮取' }}</span>
            <span class="time">{{ order.time }}</span>
          </div>
          <div class="order-route">
            {{ order.from }} → {{ order.to }}
          </div>
          <div class="order-footer">
            <span class="reward">报酬 ¥{{ order.reward }}</span>
            <span class="distance">{{ order.distance }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部导航 -->
    <van-tabbar v-model="activeTab">
      <van-tabbar-item icon="home-o" name="home" @click="$router.push('/')">首页</van-tabbar-item>
      <van-tabbar-item icon="add" name="publish" @click="$router.push('/publish')">发单</van-tabbar-item>
      <van-tabbar-item icon="orders-o" name="orders" @click="$router.push('/orders')">接单</van-tabbar-item>
      <van-tabbar-item icon="user-o" name="profile" @click="$router.push('/profile')">我的</van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const activeTab = ref('home')

const orders = ref([
  { id: 1, type: 'pickup', from: '阳光花园 3 栋', to: '5 栋', reward: 5, distance: '500m', time: '10 分钟前' },
  { id: 2, type: 'deliver', from: '阳光花园', to: '科技园', reward: 15, distance: '3km', time: '20 分钟前' },
  { id: 3, type: 'pickup', from: '星巴克', to: '阳光花园', reward: 8, distance: '1km', time: '半小时前' }
])
</script>

<style scoped>
.home {
  padding-bottom: 60px;
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}
.location {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 16px;
}
.notification {
  font-size: 20px;
}
.quick-actions {
  display: flex;
  justify-content: space-around;
  padding: 24px 16px;
  background: white;
}
.action-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}
.icon {
  font-size: 40px;
}
.text {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}
.sub {
  font-size: 12px;
  color: #999;
}
.orders-section {
  padding: 16px;
}
.section-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12px;
  font-size: 16px;
  font-weight: 600;
}
.more {
  color: #667eea;
  font-size: 14px;
}
.order-card {
  background: white;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}
.order-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}
.type {
  font-weight: 600;
  color: #333;
}
.time {
  color: #999;
  font-size: 12px;
}
.order-route {
  color: #666;
  margin-bottom: 12px;
  font-size: 14px;
}
.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.reward {
  color: #ff6b6b;
  font-weight: 600;
  font-size: 16px;
}
.distance {
  color: #999;
  font-size: 12px;
}
</style>
