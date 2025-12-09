<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header -->
    <header class="bg-indigo-600 text-white py-4 shadow-md">
      <div class="max-w-6xl mx-auto flex justify-between items-center px-6">
        <h1 class="text-2xl font-semibold">Student Dashboard</h1>

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
          Welcome
          <span v-if="studentName">, {{ studentName }}</span>
          <span v-else>...</span>
        </h2>

        <button
          @click="goToAddInternship"
          class="bg-indigo-600 text-white px-5 py-2 rounded-lg font-medium hover:bg-indigo-700 transition-all"
        >
          + Add Internship
        </button>
      </div>

      <!-- Internships -->
      <div class="bg-white shadow-md rounded-2xl p-6">
        <h3 class="text-xl font-semibold mb-4 text-gray-800">Your Internships</h3>

        <div v-if="loadingInternships" class="text-gray-500 text-sm mb-2">
          Loading internships...
        </div>
        <div v-else-if="!internships.length" class="text-gray-500 text-sm mb-2">
          You don't have any internships yet.
        </div>

        <table v-if="internships.length" class="w-full border-collapse text-left">
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
                    'bg-yellow-100 text-yellow-700': ['Pending', 'Registered'].includes(internship.status),
                    'bg-green-100 text-green-700': ['Approved', 'Accepted', 'Confirmed', 'Defended'].includes(internship.status),
                    'bg-red-100 text-red-700': ['Rejected'].includes(internship.status)
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

        <p v-if="internshipsError" class="text-red-600 text-sm mt-3">
          {{ internshipsError }}
        </p>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from '../api.js'

const router = useRouter()
const studentName = ref('')
const internships = ref([])
const loadingInternships = ref(false)
const internshipsError = ref('')

const fetchInternships = async (userId) => {
  if (!userId) {
    console.warn('fetchInternships: no userId')
    return
  }
  loadingInternships.value = true
  internshipsError.value = ''
  try {
    console.log('Backend link', userId)

    const res = await axios.post(
      '/api/internship/show/id/' + userId,
      {},
      { withCredentials: true }
    )

    console.log('Response from /api/internship/show/id:', res.data)
    const data = res.data

    const list = Array.isArray(data) ? data : (data ? [data] : [])

    console.log('Internships list:', list)

    internships.value = list.map((dto) => ({
      id: dto.id,
      company: dto.companyName || dto.organizationName || dto.company || 'N/A',
      position: dto.position || dto.role || '—',
      startDate: dto.startDate,
      endDate: dto.endDate,
      status: dto.status || 'Pending'
    }))
  } catch (err) {
    console.error('Failed to fetch internships:', err)
    internshipsError.value =
      err.response?.data?.message ||
      `HTTP ${err.response?.status ?? '???'}`
  } finally {
    loadingInternships.value = false
  }
}

const fetchProfile = async () => {
  try {

    const res = await axios.get('/api/student/profile')
    const data = res.data
    console.log('Student profile:', data)

    studentName.value = `${data.name} ${data.surname}`

    localStorage.setItem('email', data.email)
    localStorage.setItem('role', data.role)

    if (data.id != null) {
      localStorage.setItem('userId', String(data.id))
      await fetchInternships(data.id)
    } else {
      console.warn('no id field:', data)
    }
  } catch (err) {
    console.error('Profile fetch error:', err)
    if (err.response?.status === 401 || err.response?.status === 403) {
      localStorage.removeItem('token')
      localStorage.removeItem('role')
      router.push('/login')
    }
  }
}

const goToChangePassword = () => {
  router.push('/change-password')
}

const logout = async () => {
  try {
<<<<<<< HEAD
    await axios.post('/api/logout', {}, { withCredentials: true })
  } catch (e) {
    console.warn('logout error (ignored):', e)
  } finally {
    localStorage.clear()
    router.push('/login')
=======

    await axios.post('/api/student/logout', {})
  } catch (_) {}
  localStorage.clear()
  router.push('/login')
}

const goToAddInternship = () => router.push('/internship/add')
const goToChangePassword = () => router.push({ path: '/change-password', query: { from: 'student' } })

// === Keep internship uploads unchanged ===
const uploadFile = (type, internshipId) => {
  const input = document.createElement('input')
  input.type = 'file'
  input.accept = '.pdf,.doc,.docx'
  input.onchange = (e) => {
    const file = e.target.files[0]
    if (!file) return
    alert(`${type === 'agreement' ? 'Agreement' : 'Report'} uploaded for internship #${internshipId}: ${file.name}`)
>>>>>>> feature/tokenization
  }
}

const goToAddInternship = () => {
  router.push('/internships/new')
}

const viewDetails = (id) => {
  router.push(`/internships/${id}`)
}

const uploadFile = (type, internshipId) => {
 
  console.log('Upload file', type, 'for internship', internshipId)
}

onMounted(fetchProfile)
</script>


