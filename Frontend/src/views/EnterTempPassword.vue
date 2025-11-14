<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50 px-4">
    <div class="w-full max-w-md bg-white rounded-2xl shadow-lg p-8 relative">

      <!-- Back -->
      <div class="mb-4">
  <button 
    @click="goBack"
    class="inline-flex items-center text-indigo-600 hover:text-indigo-800 font-semibold"
  >
    ← Back
  </button>
</div>


      <h1 class="text-2xl font-bold text-center text-indigo-600 mb-6">
        Enter Temporary Password
      </h1>

      <p class="text-center text-gray-600 mb-6 text-sm">
        A temporary password was sent to your email.  
        Enter it below to continue.
      </p>

      <!-- Form -->
      <form @submit.prevent="submitTempPassword" class="space-y-5">
        <div>
          <label class="block text-gray-700 mb-1">Temporary Password</label>
          <input
            v-model="tempPassword"
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
          Continue
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
const tempPassword = ref('')
const error = ref('')

const goBack = () => router.push('/register/student')

const submitTempPassword = async () => {
  try {
    // 🟡 пока без бекенда — просто мок
    if (tempPassword.value.length < 4) {
      error.value = "Invalid temporary password"
      return
    }

    // При успехе:
    router.push('/create-new-password')

    // 🔵 когда появится бек:
    /*
    const res = await axios.post('/auth/check-temp-password', {
      tempPassword: tempPassword.value
    })
    if (res.status === 200) {
      router.push('/create-password')
    }
    */
  } catch (e) {
    error.value = "Wrong temporary password"
  }
}
</script>

<style scoped></style>
