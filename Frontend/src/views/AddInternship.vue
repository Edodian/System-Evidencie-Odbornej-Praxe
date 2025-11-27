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
          <label class="block text-gray-700 mb-2">Company Name</label>
          <input
            type="text"
            v-model="form.company"
            placeholder="Enter company name"
            class="w-full border border-gray-300 rounded-lg px-3 py-2 focus:ring-indigo-500 focus:border-indigo-500"
          />
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
import { ref } from "vue"
import { useRouter } from "vue-router"

const router = useRouter()

const form = ref({
  company: "",
  position: "",
  semester: "",
  startDate: "",
  endDate: "",
  description: ""
})

const submitted = ref(false)

const handleSubmit = async () => {
  console.log("Internship submitted:", form.value)

  // имитация POST-запроса
  await new Promise(resolve => setTimeout(resolve, 1000))
  console.log("POST request sent successfully!")

  // имитация скачивания PDF
  const pdfBlob = new Blob(["Mock PDF content"], { type: "application/pdf" })
  const pdfUrl = URL.createObjectURL(pdfBlob)
  const link = document.createElement("a")
  link.href = pdfUrl
  link.download = "internship_confirmation.pdf"
  link.click()

  submitted.value = true

  // через 2 секунды возвращаемся на дашборд
  setTimeout(() => {
    router.push("/dashboard/student")
  }, 2000)
}

const goBack = () => {
  router.back()
}
</script>
