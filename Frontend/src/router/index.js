import { createRouter, createWebHistory } from 'vue-router'

// === Views ===
import Landing from '@/views/Landing.vue'
import Login from '@/views/Login.vue'
import RegisterStudent from '@/views/RegisterStudent.vue'
import RegisterCompany from '@/views/RegisterCompany.vue'
import DashboardStudent from '@/views/DashboardStudent.vue'
import DashboardCompany from '@/views/DashboardCompany.vue'
import DashboardGuarantor from '@/views/DashboardGuarantor.vue'
import axios from '../api.js'

// === Routes ===
const routes = [
  { path: '/', name: 'Landing', component: Landing },
  { path: '/login', name: 'Login', component: Login },
  { path: '/register/student', name: 'RegisterStudent', component: RegisterStudent },
  { path: '/register/company', name: 'RegisterCompany', component: RegisterCompany },
  { path: '/internship/add', name: 'AddInternship', component: () => import('@/views/AddInternship.vue')},
  { path: '/forgot-password', name: 'ForgotPassword', component: () => import('@/views/ForgotPassword.vue')},
  { path: '/change-password', name: 'ChangePassword', component: () => import('@/views/ChangePassword.vue')},
  { path: '/enter-temp-password', name: 'EnterTempPassword', component: () => import('@/views/EnterTempPassword.vue') },
  { path: '/create-new-password', name: 'CreatePassword', component: () => import('@/views/CreateNewPassword.vue') },
  { path: '/guarantor/companies', name: 'GuarantorCompanies', component: () => import('@/views/GuarantorCompanies.vue') },


  // === Dashboards (private routes) ===
  {
    path: '/dashboard/student',
    name: 'DashboardStudent',
    component: DashboardStudent,
    meta: { requiresAuth: true, role: 'student' },
  },
  {
    path: '/dashboard/company',
    name: 'DashboardCompany',
    component: DashboardCompany,
    meta: { requiresAuth: true, role: 'company' },
  },
  {
    path: '/dashboard/guarantor',
    name: 'DashboardGuarantor',
    component: DashboardGuarantor,
    meta: { requiresAuth: true, role: 'guarantor' },
  },
]

// === Router ===
export const router = createRouter({
  history: createWebHistory(),
  routes,
})

// === Navigation Guards ===
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const role = localStorage.getItem('role')
  const mustChangePwd = localStorage.getItem('mustChangePwd') === 'true'
  
  if (mustChangePwd && to.path !== '/change-password') {
    return next('/change-password')
  }

  if (to.meta.requiresAuth) {
    if (!token) {
      return next('/login')
    }
    if (!role) {
      localStorage.clear()
      return next('/login')
    }

    if (to.meta.role && to.meta.role !== role) {
      return next('/')
    }
  }

  if (
    (to.path === '/login' || to.path.startsWith('/register')) &&
    token
  ) {
    return next(`/dashboard/${role}`)
  }

  next()
})


export default router
