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
  { path: '/internship/add', name: 'AddInternship', component: () => import('@/views/AddInternship.vue')},
  { path: '/forgot-password', name: 'ForgotPassword', component: () => import('@/views/ForgotPassword.vue')},
  { path: '/change-password', name: 'ChangePassword', component: () => import('@/views/ChangePassword.vue')},
  { path: '/enter-temp-password', name: 'EnterTempPassword', component: () => import('@/views/EnterTempPassword.vue') },
  { path: '/create-new-password', name: 'CreatePassword', component: () => import('@/views/CreateNewPassword.vue') },


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
  // 🚧 === ВРЕМЕННОЕ РЕШЕНИЕ, ПОТОМ ПОДМЕНИМ НА БЭК ===
  // Когда будет бекенд — заменим это на реальную JWT-проверку (через API).
  const token = localStorage.getItem('token')  // <-- заменить на проверку валидности токена через сервер
  const role = localStorage.getItem('role')    // <-- заменить на роль из backend payload / user API

  // === Проверка приватных роутов ===
  if (to.meta.requiresAuth) {
    if (!token) {
      // ❌ Нет токена — перенаправляем на логин
      return next('/login')
    }

    // 🔒 Проверяем роль
    if (to.meta.role && to.meta.role !== role) {
      // ❌ Не та роль — отправляем на главную
      return next('/')
    }
  }

  // 🚫 Если пользователь уже вошёл — не пускаем на логин/регистрацию
  if (
    (to.path === '/login' || to.path.startsWith('/register')) &&
    token
  ) {
    return next(`/dashboard/${role}`)
  }

  next()
})

export default router
