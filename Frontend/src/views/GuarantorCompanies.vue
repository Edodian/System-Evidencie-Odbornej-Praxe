<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header -->
    <header class="bg-indigo-600 text-white py-4 shadow-md">
      <div class="max-w-6xl mx-auto flex items-center justify-between px-6">
        <h1 class="text-2xl font-semibold">Companies Confirmation</h1>

        <div class="flex items-center gap-3">
          <button
            @click="goBack"
            class="bg-white text-indigo-600 font-medium px-4 py-2 rounded-lg hover:bg-indigo-100"
          >
            ← Back to Dashboard
          </button>
        </div>
      </div>
    </header>

    <!-- Filters -->
    <div class="max-w-6xl mx-auto mt-6 bg-white p-4 rounded-lg shadow-sm flex flex-col md:flex-row md:items-center md:justify-between gap-4">
      <div class="flex items-center gap-3">
        <label class="text-gray-700 font-medium">Search:</label>
        <input
          v-model="filters.name"
          type="text"
          placeholder="Company name or address..."
          class="border rounded px-3 py-2 w-72"
        />
      </div>

      <div class="flex items-center gap-3">
        <label class="text-gray-700 font-medium">Status:</label>
        <select v-model="filters.status" class="border rounded px-3 py-2">
          <option value="">All</option>
          <option>Pending</option>
          <option>Approved</option>
          <option>Rejected</option>
        </select>

        <button
          @click="clearFilters"
          class="ml-3 text-sm bg-gray-100 hover:bg-gray-200 px-3 py-2 rounded"
        >
          Clear
        </button>
      </div>
    </div>

    <!-- Table -->
    <div class="max-w-6xl mx-auto mt-6 bg-white shadow rounded-lg overflow-hidden">
      <table class="min-w-full">
        <thead class="bg-indigo-50">
          <tr>
            <th class="py-3 px-4 text-left text-sm font-medium text-gray-700">Company Name</th>
            <th class="py-3 px-4 text-left text-sm font-medium text-gray-700 hidden lg:table-cell">Address</th>
            <th class="py-3 px-4 text-left text-sm font-medium text-gray-700">Email</th>
            <th class="py-3 px-4 text-left text-sm font-medium text-gray-700">Status</th>
            <th class="py-3 px-4 text-left text-sm font-medium text-gray-700">Actions</th>
          </tr>
        </thead>

        <tbody>
          <tr
            v-for="c in filteredCompanies"
            :key="c.id"
            class="border-t hover:bg-gray-50 transition"
          >
            <td class="py-3 px-4">{{ c.name }}</td>
            <td class="py-3 px-4 hidden lg:table-cell">{{ c.address }}</td>
            <td class="py-3 px-4">{{ c.email }}</td>

            <td class="py-3 px-4">
              <span
                class="px-3 py-1 rounded-full text-sm font-medium"
                :class="{
                  'bg-yellow-100 text-yellow-700': c.status === 'Pending',
                  'bg-green-100 text-green-700': c.status === 'Approved',
                  'bg-red-100 text-red-700': c.status === 'Rejected'
                }"
              >
                {{ c.status }}
              </span>
            </td>

            <td class="py-3 px-4">
              <div class="flex items-center gap-2">
                <button
                  v-if="c.status !== 'Approved'"
                  class="bg-green-600 text-white px-3 py-1 rounded hover:bg-green-700 text-sm"
                  @click="updateStatus(c.id, 'Approved')"
                >
                  Approve
                </button>

                <button
                  v-if="c.status !== 'Rejected'"
                  class="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600 text-sm"
                  @click="updateStatus(c.id, 'Rejected')"
                >
                  Reject
                </button>

                <button
                  class="bg-gray-100 text-gray-800 px-2 py-1 rounded hover:bg-gray-200 text-sm"
                  @click="viewDetails(c)"
                >
                  Details
                </button>
              </div>
            </td>
          </tr>

          <tr v-if="filteredCompanies.length === 0">
            <td colspan="5" class="py-6 px-4 text-center text-gray-500">
              No companies found for the current filters.
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue"
import { useRouter } from "vue-router"

const router = useRouter()

const goBack = () => {
  // если нужен конкретный маршрут, поправь сюда
  router.push("/dashboard/guarantor")
}

// пример локальных данных — позже заменим на API
const companies = ref([
  { id: 1, name: "ACME Corp", address: "London, UK", email: "info@acme.com", status: "Pending" },
  { id: 2, name: "TechX", address: "Berlin, DE", email: "office@techx.io", status: "Approved" },
  { id: 3, name: "DataLab", address: "Bratislava, SK", email: "team@datalab.com", status: "Pending" }
])

const filters = ref({
  name: "",
  status: ""
})

const filteredCompanies = computed(() => {
  return companies.value.filter(c => {
    const q = filters.value.name.trim().toLowerCase()
    const matchesName = !q || c.name.toLowerCase().includes(q) || c.address.toLowerCase().includes(q)
    const matchesStatus = !filters.value.status || c.status === filters.value.status
    return matchesName && matchesStatus
  })
})

const updateStatus = (id, newStatus) => {
  const company = companies.value.find(c => c.id === id)
  if (company) company.status = newStatus
}

const viewDetails = (company) => {
  // временно — просто лог. Можно открыть модалку или страницу с подробностями
  console.log("View details for", company)
  // router.push(`/guarantor/companies/${company.id}`)  // пример
}

const clearFilters = () => {
  filters.value.name = ""
  filters.value.status = ""
}
</script>

<style scoped>
/* можно добавить дополнительные мелкие стили здесь */
</style>
