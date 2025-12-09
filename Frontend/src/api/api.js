// src/api.js
import axios from 'axios'
import router from '../router'

const api = axios.create({
  baseURL: 'http://localhost:8081',
})

// Attach token on every request(fill later)
api.interceptors.request.use((config) => {
  const publicPaths = [
    '/api/student/login',
    '/api/student/register',
    '/api/student/reset-password',
    '/api/student/create-password',
    '/api/student/verify-temp-password',
  ]

  const isPublic = publicPaths.some((p) => config.url?.startsWith(p))

  if (!isPublic) {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers = config.headers || {}
      config.headers.Authorization = `Bearer ${token}`
    }
  }

  return config
})

// NEW: global 401/403 handler
api.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status

    if (status === 401 || status === 403) {
      // invalidates tokens
      localStorage.clear()
      // disallows redirects if already on /login
      if (router.currentRoute.value.path !== '/login') {
        router.push('/login')
      }
    }

    return Promise.reject(error)
  }
)

export default api
