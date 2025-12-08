<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header -->
    <header class="bg-indigo-600 text-white py-4 shadow-md">
      <div class="max-w-6xl mx-auto flex justify-between items-center px-6">
        <h1 class="text-2xl font-semibold">Guarantor Dashboard</h1>
        
        <div class="flex items-center gap-3">
          <!-- Companies Button - теперь с иконкой -->
          <button
            @click="goToCompanies"
            class="flex items-center gap-2 bg-white/20 hover:bg-white/30 text-white font-medium px-4 py-2 rounded-lg transition-all duration-200 backdrop-blur-sm border border-white/20"
            title="View Companies"
          >
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4"/>
            </svg>
            <span class="hidden sm:inline">Companies</span>
          </button>

          <!-- Logout Button - сдвинут вправо и с иконкой -->
          <button
            @click="logout"
            class="flex items-center gap-2 bg-white text-indigo-600 font-medium px-4 py-2 rounded-lg hover:bg-indigo-100 transition-all duration-200 shadow-sm hover:shadow-md"
          >
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"/>
            </svg>
            <span class="hidden sm:inline">Logout</span>
          </button>
        </div>
      </div>
    </header>

    <!-- 🔹 Фильтры -->
    <div class="max-w-6xl mx-auto px-6 mt-6">
      <div class="flex flex-wrap items-center justify-between gap-4 mb-6">
        <div class="flex flex-wrap gap-4">
          <div>
            <label class="text-gray-700 mr-2 text-sm font-medium">Student:</label>
            <input
              v-model="filters.student"
              type="text"
              placeholder="e.g. John Doe"
              class="border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
            />
          </div>

          <div>
            <label class="text-gray-700 mr-2 text-sm font-medium">Company:</label>
            <input
              v-model="filters.company"
              type="text"
              placeholder="e.g. ACME Corp"
              class="border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
            />
          </div>

          <div>
            <label class="text-gray-700 mr-2 text-sm font-medium">Year:</label>
            <select v-model="filters.year" class="border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent">
              <option value="">All</option>
              <option v-for="year in years" :key="year" :value="year">
                {{ year }}
              </option>
            </select>
          </div>

          <div>
            <label class="text-gray-700 mr-2 text-sm font-medium">Status:</label>
            <select v-model="filters.status" class="border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent">
              <option value="">All</option>
              <option>Pending</option>
              <option>Approved</option>
              <option>Rejected</option>
            </select>
          </div>
        </div>

        <button
          @click="exportReport"
          class="flex items-center gap-2 bg-indigo-600 text-white px-4 py-2 rounded-lg hover:bg-indigo-700 transition-all duration-200 shadow-sm hover:shadow-md"
        >
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 10v6m0 0l-3-3m3 3l3-3m2 8H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/>
          </svg>
          Export Report
        </button>
      </div>

      <!-- 🔹 Таблица -->
      <div class="bg-white shadow-lg rounded-xl overflow-hidden">
        <table class="min-w-full">
          <thead>
            <tr class="bg-indigo-50">
              <th class="py-3 px-6 text-left text-sm font-semibold text-indigo-900">Student</th>
              <th class="py-3 px-6 text-left text-sm font-semibold text-indigo-900">Company</th>
              <th class="py-3 px-6 text-left text-sm font-semibold text-indigo-900">Year</th>
              <th class="py-3 px-6 text-left text-sm font-semibold text-indigo-900">Status</th>
              <th class="py-3 px-6 text-left text-sm font-semibold text-indigo-900">Actions</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-200">
            <tr
              v-for="internship in filteredInternships"
              :key="internship.id"
              class="hover:bg-gray-50 transition-colors duration-150"
            >
              <td class="py-4 px-6 text-sm text-gray-900">{{ internship.student }}</td>
              <td class="py-4 px-6 text-sm text-gray-700">{{ internship.company }}</td>
              <td class="py-4 px-6 text-sm text-gray-700">{{ internship.year }}</td>
              <td class="py-4 px-6">
                <span
                  class="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium"
                  :class="{
                    'bg-yellow-100 text-yellow-800': internship.status === 'Pending',
                    'bg-green-100 text-green-800': internship.status === 'Approved',
                    'bg-red-100 text-red-800': internship.status === 'Rejected'
                  }"
                >
                  {{ internship.status }}
                </span>
              </td>
              <td class="py-4 px-6">
                <div class="flex gap-2">
                  <button
                    class="flex items-center gap-1 bg-green-600 text-white px-3 py-2 rounded-lg hover:bg-green-700 transition-colors duration-200 text-sm font-medium"
                    @click="updateStatus(internship.id, 'Approved')"
                  >
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/>
                    </svg>
                    Approve
                  </button>
                  <button
                    class="flex items-center gap-1 bg-red-500 text-white px-3 py-2 rounded-lg hover:bg-red-600 transition-colors duration-200 text-sm font-medium"
                    @click="updateStatus(internship.id, 'Rejected')"
                  >
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
                    </svg>
                    Reject
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>

import { ref, computed, onMounted } from 'vue'
import { getAllInternships, changeInternshipStatus } from '@/api/internships'

// Список стажировок теперь с бекенда
const internships = ref([])

// Загружаем стажировки с сервера
const loadInternships = async () => {
  try {
    internships.value = await getAllInternships()
  } catch (e) {
    console.error('Failed to fetch internships', e)
  }
}

onMounted(loadInternships)

// Обновление статуса через бэк
const updateStatus = async (id, newStatus) => {
  try {
    await changeInternshipStatus(id, newStatus)
    await loadInternships() // перезагружаем данные
  } catch (e) {
    console.error('Failed to update status', e)
  }
}

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
  { id: 2, student: 'ХУЙ', company: 'DataMinds', year: 2025, status: 'Approved' },
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

const goToCompanies = () => {
  router.push("/guarantor/companies")
}
</script>