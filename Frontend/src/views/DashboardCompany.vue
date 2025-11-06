<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header -->
    <header class="bg-indigo-600 text-white py-4 shadow-md">
      <div class="max-w-6xl mx-auto flex justify-between items-center px-6">
        <h1 class="text-2xl font-semibold">Company Dashboard</h1>

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

    <!-- Applications Table -->
    <div class="max-w-6xl mx-auto mt-8 bg-white rounded-xl shadow-md p-6">
      <h2 class="text-xl font-semibold text-indigo-700 mb-4">Internship Applications</h2>

      <table class="min-w-full border-collapse">
        <thead>
          <tr class="bg-indigo-50 text-indigo-700">
            <th class="py-2 px-4 text-left">Student</th>
            <th class="py-2 px-4 text-left">Program</th>
            <th class="py-2 px-4 text-left">Period</th>
            <th class="py-2 px-4 text-left">Documents</th>
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

            <!-- Upload + View Document -->
            <td class="py-2 px-4">
              <div class="flex items-center space-x-2">
                <!-- Скрытый input -->
                <input
                  type="file"
                  accept="application/pdf"
                  :id="'file-' + index"
                  class="hidden"
                  @change="handleFileUpload($event, index)"
                />

                <!-- Кастомная кнопка Upload -->
                <label
                  :for="'file-' + index"
                  class="bg-indigo-600 text-white px-3 py-1 rounded-lg text-sm cursor-pointer hover:bg-indigo-700 transition"
                >
                  Upload (PDF)
                </label>

                <!-- View -->
                <button
                  v-if="app.documentUrl"
                  @click="viewDocument(app.documentUrl)"
                  class="bg-indigo-100 text-indigo-600 px-3 py-1 rounded text-sm hover:bg-indigo-200 transition"
                >
                  View
                </button>
              </div>

              <p v-if="app.documentName" class="text-xs text-gray-500 mt-1 truncate max-w-[180px]">
                {{ app.documentName }}
              </p>
            </td>

            <!-- Status -->
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

            <!-- Actions -->
            <td class="py-2 px-4">
              <button
                class="bg-green-600 text-white px-3 py-1 rounded hover:bg-green-700 text-sm"
                @click="updateStatus(index, 'Accepted')"
              >
                Approve
              </button>
              <button
                class="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600 text-sm ml-2"
                @click="updateStatus(index, 'Rejected')"
              >
                Reject
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue"
import { useRouter } from "vue-router"

const router = useRouter()

const logout = () => {
  localStorage.removeItem("token")
  localStorage.removeItem("role")
  router.push("/login")
}

const goToChangePassword = () => {
  router.push({ path: "/change-password", query: { from: "company" } })
}

const applications = ref([
  {
    student: "John Doe",
    program: "Informatics",
    period: "Mar–Jun 2025",
    status: "Pending",
    documentUrl: null,
    documentName: null
  },
  {
    student: "Jane Smith",
    program: "Computer Science",
    period: "Apr–Jul 2025",
    status: "Pending",
    documentUrl: null,
    documentName: null
  }
])

// ✅ Загрузка документа
const handleFileUpload = (event, index) => {
  const file = event.target.files[0]
  if (!file) return

  const fileUrl = URL.createObjectURL(file)
  applications.value[index].documentUrl = fileUrl
  applications.value[index].documentName = file.name
  applications.value[index].status = "Pending"
}

// ✅ Просмотр документа
const viewDocument = (url) => {
  window.open(url, "_blank")
}

// ✅ Обновление статуса
const updateStatus = (index, newStatus) => {
  applications.value[index].status = newStatus
}
</script>
