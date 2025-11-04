<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header -->
    <header class="bg-indigo-600 text-white py-4 shadow-md">
      <div class="max-w-6xl mx-auto flex justify-between items-center px-6">
        <h1 class="text-2xl font-semibold">Company Dashboard</h1>
        <button
          @click="logout"
          class="bg-white text-indigo-600 font-medium px-4 py-2 rounded-lg hover:bg-indigo-100"
        >
          Logout
        </button>
      </div>
    </header>
    <table class="min-w-full bg-white shadow rounded">
      <thead>
        <tr class="bg-indigo-50">
          <th class="py-2 px-4 text-left">Student</th>
          <th class="py-2 px-4 text-left">Program</th>
          <th class="py-2 px-4 text-left">Internship Period</th>
          <th class="py-2 px-4 text-left">Status</th>
          <th class="py-2 px-4 text-left">Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="(app, index) in applications"
          :key="index"
          class="border-t hover:bg-gray-50 transition"
        >
          <td class="py-2 px-4">{{ app.student }}</td>
          <td class="py-2 px-4">{{ app.program }}</td>
          <td class="py-2 px-4">{{ app.period }}</td>
          <td
            class="py-2 px-4 font-medium"
            :class="{
              'text-yellow-600': app.status === 'Pending',
              'text-green-600': app.status === 'Accepted',
              'text-red-600': app.status === 'Rejected'
            }"
          >
            {{ app.status }}
          </td>
          <td class="py-2 px-4">
            <button
              class="bg-indigo-600 text-white px-3 py-1 rounded hover:bg-indigo-700"
              @click="updateStatus(index, 'Accepted')"
            >
              Accept
            </button>
            <button
              class="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600 ml-2"
              @click="updateStatus(index, 'Rejected')"
            >
              Reject
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()

const logout = () => {
  router.push('/login')
}

import { ref } from 'vue'

const applications = ref([
  { student: 'John Doe', program: 'Informatics', period: 'Mar–Jun 2025', status: 'Pending' },
  { student: 'Jane Smith', program: 'Computer Science', period: 'Apr–Jul 2025', status: 'Pending' }
])

const updateStatus = (index, newStatus) => {
  applications.value[index].status = newStatus
}
</script>
