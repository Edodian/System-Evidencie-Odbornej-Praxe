<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header -->
    <header class="bg-indigo-600 text-white py-4 shadow-md">
      <div class="max-w-6xl mx-auto flex justify-between items-center px-6">
        <h1 class="text-2xl font-semibold">Add Internship</h1>
        <button
          @click="goBack"
          class="bg-white text-indigo-600 font-medium px-4 py-2 rounded-lg hover:bg-indigo-100"
        >
          ← Back
        </button>
      </div>
    </header>

    <!-- Form Section -->
    <main class="max-w-3xl mx-auto bg-white mt-10 p-8 rounded-2xl shadow-md">
      <h2 class="text-2xl font-bold text-indigo-700 mb-6 text-center">
        Internship Details
      </h2>

      <form @submit.prevent="handleSubmit" class="space-y-5">
        <!-- Company -->
        <div>
          <select
  v-model="form.organizationId"
  class="w-full border border-gray-300 rounded-lg px-3 py-2 focus:ring-indigo-500 focus:border-indigo-500"
>
  <option value="">Select company</option>
  <option v-for="org in organizations" :key="org.id" :value="org.id">
    {{ org.name }}
  </option>
</select>

        </div>

        <!-- Position -->
        <div>
          <label class="block text-gray-700 mb-2">Position</label>
          <input
            type="text"
            v-model="form.position"
            placeholder="e.g. Frontend Developer Intern"
            class="w-full border border-gray-300 rounded-lg px-3 py-2 focus:ring-indigo-500 focus:border-indigo-500"
          />
        </div>

        <!-- Semester -->
        <div>
          <label class="block text-gray-700 mb-2">Semester</label>
          <select
            v-model="form.semester"
            class="w-full border border-gray-300 rounded-lg px-3 py-2 focus:ring-indigo-500 focus:border-indigo-500"
          >
            <option value="">Select semester</option>
            <option value="Spring 2025">Spring 2025</option>
            <option value="Summer 2025">Summer 2025</option>
            <option value="Fall 2025">Fall 2025</option>
          </select>
        </div>

        <!-- Start Date -->
        <div>
          <label class="block text-gray-700 mb-2">Start Date</label>
          <input
            type="date"
            v-model="form.startDate"
            class="w-full border border-gray-300 rounded-lg px-3 py-2 focus:ring-indigo-500 focus:border-indigo-500"
          />
        </div>

        <!-- End Date -->
        <div>
          <label class="block text-gray-700 mb-2">End Date</label>
          <input
            type="date"
            v-model="form.endDate"
            class="w-full border border-gray-300 rounded-lg px-3 py-2 focus:ring-indigo-500 focus:border-indigo-500"
          />
        </div>

        <!-- Description -->
        <div>
          <label class="block text-gray-700 mb-2">Description</label>
          <textarea
            v-model="form.description"
            placeholder="Describe your internship goals and tasks"
            class="w-full border border-gray-300 rounded-lg px-3 py-2 focus:ring-indigo-500 focus:border-indigo-500"
            rows="4"
          ></textarea>
        </div>

        <!-- Submit -->
        <div class="flex justify-center">
          <button
            type="submit"
            class="bg-indigo-600 text-white font-medium px-6 py-2 rounded-lg hover:bg-indigo-700 transition-all"
          >
            Submit
          </button>
        </div>
      </form>

      <!-- Success Message -->
      <div
        v-if="submitted"
        class="mt-6 text-center text-green-600 font-medium bg-green-50 py-3 rounded-lg"
      >
        Internship successfully submitted! Preparing PDF...
        <br />
        Redirecting to dashboard...
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue"
import { useRouter } from "vue-router"
import axios from '../api/api.js'

const router = useRouter()

const organizations = ref([])

const form = ref({
  organizationId: "",
  position: "",
  semester: "",
  startDate: "",
  endDate: "",
  description: ""
})

const submitted = ref(false)

onMounted(async () => {
  try {
    const res = await axios.get('/api/organization/all')
    organizations.value = res.data
  } catch (err) {
    console.error('Failed to load organizations:', err)
  }
})

const handleSubmit = async () => {
  console.log("Internship submitted:", form.value)

  try {
    await axios.post('/api/internship', {
      organizationId: form.value.organizationId,
      beginDate: form.value.startDate,
      endDate: form.value.endDate,
      note: form.value.description,
      semester: form.value.semester,
      position: form.value.position
    })

    submitted.value = true

    setTimeout(() => {
      router.push("/dashboard/student")
    }, 2000)
  } catch (err) {
    console.error("Failed to submit internship:", err)
  }
}

const goBack = () => {
  router.back()
}
</script>

