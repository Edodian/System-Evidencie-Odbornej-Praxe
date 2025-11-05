<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50 px-4">
    <div class="w-full max-w-md bg-white rounded-2xl shadow-lg p-8 border border-gray-100 transition-all duration-300 hover:shadow-xl">

      <!-- Back Button -->
      <button
        @click="goBack"
        class="mb-4 flex items-center text-indigo-600 hover:text-indigo-800 font-medium"
      >
        ← Back to Dashboard
      </button>

      <h1 class="text-3xl font-bold text-center text-indigo-600 mb-6">
        Change Password
      </h1>

      <form @submit.prevent="handleSubmit" class="space-y-4">
        <div>
          <label class="block text-gray-700 mb-2">New Password</label>
          <input
            type="password"
            v-model="newPassword"
            placeholder="Enter new password"
            class="w-full border border-gray-300 rounded-lg px-3 py-2 focus:ring-indigo-500 focus:border-indigo-500"
          />
        </div>

        <div>
          <label class="block text-gray-700 mb-2">Confirm Password</label>
          <input
            type="password"
            v-model="confirmPassword"
            placeholder="Confirm new password"
            class="w-full border border-gray-300 rounded-lg px-3 py-2 focus:ring-indigo-500 focus:border-indigo-500"
          />
        </div>

        <button
          type="submit"
          class="w-full bg-indigo-600 text-white font-medium py-2 rounded-lg hover:bg-indigo-700 transition-all"
        >
          Change Password
        </button>
      </form>

      <p v-if="successMessage" class="mt-4 text-center text-green-600 bg-green-50 py-2 rounded-lg">
        {{ successMessage }}
      </p>
      <p v-if="errorMessage" class="mt-4 text-center text-red-500 bg-red-50 py-2 rounded-lg">
        {{ errorMessage }}
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue"
import { useRouter, useRoute } from "vue-router"

const router = useRouter()
const route = useRoute() // получаем текущий route

const newPassword = ref("")
const confirmPassword = ref("")
const successMessage = ref("")
const errorMessage = ref("")

// 🔹 Считываем, откуда пользователь пришёл (query from)
const fromDashboard = route.query.from || "login" // fallback на логин

const handleSubmit = async () => {
  if (!newPassword.value || !confirmPassword.value) {
    errorMessage.value = "Please fill in both fields."
    successMessage.value = ""
    return
  }

  if (newPassword.value !== confirmPassword.value) {
    errorMessage.value = "Passwords do not match."
    successMessage.value = ""
    return
  }

  try {
    await new Promise((resolve) => setTimeout(resolve, 1000))
    console.log("POST /api/change-password", { newPassword: newPassword.value })
    successMessage.value = "Password successfully changed!"
    errorMessage.value = ""

    setTimeout(() => {
      // редирект в зависимости от from
      if (fromDashboard === "student") router.push("/dashboard/student")
      else if (fromDashboard === "company") router.push("/dashboard/company")
      else router.push("/login")
    }, 2000)
  } catch {
    errorMessage.value = "Something went wrong. Please try again."
    successMessage.value = ""
  }
}

// кнопка "Back to Dashboard"
const goBack = () => {
  if (fromDashboard === "student") router.push("/dashboard/student")
  else if (fromDashboard === "company") router.push("/dashboard/company")
  else router.push("/login")
}
</script>
