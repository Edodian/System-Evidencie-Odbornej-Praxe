<template>
  <div class="p-8">
    <h1 class="text-3xl font-bold text-indigo-700 mb-6">Company Dashboard</h1>

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
import { ref } from 'vue'

const applications = ref([
  { student: 'John Doe', program: 'Informatics', period: 'Mar–Jun 2025', status: 'Pending' },
  { student: 'Jane Smith', program: 'Computer Science', period: 'Apr–Jul 2025', status: 'Pending' }
])

const updateStatus = (index, newStatus) => {
  applications.value[index].status = newStatus
}
</script>
