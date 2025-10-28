<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-6">
    <div class="max-w-md w-full bg-white rounded-2xl shadow-md p-8">
      <h1 class="text-2xl font-bold text-indigo-700 mb-6 text-center">
        Company Registration
      </h1>

      <form @submit.prevent="handleSubmit" class="space-y-5">
        <!-- Company Name -->
        <div>
          <label class="block text-gray-700 font-medium mb-1">Company Name</label>
          <input
            v-model="form.companyName"
            type="text"
            placeholder="ACME Corporation"
            class="w-full border border-gray-300 rounded-lg px-4 py-2 focus:ring-2 focus:ring-indigo-500 focus:outline-none"
            required
          />
        </div>

        <!-- IČO -->
        <div>
          <label class="block text-gray-700 font-medium mb-1">IČO</label>
          <input
            v-model="form.ico"
            type="text"
            placeholder="12345678"
            class="w-full border border-gray-300 rounded-lg px-4 py-2 focus:ring-2 focus:ring-indigo-500 focus:outline-none"
            required
            pattern="^[0-9]{8}$"
            title="IČO must be 8 digits"
          />
        </div>

        <!-- Contact Person -->
        <div>
          <label class="block text-gray-700 font-medium mb-1">Contact Person</label>
          <input
            v-model="form.contactPerson"
            type="text"
            placeholder="John Doe"
            class="w-full border border-gray-300 rounded-lg px-4 py-2 focus:ring-2 focus:ring-indigo-500 focus:outline-none"
            required
          />
        </div>

        <!-- Email -->
        <div>
          <label class="block text-gray-700 font-medium mb-1">Company Email</label>
          <input
            v-model="form.email"
            type="email"
            placeholder="info@company.com"
            class="w-full border border-gray-300 rounded-lg px-4 py-2 focus:ring-2 focus:ring-indigo-500 focus:outline-none"
            required
          />
        </div>

        <!-- Phone -->
        <div>
          <label class="block text-gray-700 font-medium mb-1">Phone Number</label>
          <input
            v-model="form.phone"
            type="tel"
            placeholder="+421 900 123 456"
            class="w-full border border-gray-300 rounded-lg px-4 py-2 focus:ring-2 focus:ring-indigo-500 focus:outline-none"
          />
        </div>

        <!-- Address -->
        <div>
          <label class="block text-gray-700 font-medium mb-1">Address</label>
          <input
            v-model="form.address"
            type="text"
            placeholder="Main Street 12, Bratislava"
            class="w-full border border-gray-300 rounded-lg px-4 py-2 focus:ring-2 focus:ring-indigo-500 focus:outline-none"
          />
        </div>

        <!-- Industry -->
        <div>
          <label class="block text-gray-700 font-medium mb-1">Industry</label>
          <select
            v-model="form.industry"
            class="w-full border border-gray-300 rounded-lg px-4 py-2 focus:ring-2 focus:ring-indigo-500 focus:outline-none"
            required
          >
            <option disabled value="">Select industry</option>
            <option>Software Development</option>
            <option>Finance</option>
            <option>Manufacturing</option>
            <option>Education</option>
            <option>Other</option>
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
        Company registered successfully! We will contact you soon.
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
import { ref } from 'vue'

const form = ref({
  companyName: '',
  ico: '',
  contactPerson: '',
  email: '',
  phone: '',
  address: '',
  industry: '',
})

const submitted = ref(false)

const handleSubmit = () => {
  console.log('Submitted company registration:', form.value)
  submitted.value = true
}
</script>

<style scoped>
/* всё на tailwind */
</style>
