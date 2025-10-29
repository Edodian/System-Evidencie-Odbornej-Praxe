<template>
  <div class="text-center mt-20">
    <h1 class="text-2xl font-bold text-indigo-600">Login Page</h1>
    <p class="text-gray-600 mt-4">Form will be here.</p>

    <!-- Кнопка для гарантора, пока можно закомментировать или оставить -->
    <router-link v-if="role === 'guarantor'" to="/dashboard/guarantor">
      Go to Dashboard
    </router-link>
  </div>
</template>

<script setup>
// Логика компонента будет тут
</script>

<style scoped>
/* Стили, если нужны */
</style>



