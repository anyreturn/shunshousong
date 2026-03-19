<template>
  <div class="order-detail">
    <div class="page-header">
      <van-icon name="arrow-left" @click="$router.back()" />
      <span>订单详情</span>
    </div>

    <div class="content" v-if="order">
      <!-- 订单状态 -->
      <div class="status-card">
        <div class="status">{{ order.status }}</div>
        <div class="time">{{ order.time }}</div>
      </div>

      <!-- 订单信息 -->
      <div class="info-section">
        <div class="section-title">订单信息</div>
        <div class="info-item">
          <span class="label">订单类型</span>
          <span class="value">{{ order.type === 'deliver' ? '帮我送' : '帮我取' }}</span>
        </div>
        <div class="info-item">
          <span class="label">取件地址</span>
          <span class="value">{{ order.from }}</span>
        </div>
        <div class="info-item">
          <span class="label">收件地址</span>
          <span class="value">{{ order.to }}</span>
        </div>
        <div class="info-item">
          <span class="label">物品描述</span>
          <span class="value">{{ order.description }}</span>
        </div>
        <div class="info-item">
          <span class="label">期望时间</span>
          <span class="value">{{ order.expectedTime }}</span>
        </div>
      </div>

      <!-- 报酬信息 -->
      <div class="info-section">
        <div class="section-title">报酬信息</div>
        <div class="reward-info">
          <span class="reward">¥{{ order.reward }}</span>
          <span class="tip">平台服务费 ¥0 (首单免费)</span>
        </div>
      </div>

      <!-- 发单人信息 -->
      <div class="info-section">
        <div class="section-title">发单人</div>
        <div class="user-info">
          <van-image round src="https://fastly.jsdelivr.net/npm/@vant/assets/cat.jpeg" class="avatar" />
          <div class="user-detail">
            <div class="nickname">张先生</div>
            <div class="rating">
              <van-icon name="star" />
              <van-icon name="star" />
              <van-icon name="star" />
              <van-icon name="star" />
              <van-icon name="star" />
              <span class="rating-text">5.0 (12 单)</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 地图 -->
      <div class="map-section">
        <div class="section-title">配送路线</div>
        <div class="map-placeholder">
          <van-icon name="map-marked" />
          <span>地图加载中...</span>
        </div>
      </div>
    </div>

    <!-- 底部操作栏 -->
    <div class="action-bar" v-if="order">
      <van-button plain type="default" @click="contactPublisher">联系发单人</van-button>
      <van-button type="primary" @click="acceptOrder">接单</van-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const order = ref(null)

onMounted(() => {
  // 模拟获取订单详情
  order.value = {
    id: route.params.id,
    status: '待接单',
    time: '10 分钟前发布',
    type: 'pickup',
    from: '阳光花园 3 栋',
    to: '5 栋',
    description: '帮忙取个快递，快递柜取件码会发给你',
    expectedTime: '立即',
    reward: 5
  }
})

const contactPublisher = () => {
  // TODO: 拨打电话或发送消息
  console.log('联系发单人')
}

const acceptOrder = () => {
  // TODO: 调用 API 接单
  console.log('接单')
}
</script>

<style scoped>
.order-detail {
  padding-bottom: 80px;
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
.content {
  padding: 16px;
}
.status-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 24px;
  border-radius: 12px;
  margin-bottom: 16px;
  text-align: center;
}
.status {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 8px;
}
.time {
  font-size: 14px;
  opacity: 0.9;
}
.info-section {
  background: white;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}
.section-title {
  font-size: 14px;
  font-weight: 600;
  color: #666;
  margin-bottom: 12px;
}
.info-item {
  display: flex;
  justify-content: space-between;
  padding: 12px 0;
  border-bottom: 1px solid #f5f5f5;
}
.info-item:last-child {
  border-bottom: none;
}
.label {
  color: #999;
  font-size: 14px;
}
.value {
  color: #333;
  font-size: 14px;
}
.reward-info {
  display: flex;
  align-items: baseline;
  gap: 12px;
}
.reward {
  font-size: 32px;
  font-weight: 600;
  color: #ff6b6b;
}
.tip {
  color: #999;
  font-size: 12px;
}
.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}
.avatar {
  width: 50px;
  height: 50px;
}
.user-detail {
  flex: 1;
}
.nickname {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}
.rating {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #ff976a;
  font-size: 12px;
}
.rating-text {
  color: #999;
  margin-left: 4px;
}
.map-section {
  background: white;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}
.map-placeholder {
  height: 200px;
  background: #f5f5f5;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #999;
}
.action-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  gap: 12px;
  padding: 12px 16px;
  background: white;
  box-shadow: 0 -2px 8px rgba(0,0,0,0.05);
}
.action-bar .van-button {
  flex: 1;
}
</style>
