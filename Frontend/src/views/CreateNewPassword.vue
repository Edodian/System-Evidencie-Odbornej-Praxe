<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50 px-4">
    <div class="w-full max-w-md bg-white rounded-2xl shadow-lg p-8 relative">
      
      <!-- Back -->
      <button 
        @click="goBack"
        class="inline-flex items-center text-indigo-600 hover:text-indigo-800 font-semibold mb-4"
      >
        ← Back
      </button>

      <h1 class="text-2xl font-bold text-center text-indigo-600 mb-6">
        Create New Password
      </h1>

      <form @submit.prevent="createNewPassword" class="space-y-5">
        <div>
          <label class="block text-gray-700 mb-1">New Password</label>
          <input
            v-model="newPwd"
            type="password"
            placeholder="••••••••"
            class="w-full border border-gray-300 rounded-lg px-3 py-2 focus:ring-indigo-500 focus:border-indigo-500"
            required
          />
        </div>

        <div>
          <label class="block text-gray-700 mb-1">Repeat Password</label>
          <input
            v-model="repeatPwd"
            type="password"
            placeholder="••••••••"
            class="w-full border border-gray-300 rounded-lg px-3 py-2 focus:ring-indigo-500 focus:border-indigo-500"
            required
          />
        </div>

        <button
          type="submit"
          class="w-full bg-indigo-600 text-white font-medium py-2 rounded-lg hover:bg-indigo-700 transition"
        >
          Save Password
        </button>

        <!-- Abort button -->
        <button
          type="button"
          @click="abortChange"
          class="w-full mt-3 text-indigo-600 font-medium py-2 rounded-lg hover:bg-indigo-50 transition"
        >
          Abort changes
        </button>
      </form>

      <p v-if="error" class="text-red-600 text-center mt-4">{{ error }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from '../api.js'

const router = useRouter()
const newPwd = ref('')
const repeatPwd = ref('')
const error = ref('')

// 💡 Не даём «просто так» заходить на страницу
onMounted(() => {
  const email = sessionStorage.getItem('regEmail') || localStorage.getItem('email')
  const tempPassword = sessionStorage.getItem('tempPassword')

  if (!email || !tempPassword) {
    // Нет контекста (ни email, ни кода) → пользователь обошёл флоу
    router.replace('/login')
  }
})

const goBack = () => {
  router.back() 
}

const abortChange = () => router.push('/login')

const createNewPassword = async () => {
  error.value = ""

  if (newPwd.value !== repeatPwd.value) {
    error.value = "Passwords do not match"
    return
  }

  if (newPwd.value.length < 6) {
    error.value = "Password must be at least 6 characters"
    return
  }

  const email = sessionStorage.getItem('regEmail') || localStorage.getItem('email')
  const tempPassword = sessionStorage.getItem('tempPassword')

  if (!email || !tempPassword) {
    // Дублирующая защита
    error.value = "Password reset flow expired. Please start again."
    router.push('/forgot-password')
    return
  }

  try {
    const res = await axios.post(
      '/api/student/create-password',
      {
        email,
        tempPassword,
        newPassword: newPwd.value,
        confirmPassword: repeatPwd.value
      },
      { withCredentials: true }
    )

    if (res.data.status === 'PASSWORD_CREATED') {
      sessionStorage.removeItem('regEmail')
      sessionStorage.removeItem('tempPassword')
      router.push('/login')
    } else {
      error.value = res.data.message || "Failed to set password"
    }
  } catch (e) {
    console.error('Create password error:', e)

    const status = e.response?.status
    const backendStatus = e.response?.data?.status
    const backendError = e.response?.data?.error

    // На самом деле до этого момента tempPassword уже был проверен в /verify-temp-password,
    // так что сюда эти ошибки долетать почти не должны, но на всякий случай:

    if (status === 401 && backendError === 'Invalid temporary password.') {
      error.value = "Temporary password is wrong."
      sessionStorage.removeItem('tempPassword')
      router.push('/enter-temp-password')
      return
    }

    if (status === 403 && backendStatus === 'TEMPORARY_PASSWORD_EXPIRED') {
      error.value = e.response?.data?.message || "Temporary password expired. Please request a new code."
      sessionStorage.removeItem('tempPassword')
      router.push('/forgot-password')
      return
    }

    error.value = backendError || e.message || "Failed to set password"
  }
}
</script>

<style scoped></style>
