<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-6">
    <div class="max-w-md w-full bg-white rounded-2xl shadow-md p-8">
      <!-- Back Arrow Box в левом верхнем углу -->
    <div 
  class="absolute top-6 left-6 w-16 h-16 flex items-center justify-center bg-indigo-50 rounded-lg cursor-pointer hover:bg-indigo-100"
  @click="goBack"
>
  <span class="text-indigo-600 text-5xl font-bold">←</span>
</div>
      <h1 class="text-2xl font-bold text-indigo-700 mb-6 text-center">
        Student Registration
      </h1>

      <form @submit.prevent="handleSubmit" class="space-y-5">
        <!-- Name -->
        <div>
          <label class="block text-gray-700 font-medium mb-1">Full Name</label>
          <input
            v-model="form.name"
            type="text"
            placeholder="John Doe"
            class="w-full border border-gray-300 rounded-lg px-4 py-2 focus:ring-2 focus:ring-indigo-500 focus:outline-none"
            required
          />
        </div>

        <!-- Student Email -->
        <div>
          <label class="block text-gray-700 font-medium mb-1">Student Email</label>
          <input
            v-model="form.studentEmail"
            type="email"
            placeholder="example@student.university.com"
            class="w-full border border-gray-300 rounded-lg px-4 py-2 focus:ring-2 focus:ring-indigo-500 focus:outline-none"
            required
          />
        </div>

        <!-- Alternate Email -->
        <div>
          <label class="block text-gray-700 font-medium mb-1">Alternate Email</label>
          <input
            v-model="form.altEmail"
            type="email"
            placeholder="example@gmail.com"
            class="w-full border border-gray-300 rounded-lg px-4 py-2 focus:ring-2 focus:ring-indigo-500 focus:outline-none"
          />
        </div>

        <!-- Phone -->
        <div>
          <label class="block text-gray-700 font-medium mb-1">Phone Number</label>
          <input
            v-model="form.phone"
            type="tel"
            placeholder="+421 900 000 000"
            class="w-full border border-gray-300 rounded-lg px-4 py-2 focus:ring-2 focus:ring-indigo-500 focus:outline-none"
          />
        </div>

        <!-- Program -->
        <div>
          <label class="block text-gray-700 font-medium mb-1">Study Program</label>
          <select
            v-model="form.program"
            class="w-full border border-gray-300 rounded-lg px-4 py-2 focus:ring-2 focus:ring-indigo-500 focus:outline-none"
            required
          >
            <option disabled value="">Select your program</option>
            <option>Computer Science</option>
            <option>Information Systems</option>
            <option>Software Engineering</option>
            <option>Data Science</option>
          </select>
        </div>

        <!-- Submit -->
        <button
          type="submit"
          class="w-full bg-indigo-600 text-white font-medium py-2 rounded-lg hover:bg-indigo-700 transition"
        >
          Register
        </button>
      </form>

      <!-- Success Message -->
      <p
        v-if="submitted"
        class="text-green-600 text-center mt-4 font-medium"
      >
        Registration successful! Check your email for confirmation.
      </p>

      <router-link
        to="/login"
        class="block text-center text-indigo-600 hover:underline mt-6"
      >
        Already have an account? Log in
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { ref } from 'vue'

const router = useRouter()

const goBack = () => {
  router.back()
}

const form = ref({
  name: '',
  studentEmail: '',
  altEmail: '',
  phone: '',
  program: '',
})

const submitted = ref(false)

const handleSubmit = () => {
  console.log('Submitted form:', form.value)
  submitted.value = true

  // Показываем сообщение 3 секунды, потом редиректим на логин
  setTimeout(() => {
    router.push('/login')
  }, 3000)
}
</script>


<style scoped>
/* пока всё на tailwind */
</style>
