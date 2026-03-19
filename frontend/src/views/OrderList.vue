<template>
  <div class="order-list">
    <div class="page-header">
      <van-icon name="arrow-left" @click="$router.back()" />
      <span>接单大厅</span>
    </div>

    <!-- 筛选条件 -->
    <div class="filters">
      <van-dropdown-menu>
        <van-dropdown-item v-model="filterType" :options="typeOptions" title="类型" />
        <van-dropdown-item v-model="filterDistance" :options="distanceOptions" title="距离" />
        <van-dropdown-item v-model="filterReward" :options="rewardOptions" title="报酬" />
      </van-dropdown-menu>
    </div>

    <!-- 订单列表 -->
    <div class="orders">
      <div class="order-card" v-for="order in orders" :key="order.id" @click="$router.push(`/order/${order.id}`)">
        <div class="order-header">
          <span class="type">{{ order.type === 'deliver' ? '📦 帮送' : '📥 帮取' }}</span>
          <span class="time">{{ order.time }}</span>
        </div>
        <div class="order-route">
          <div class="route-item">
            <van-icon name="location-o" />
            <span>{{ order.from }}</span>
          </div>
          <div class="route-arrow">↓</div>
          <div class="route-item">
            <van-icon name="location-o" />
            <span>{{ order.to }}</span>
          </div>
        </div>
        <div class="order-footer">
          <span class="reward">¥{{ order.reward }}</span>
          <span class="distance">{{ order.distance }}</span>
          <span class="status">{{ order.status }}</span>
        </div>
      </div>
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

const activeTab = ref('orders')
const filterType = ref(0)
const filterDistance = ref(0)
const filterReward = ref(0)

const typeOptions = [
  { text: '全部', value: 0 },
  { text: '帮我送', value: 1 },
  { text: '帮我取', value: 2 }
]

const distanceOptions = [
  { text: '全部', value: 0 },
  { text: '1km 内', value: 1 },
  { text: '3km 内', value: 2 },
  { text: '5km 内', value: 3 }
]

const rewardOptions = [
  { text: '全部', value: 0 },
  { text: '¥5 以下', value: 1 },
  { text: '¥5-10', value: 2 },
  { text: '¥10 以上', value: 3 }
]

const orders = ref([
  { id: 1, type: 'pickup', from: '阳光花园 3 栋', to: '5 栋', reward: 5, distance: '500m', time: '10 分钟前', status: '待接单' },
  { id: 2, type: 'deliver', from: '阳光花园', to: '科技园', reward: 15, distance: '3km', time: '20 分钟前', status: '待接单' },
  { id: 3, type: 'pickup', from: '星巴克', to: '阳光花园', reward: 8, distance: '1km', time: '半小时前', status: '待接单' },
  { id: 4, type: 'deliver', from: '菜鸟驿站', to: '阳光花园 8 栋', reward: 6, distance: '800m', time: '1 小时前', status: '待接单' }
])
</script>

<style scoped>
.order-list {
  padding-bottom: 60px;
}
.page-header {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  font-size: 18px;
  font-weight: 600;
  border-bottom: 1px solid #eee;
}
.filters {
  position: sticky;
  top: 0;
  z-index: 100;
  background: white;
}
.orders {
  padding: 16px;
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
  margin-bottom: 12px;
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
  margin-bottom: 12px;
}
.route-item {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #666;
  font-size: 14px;
}
.route-arrow {
  text-align: center;
  color: #999;
  margin: 4px 0;
}
.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.reward {
  color: #ff6b6b;
  font-weight: 600;
  font-size: 18px;
}
.distance, .status {
  color: #999;
  font-size: 12px;
}
</style>
