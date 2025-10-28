<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50 px-4">
    <div
      class="w-full max-w-md bg-white rounded-2xl shadow-lg p-8 border border-gray-100 transition-all duration-300 hover:shadow-xl"
    >
      <h1 class="text-3xl font-bold text-center text-indigo-600 mb-6">Login</h1>

      <!-- Role Selector -->
      <div class="mb-6">
        <label class="block text-gray-700 font-medium mb-2">Select Role</label>
        <select
          v-model="role"
          class="w-full border-gray-300 rounded-lg shadow-sm focus:ring-indigo-500 focus:border-indigo-500 px-3 py-2"
        >
          <option value="">Choose role</option>
          <option value="student">Student</option>
          <option value="company">Company</option>
          <option value="guarantor">Guarantor</option>
        </select>
      </div>

      <!-- Common fields -->
      <div class="mb-4">
        <label class="block text-gray-700 mb-2">Email</label>
        <input
          type="email"
          v-model="email"
          placeholder="you@example.com"
          class="w-full border border-gray-300 rounded-lg px-3 py-2 focus:ring-indigo-500 focus:border-indigo-500"
        />
      </div>

      <div class="mb-6">
        <label class="block text-gray-700 mb-2">Password</label>
        <input
          type="password"
          v-model="password"
          placeholder="••••••••"
          class="w-full border border-gray-300 rounded-lg px-3 py-2 focus:ring-indigo-500 focus:border-indigo-500"
        />
      </div>

      <!-- Extra fields -->
      <div v-if="role === 'student'" class="mb-6">
        <label class="block text-gray-700 mb-2">Student ID</label>
        <input
          type="text"
          v-model="studentId"
          placeholder="e.g. ST12345"
          class="w-full border border-gray-300 rounded-lg px-3 py-2 focus:ring-indigo-500 focus:border-indigo-500"
        />
      </div>

      <div v-if="role === 'company'" class="mb-6">
        <label class="block text-gray-700 mb-2">Company Code</label>
        <input
          type="text"
          v-model="companyCode"
          placeholder="e.g. CMP001"
          class="w-full border border-gray-300 rounded-lg px-3 py-2 focus:ring-indigo-500 focus:border-indigo-500"
        />
      </div>

      <div v-if="role === 'guarantor'" class="mb-6">
        <label class="block text-gray-700 mb-2">Guarantor Key</label>
        <input
          type="text"
          v-model="guarantorKey"
          placeholder="e.g. GUAR-KEY"
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

      <!-- Message -->
      <p v-if="error" class="text-red-500 text-sm mt-4 text-center">{{ error }}</p>

      <div class="text-center mt-6 text-gray-500 text-sm">
        Don’t have an account?
        <router-link to="/register/student" class="text-indigo-600 hover:underline"
          >Register</router-link
        >
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const role = ref('')
const email = ref('')
const password = ref('')
const studentId = ref('')
const companyCode = ref('')
const guarantorKey = ref('')
const error = ref('')

const login = () => {
  if (!role.value) {
    error.value = 'Please select a role'
    return
  }
  if (!email.value || !password.value) {
    error.value = 'Please enter email and password'
    return
  }

  // Simulate login success
  if (role.value === 'student') router.push('/dashboard/student')
  else if (role.value === 'company') router.push('/dashboard/company')
  else if (role.value === 'guarantor') router.push('/dashboard/guarantor')
}
</script>
