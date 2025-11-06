<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50 px-4">
    <div
      class="w-full max-w-md bg-white rounded-2xl shadow-lg p-8 border border-gray-100 transition-all duration-300 hover:shadow-xl"
    >
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
          <label class="block text-gray-700 mb-2">Old Password</label>
          <input
            type="password"
            v-model="oldPassword"
            placeholder="Enter current password"
            class="w-full border border-gray-300 rounded-lg px-3 py-2 focus:ring-indigo-500 focus:border-indigo-500"
          />
        </div>

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

      <p
        v-if="successMessage"
        class="mt-4 text-center text-green-600 bg-green-50 py-2 rounded-lg"
      >
        {{ successMessage }}
      </p>
      <p
        v-if="errorMessage"
        class="mt-4 text-center text-red-500 bg-red-50 py-2 rounded-lg"
      >
        {{ errorMessage }}
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue"
import { useRouter, useRoute } from "vue-router"
import axios from "axios"

const router = useRouter()
const route = useRoute()

const oldPassword = ref("")
const newPassword = ref("")
const confirmPassword = ref("")
const successMessage = ref("")
const errorMessage = ref("")

const email = localStorage.getItem("email")
const fromDashboard = route.query.from || "student"

const handleSubmit = async () => {
  errorMessage.value = ""
  successMessage.value = ""

  if (!oldPassword.value || !newPassword.value || !confirmPassword.value) {
    errorMessage.value = "Please fill in all fields."
    return
  }

  if (newPassword.value !== confirmPassword.value) {
    errorMessage.value = "Passwords do not match."
    return
  }

  try {
    const response = await axios.post(
      "/api/student/change-password",
      {
        email,
        oldPassword: oldPassword.value,
        newPassword: newPassword.value,
      },
      { withCredentials: true }
    )

    if (response.data.status === "PASSWORD_CHANGED") {
      successMessage.value = "Password successfully changed!"

      // 🟢 Сбрасываем флаг mustChangePwd, иначе router guard не пустит
      localStorage.setItem("mustChangePwd", "false")

      setTimeout(() => {
        // Перенаправление на дэшборд
        router.push("/dashboard/student")
      }, 1200)
    } else {
      errorMessage.value = response.data.message || "Unexpected response."
    }
  } catch (error) {
    console.error(error)
    if (error.response?.data?.error) {
      errorMessage.value = error.response.data.error
    } else {
      errorMessage.value = "Something went wrong. Please try again."
    }
  }
}

const goBack = () => {
  // 👇 Также сбрасываем флаг при нажатии кнопки
  localStorage.setItem("mustChangePwd", "false")
  router.push("/dashboard/student")
}
</script>
