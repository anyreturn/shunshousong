<template>
  <div class="publish">
    <div class="page-header">
      <van-icon name="arrow-left" @click="$router.back()" />
      <span>发布配送需求</span>
    </div>

    <div class="form">
      <!-- 订单类型 -->
      <div class="form-section">
        <div class="section-title">订单类型</div>
        <div class="type-selector">
          <div class="type-option" :class="{ active: orderType === 'deliver' }" @click="orderType = 'deliver'">
            <span class="icon">📦</span>
            <span>帮我送</span>
          </div>
          <div class="type-option" :class="{ active: orderType === 'pickup' }" @click="orderType = 'pickup'">
            <span class="icon">📥</span>
            <span>帮我取</span>
          </div>
        </div>
      </div>

      <!-- 取件地址 -->
      <div class="form-section">
        <div class="section-title">取件地址 🗺️</div>
        <van-field v-model="pickupAddress" placeholder="请输入取件地址" />
      </div>

      <!-- 收件地址 -->
      <div class="form-section">
        <div class="section-title">收件地址 🗺️</div>
        <van-field v-model="deliveryAddress" placeholder="请输入收件地址" />
      </div>

      <!-- 物品描述 -->
      <div class="form-section">
        <div class="section-title">物品描述</div>
        <van-field
          v-model="description"
          type="textarea"
          rows="3"
          placeholder="请描述物品及注意事项"
        />
      </div>

      <!-- 期望时间 -->
      <div class="form-section">
        <div class="section-title">期望时间</div>
        <van-radio-group v-model="expectedTime" direction="horizontal">
          <van-radio name="now">立即</van-radio>
          <van-radio name="30min">30 分钟内</van-radio>
          <van-radio name="1hour">1 小时内</van-radio>
        </van-radio-group>
      </div>

      <!-- 报酬设置 -->
      <div class="form-section">
        <div class="section-title">报酬设置</div>
        <div class="reward-slider">
          <span>¥3</span>
          <van-slider v-model="reward" :min="3" :max="50" />
          <span>¥50</span>
        </div>
        <div class="reward-display">
          建议 ¥5-15 · 当前 ¥{{ reward }}
        </div>
      </div>

      <!-- 提交按钮 -->
      <div class="submit-section">
        <van-button type="primary" block round @click="submitOrder">
          支付 ¥{{ reward }}.00 并发布
        </van-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const orderType = ref('deliver')
const pickupAddress = ref('')
const deliveryAddress = ref('')
const description = ref('')
const expectedTime = ref('now')
const reward = ref(8)

const submitOrder = () => {
  // TODO: 调用 API 提交订单
  console.log('提交订单:', {
    type: orderType.value,
    pickup: pickupAddress.value,
    delivery: deliveryAddress.value,
    description: description.value,
    expectedTime: expectedTime.value,
    reward: reward.value
  })
}
</script>

<style scoped>
.publish {
  padding-bottom: 24px;
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
.form {
  padding: 16px;
}
.form-section {
  margin-bottom: 24px;
}
.section-title {
  font-size: 14px;
  font-weight: 600;
  color: #666;
  margin-bottom: 12px;
}
.type-selector {
  display: flex;
  gap: 12px;
}
.type-option {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  border: 2px solid #eee;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s;
}
.type-option.active {
  border-color: #667eea;
  background: linear-gradient(135deg, rgba(102,126,234,0.1) 0%, rgba(118,75,162,0.1) 100%);
}
.icon {
  font-size: 32px;
  margin-bottom: 8px;
}
.reward-slider {
  display: flex;
  align-items: center;
  gap: 12px;
}
.reward-display {
  text-align: center;
  color: #999;
  font-size: 12px;
  margin-top: 8px;
}
.submit-section {
  margin-top: 32px;
}
</style>
