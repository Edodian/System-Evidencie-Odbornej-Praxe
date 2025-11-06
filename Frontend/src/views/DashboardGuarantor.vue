<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header -->
    <header class="bg-indigo-600 text-white py-4 shadow-md">
      <div class="max-w-6xl mx-auto flex justify-between items-center px-6">
        <h1 class="text-2xl font-semibold">Guarantor Dashboard</h1>
        <button
          @click="logout"
          class="bg-white text-indigo-600 font-medium px-4 py-2 rounded-lg hover:bg-indigo-100"
        >
          Logout
        </button>
      </div>
    </header>
  
    <!-- 🔹 Фильтры -->
    <div class="flex flex-wrap items-center justify-between gap-4 mb-6">
      <div class="flex flex-wrap gap-4">
        <div>
          <label class="text-gray-700 mr-2">Student:</label>
          <input
            v-model="filters.student"
            type="text"
            placeholder="e.g. John Doe"
            class="border rounded px-2 py-1"
          />
        </div>

        <div>
          <label class="text-gray-700 mr-2">Company:</label>
          <input
            v-model="filters.company"
            type="text"
            placeholder="e.g. ACME Corp"
            class="border rounded px-2 py-1"
          />
        </div>

        <div>
          <label class="text-gray-700 mr-2">Year:</label>
          <select v-model="filters.year" class="border rounded px-2 py-1">
            <option value="">All</option>
            <option v-for="year in years" :key="year" :value="year">
              {{ year }}
            </option>
          </select>
        </div>

        <div>
          <label class="text-gray-700 mr-2">Status:</label>
          <select v-model="filters.status" class="border rounded px-2 py-1">
            <option value="">All</option>
            <option>Pending</option>
            <option>Approved</option>
            <option>Rejected</option>
          </select>
        </div>
      </div>

      <button
        @click="exportReport"
        class="bg-indigo-600 text-white px-4 py-2 rounded hover:bg-indigo-700"
      >
        Export Report
      </button>
    </div>

    <!-- 🔹 Таблица -->
    <table class="min-w-full bg-white shadow rounded overflow-hidden">
      <thead>
        <tr class="bg-indigo-50">
          <th class="py-2 px-4 text-left">Student</th>
          <th class="py-2 px-4 text-left">Company</th>
          <th class="py-2 px-4 text-left">Year</th>
          <th class="py-2 px-4 text-left">Status</th>
          <th class="py-2 px-4 text-left">Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="internship in filteredInternships"
          :key="internship.id"
          class="border-t hover:bg-gray-50 transition"
        >
          <td class="py-2 px-4">{{ internship.student }}</td>
          <td class="py-2 px-4">{{ internship.company }}</td>
          <td class="py-2 px-4">{{ internship.year }}</td>
          <td class="py-2 px-4">
            <span
              class="px-3 py-1 rounded-full text-sm font-medium"
              :class="{
                'bg-yellow-100 text-yellow-700': internship.status === 'Pending',
                'bg-green-100 text-green-700': internship.status === 'Approved',
                'bg-red-100 text-red-700': internship.status === 'Rejected'
              }"
            >
              {{ internship.status }}
            </span>
          </td>
          <td class="py-2 px-4">
            <button
              class="bg-green-600 text-white px-3 py-1 rounded hover:bg-green-700 mr-2"
              @click="updateStatus(internship.id, 'Approved')"
            >
              Approve
            </button>
            <button
              class="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600"
              @click="updateStatus(internship.id, 'Rejected')"
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
  localStorage.removeItem('token')
  localStorage.removeItem('role')
  router.push('/login')
}

import { ref, computed } from 'vue'

// 📌 Список стажировок (пока локально)
const internships = ref([
  { id: 1, student: 'John Doe', company: 'ACME Corp', year: 2025, status: 'Pending' },
  { id: 2, student: 'Jane Smith', company: 'DataMinds', year: 2025, status: 'Approved' },
  { id: 3, student: 'Tom Brown', company: 'InnovateX', year: 2024, status: 'Rejected' },
  { id: 4, student: 'Emily Davis', company: 'TechCorp', year: 2025, status: 'Pending' }
])

// 📌 Фильтры
const filters = ref({
  student: '',
  company: '',
  year: '',
  status: ''
})

// 📌 Доступные годы
const years = [2024, 2025, 2026]

// 📌 Фильтрация
const filteredInternships = computed(() => {
  return internships.value.filter(i => {
    const matchesStudent = i.student.toLowerCase().includes(filters.value.student.toLowerCase())
    const matchesCompany = i.company.toLowerCase().includes(filters.value.company.toLowerCase())
    const matchesYear = !filters.value.year || i.year === Number(filters.value.year)
    const matchesStatus = !filters.value.status || i.status === filters.value.status
    return matchesStudent && matchesCompany && matchesYear && matchesStatus
  })
})

// 📌 Обновление статуса
const updateStatus = (id, newStatus) => {
  const internship = internships.value.find(i => i.id === id)
  if (internship) internship.status = newStatus
}

// 📌 Экспорт CSV
const exportReport = () => {
  if (!filteredInternships.value.length) {
    alert('No data to export!')
    return
  }

  // Создаём заголовки и строки CSV
  const headers = ['Student', 'Company', 'Year', 'Status']
  const rows = filteredInternships.value.map(i => [i.student, i.company, i.year, i.status])

  const csvContent = [
    headers.join(','), // заголовки
    ...rows.map(r => r.join(',')) // строки
  ].join('\n')

  // Создаём Blob и ссылку для скачивания
  const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
  const url = URL.createObjectURL(blob)
  const link = document.createElement('a')

  const filename = `internships_report_${new Date().getFullYear()}.csv`
  link.setAttribute('href', url)
  link.setAttribute('download', filename)
  link.style.visibility = 'hidden'
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)

  console.log('✅ CSV exported:', filename)
}

</script>
