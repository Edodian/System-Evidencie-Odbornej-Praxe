import { createRouter, createWebHistory } from 'vue-router'
import Landing from '@/views/Landing.vue'
import Login from '@/views/Login.vue'
import RegisterStudent from '@/views/RegisterStudent.vue'
import RegisterCompany from '@/views/RegisterCompany.vue'
import Dashboard from '@/views/Dashboard.vue'

const routes = [
  { path: '/', name: 'Landing', component: Landing },
  { path: '/login', name: 'Login', component: Login },
  { path: '/register/student', name: 'RegisterStudent', component: RegisterStudent },
  { path: '/register/company', name: 'RegisterCompany', component: RegisterCompany },
  { path: '/dashboard', name: 'Dashboard', component: Dashboard },
]

export const router = createRouter({
  history: createWebHistory(),
  routes,
})
