<template>
  <div class="p-8">
    <h1 class="text-3xl font-bold text-indigo-700 mb-6">Guarantor Dashboard</h1>
    <p class="text-gray-600 mb-4">Здесь гарант сможет видеть все практики, фильтровать и менять статусы.</p>
    <!-- Пример таблицы с практиками -->
    <table class="min-w-full bg-white shadow rounded">
      <thead>
        <tr class="bg-indigo-50">
          <th class="py-2 px-4 text-left">Student</th>
          <th class="py-2 px-4 text-left">Company</th>
          <th class="py-2 px-4 text-left">Status</th>
          <th class="py-2 px-4 text-left">Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr class="border-t">
          <td class="py-2 px-4">John Doe</td>
          <td class="py-2 px-4">ACME Corp</td>
          <td class="py-2 px-4">Confirmed</td>
          <td class="py-2 px-4">
            <button class="bg-indigo-600 text-white px-3 py-1 rounded hover:bg-indigo-700">Approve</button>
            <button class="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600 ml-2">Reject</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
// Пока без логики, позже подключим API
</script>
