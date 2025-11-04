import { createRouter, createWebHistory } from 'vue-router'

// === Views ===
import Landing from '@/views/Landing.vue'
import Login from '@/views/Login.vue'
import RegisterStudent from '@/views/RegisterStudent.vue'
import RegisterCompany from '@/views/RegisterCompany.vue'
import DashboardStudent from '@/views/DashboardStudent.vue'
import DashboardCompany from '@/views/DashboardCompany.vue'
import DashboardGuarantor from '@/views/DashboardGuarantor.vue'

// === Routes ===
const routes = [
  { path: '/', name: 'Landing', component: Landing },
  { path: '/login', name: 'Login', component: Login },
  { path: '/register/student', name: 'RegisterStudent', component: RegisterStudent },
  { path: '/register/company', name: 'RegisterCompany', component: RegisterCompany },
  {path: '/internship/add', name: 'AddInternship', component: () => import('@/views/AddInternship.vue')
},

  // Dashboards by role
  { path: '/dashboard/student', name: 'DashboardStudent', component: DashboardStudent },
  { path: '/dashboard/company', name: 'DashboardCompany', component: DashboardCompany },
  { path: '/dashboard/guarantor', name: 'DashboardGuarantor', component: DashboardGuarantor },
]

// === Router ===
export const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
