<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header -->
    <header class="bg-indigo-600 text-white py-4 shadow-md">
      <div class="max-w-6xl mx-auto flex justify-between items-center px-6">
        <h1 class="text-2xl font-semibold">Student Dashboard</h1>

        <!-- Кнопки справа -->
        <div class="flex items-center space-x-2">
          <button
            @click="goToChangePassword"
            class="bg-white text-indigo-600 font-medium px-4 py-2 rounded-lg hover:bg-indigo-100"
          >
            Change Password
          </button>

          <button
            @click="logout"
            class="bg-white text-indigo-600 font-medium px-4 py-2 rounded-lg hover:bg-indigo-100"
          >
            Logout
          </button>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="max-w-6xl mx-auto py-10 px-6">
      <div class="flex justify-between items-center mb-6">
        <h2 class="text-3xl font-bold text-indigo-700">
          Welcome, {{ studentName }}
        </h2>

        <!-- 🔹 Add Internship Button -->
        <button
          @click="goToAddInternship"
          class="bg-indigo-600 text-white px-5 py-2 rounded-lg font-medium hover:bg-indigo-700 transition-all"
        >
          + Add Internship
        </button>
      </div>

      <div class="bg-white shadow-md rounded-2xl p-6">
        <h3 class="text-xl font-semibold mb-4 text-gray-800">Your Internships</h3>

        <table class="w-full border-collapse text-left">
          <thead>
            <tr class="border-b bg-indigo-50 text-indigo-700">
              <th class="py-3 px-4">Company</th>
              <th class="py-3 px-4">Position</th>
              <th class="py-3 px-4">Start Date</th>
              <th class="py-3 px-4">End Date</th>
              <th class="py-3 px-4">Status</th>
              <th class="py-3 px-4 text-center">Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="internship in internships"
              :key="internship.id"
              class="border-b hover:bg-gray-50 transition"
            >
              <td class="py-3 px-4">{{ internship.company }}</td>
              <td class="py-3 px-4">{{ internship.position }}</td>
              <td class="py-3 px-4">{{ internship.startDate }}</td>
              <td class="py-3 px-4">{{ internship.endDate }}</td>
              <td class="py-3 px-4">
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
              <td class="py-3 px-4 text-center space-y-2">
                <button
                  class="text-indigo-600 hover:text-indigo-800 font-medium block w-full"
                  @click="viewDetails(internship.id)"
                >
                  View Details
                </button>

                <!-- 🔹 Upload Buttons -->
                <button
                  @click="uploadFile('agreement', internship.id)"
                  class="bg-indigo-100 text-indigo-700 text-sm px-3 py-1 rounded-lg hover:bg-indigo-200 transition-all w-full"
                >
                  Upload Agreement
                </button>

                <button
                  @click="uploadFile('report', internship.id)"
                  class="bg-purple-100 text-purple-700 text-sm px-3 py-1 rounded-lg hover:bg-purple-200 transition-all w-full"
                >
                  Upload Report
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const studentName = ref('John Doe')

const internships = ref([
  {
    id: 1,
    company: 'TechCorp Ltd.',
    position: 'Frontend Developer Intern',
    startDate: '2025-07-01',
    endDate: '2025-09-30',
    status: 'Approved'
  },
  {
    id: 2,
    company: 'InnovateX',
    position: 'UI/UX Design Intern',
    startDate: '2025-08-15',
    endDate: '2025-11-15',
    status: 'Pending'
  },
  {
    id: 3,
    company: 'DataMinds',
    position: 'Data Analyst Intern',
    startDate: '2025-05-01',
    endDate: '2025-07-31',
    status: 'Rejected'
  }
])

const viewDetails = (id) => {
  router.push(`/internship/${id}`)
}

const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('role')
  router.push('/login')
}

const goToAddInternship = () => {
  router.push('/internship/add')
}

const goToChangePassword = () => {
  router.push({ path: '/change-password', query: { from: 'student' } })
}

// 🔹 Mock upload logic (заменить на backend позже)
const uploadFile = (type, internshipId) => {
  const input = document.createElement('input')
  input.type = 'file'
  input.accept = '.pdf,.doc,.docx'
  input.onchange = (e) => {
    const file = e.target.files[0]
    if (!file) return
    alert(`${type === 'agreement' ? 'Agreement' : 'Report'} uploaded for internship #${internshipId}: ${file.name}`)
    // 🚀 TODO: заменить на axios.post(`/api/upload/${internshipId}`, file)
  }
  input.click()
}
</script>
