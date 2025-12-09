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

      <h1 class="text-2xl font-bold text-center text-indigo-600 mb-4">
        Forgot Password
      </h1>

      <p class="text-gray-600 text-center mb-6 text-sm">
        Enter your email address and we will send you a temporary code to reset
        your password.
      </p>

      <form @submit.prevent="handleSubmit" class="space-y-5">
        <!-- Email -->
        <div>
          <label class="block text-gray-700 mb-1">Email</label>
          <input
            type="email"
            v-model="email"
            placeholder="you@example.com"
            class="w-full border border-gray-300 rounded-lg px-3 py-2 focus:ring-indigo-500 focus:border-indigo-500"
            required
          />
        </div>

        <button
          type="submit"
          class="w-full bg-indigo-600 text-white font-medium py-2 rounded-lg hover:bg-indigo-700 transition-all"
          :disabled="loading"
        >
          {{ loading ? "Sending..." : "Send reset code" }}
        </button>
      </form>

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
import { ref } from "vue";
import { useRouter } from "vue-router";
import axios from "../api.js";

const router = useRouter();
const email = ref("");
const message = ref("");
const error = ref("");
const loading = ref(false);

const goBack = () => {
  router.push("/login");
};

const handleSubmit = async () => {
  if (!email.value) {
    error.value = "Please enter your email";
    message.value = "";
    return;
  }

  error.value = "";
  message.value = "";
  loading.value = true;

  try {
    const res = await axios.post("/api/student/reset-password", {
      email: email.value,
    });

    // backend: "Reset code was sent to your email and is valid for 5 minutes."
    message.value = res.data.message || "Reset code was sent to your email.";

    // сохраняем email, чтобы EnterTempPassword и CreateNewPassword знали, с кем работаем
    sessionStorage.setItem("regEmail", email.value);

    // чуть подождали и отправили вводить временный пароль
    setTimeout(() => router.push("/enter-temp-password"), 1000);
  } catch (err) {
    console.error("Reset password error:", err);
    if (err.response) {
      if (err.response.status === 404) {
        error.value = "User with this email was not found.";
      } else {
        error.value =
          err.response.data?.error || "Server error. Please try again later.";
      }
    } else {
      error.value = "Network error. Please try again later.";
    }
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped></style>
