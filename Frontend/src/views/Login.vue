<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50 px-4">
    <div
      class="w-full max-w-md bg-white rounded-2xl shadow-lg p-8 border border-gray-100 transition-all duration-300 hover:shadow-xl"
    >
    <!-- Back Arrow Box в левом верхнем углу -->
    <div 
  class="absolute top-6 left-6 w-16 h-16 flex items-center justify-center bg-indigo-50 rounded-lg cursor-pointer hover:bg-indigo-100"
  @click="goBack"
>
  <span class="text-indigo-600 text-5xl font-bold">←</span>
</div>

      <h1 class="text-3xl font-bold text-center text-indigo-600 mb-6">Login</h1>

      <!-- Email -->
      <div class="mb-4">
        <label class="block text-gray-700 mb-2">Email</label>
        <input
          type="email"
          v-model="email"
          placeholder="you@example.com"
          class="w-full border border-gray-300 rounded-lg px-3 py-2 focus:ring-indigo-500 focus:border-indigo-500"
        />
      </div>

      <!-- Password -->
      <div class="mb-6">
        <label class="block text-gray-700 mb-2">Password</label>
        <input
          type="password"
          v-model="password"
          placeholder="••••••••"
          class="w-full border border-gray-300 rounded-lg px-3 py-2 focus:ring-indigo-500 focus:border-indigo-500"
        />
      </div>

      <!-- Submit -->
      <button
        @click="login"
        class="w-full bg-indigo-600 text-white font-medium py-2 rounded-lg hover:bg-indigo-700 transition-all"
      >
        Log In
      </button>

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

const router = useRouter()
const email = ref('')
const password = ref('')
const error = ref('')

const goBack = () => {
  router.back()
}

const login = () => {
  if (!email.value || !password.value) {
    error.value = 'Please enter email and password'
    return
  }

  // Пример имитации логина (позже заменим на реальный API)
  // Роль может возвращаться с бэкенда, например:
  // { email, role: 'student' | 'company' | 'guarantor' }

  // Временно - фейковая логика по email
  if (email.value.includes('student')) router.push('/dashboard/student')
  else if (email.value.includes('company')) router.push('/dashboard/company')
  else if (email.value.includes('guarantor')) router.push('/dashboard/guarantor')
  else router.push('/dashboard/student') // fallback
}
</script>