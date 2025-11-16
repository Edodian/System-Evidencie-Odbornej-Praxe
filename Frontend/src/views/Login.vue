<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50 px-4">
    <div
      class="w-full max-w-md bg-white rounded-2xl shadow-lg p-8 border border-gray-100 transition-all duration-300 hover:shadow-xl"
    >
      <!-- Back Arrow Box -->
      <div 
        class="absolute top-6 left-6 w-16 h-16 flex items-center justify-center bg-indigo-50 rounded-lg cursor-pointer hover:bg-indigo-100"
        @click="goBack"
      >
        <span class="text-indigo-600 text-5xl font-bold">←</span>
      </div>

      <h1 class="text-3xl font-bold text-center text-indigo-600 mb-6">Login</h1>

      <form @submit.prevent="login" class="space-y-5">
        <!-- Email -->
        <div>
          <label class="block text-gray-700 mb-2">Email</label>
          <input
            type="email"
            v-model="email"
            placeholder="you@example.com"
            class="w-full border border-gray-300 rounded-lg px-3 py-2 focus:ring-indigo-500 focus:border-indigo-500"
            required
          />
        </div>

        <!-- Password -->
        <div>
          <label class="block text-gray-700 mb-2">Password</label>
          <input
            type="password"
            v-model="password"
            placeholder="••••••••"
            class="w-full border border-gray-300 rounded-lg px-3 py-2 focus:ring-indigo-500 focus:border-indigo-500"
            required
          />
        </div>

        <!-- Submit -->
        <button
          type="submit"
          class="w-full bg-indigo-600 text-white font-medium py-2 rounded-lg hover:bg-indigo-700 transition-all"
          :disabled="loading"
        >
          {{ loading ? 'Logging in...' : 'Log In' }}
        </button>
      </form>

      <!-- Forgot password -->
      <p class="text-center mt-4">
        <router-link to="/forgot-password" class="text-indigo-600 hover:underline text-sm">
          Forgot your password?
        </router-link>
      </p>

      <!-- Error -->
      <p v-if="error" class="text-red-500 text-sm mt-4 text-center">{{ error }}</p>

      <div class="text-center mt-6 text-gray-500 text-sm">
        Don’t have an account?
        <router-link to="/register/student" class="text-indigo-600 hover:underline">
          Register
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from '../api.js'

const router = useRouter()
const email = ref('')
const password = ref('')
const error = ref('')
const loading = ref(false)

const goBack = () => router.push('/')

const login = async () => {
  if (!email.value || !password.value) {
    error.value = 'Please enter email and password'
    return
  }

  loading.value = true
  error.value = ''

  try {
    const res = await axios.post(
      '/api/student/login',
      { email: email.value, password: password.value },
      { withCredentials: true }
    )

    // === Успешный логин ===
    const data = res.data
    localStorage.setItem('email', email.value)
    localStorage.setItem('mustChangePwd', 'false')
    localStorage.setItem('role', 'student')
    localStorage.setItem('token', 'session') // пока без JWT

    router.push('/dashboard/student')
  } catch (e) {
    console.error('Login error:', e)

    const status = e.response?.status
    const backendStatus = e.response?.data?.status

    if (status === 403 && backendStatus === 'PASSWORD_CHANGE_REQUIRED') {
      // Обязательная смена пароля
      localStorage.setItem('email', email.value)
      localStorage.setItem('mustChangePwd', 'true')
      error.value = 'You must change your temporary password first.'
      router.push('/change-password')
      return
    }

    if (status === 401) {
      error.value = 'Invalid email or password.'
      return
    }

    error.value = e.response?.data?.message || 'Login failed. Please try again.'
  } finally {
    loading.value = false
  }
}
</script>
