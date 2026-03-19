import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Publish from '../views/Publish.vue'
import OrderList from '../views/OrderList.vue'
import OrderDetail from '../views/OrderDetail.vue'
import Profile from '../views/Profile.vue'
import Register from '../views/Register.vue'
import Login from '../views/Login.vue'

const routes = [
  { path: '/', name: 'Home', component: Home },
  { path: '/publish', name: 'Publish', component: Publish },
  { path: '/orders', name: 'OrderList', component: OrderList },
  { path: '/order/:id', name: 'OrderDetail', component: OrderDetail },
  { path: '/profile', name: 'Profile', component: Profile },
  { path: '/register', name: 'Register', component: Register },
  { path: '/login', name: 'Login', component: Login }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
