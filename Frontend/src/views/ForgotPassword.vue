<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50 px-4">
    <div
      class="w-full max-w-md bg-white rounded-2xl shadow-lg p-8 border border-gray-100 transition-all duration-300 hover:shadow-xl"
    >
      <h1 class="text-3xl font-bold text-center text-indigo-600 mb-6">
        Forgot Password
      </h1>

      <p class="text-gray-600 text-center mb-6">
        Enter your email address and we’ll send you a link to reset your password.
      </p>

      <!-- Email -->
      <div class="mb-6">
        <label class="block text-gray-700 mb-2">Email</label>
        <input
          type="email"
          v-model="email"
          placeholder="you@example.com"
          class="w-full border border-gray-300 rounded-lg px-3 py-2 focus:ring-indigo-500 focus:border-indigo-500"
        />
      </div>

      <!-- Submit -->
      <button
        @click="handleSubmit"
        class="w-full bg-indigo-600 text-white font-medium py-2 rounded-lg hover:bg-indigo-700 transition-all"
      >
        Send Reset Link
      </button>

      <!-- Message -->
      <p
        v-if="message"
        class="mt-4 text-center text-green-600 bg-green-50 py-2 rounded-lg"
      >
        {{ message }}
      </p>

      <!-- Error -->
      <p
        v-if="error"
        class="mt-4 text-center text-red-500 bg-red-50 py-2 rounded-lg"
      >
        {{ error }}
      </p>

      <!-- Back to Login -->
      <div class="text-center mt-6 text-gray-500 text-sm">
        Remember your password?
        <router-link to="/login" class="text-indigo-600 hover:underline">
          Back to Login
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue"
import { useRouter } from "vue-router"

const router = useRouter()
const email = ref("")
const message = ref("")
const error = ref("")

const handleSubmit = async () => {
  if (!email.value) {
    error.value = "Please enter your email"
    return
  }

  error.value = ""
  message.value = ""

  try {
    // 🔹 Mock API POST-запрос
    await new Promise((resolve) => setTimeout(resolve, 1000))
    console.log("POST /api/forgot-password", { email: email.value })

    message.value = "Password reset link has been sent to your email."
    setTimeout(() => router.push("/login"), 2500)
  } catch (err) {
    error.value = "Something went wrong. Please try again later."
  }
}
</script>
