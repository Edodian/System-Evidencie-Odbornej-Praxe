<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50 px-4">
    <div class="w-full max-w-md bg-white rounded-2xl shadow-lg p-8 relative">

      <!-- Back -->
     <button 
    @click="goBack"
    class="inline-flex items-center text-indigo-600 hover:text-indigo-800 font-semibold"
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
      </form>

      <p v-if="error" class="text-red-600 text-center mt-4">{{ error }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from '../api.js'

const router = useRouter()
const newPwd = ref('')
const repeatPwd = ref('')
const error = ref('')

const goBack = () => router.back()

const createNewPassword = async () => {
  if (newPwd.value !== repeatPwd.value) {
    error.value = "Passwords do not match"
    return
  }

  if (newPwd.value.length < 6) {
    error.value = "Password must be at least 6 characters"
    return
  }

  try {
    // 🟡 пока нет бекенда — мок
    router.push('/login')

    // 🔵 потом:
    /*
    const res = await axios.post('/auth/set-password', {
      password: newPwd.value
    })
    if (res.status === 200) {
      router.push('/login')
    }
    */
  } catch (e) {
    error.value = "Failed to set password"
  }
}
</script>

<style scoped></style>
