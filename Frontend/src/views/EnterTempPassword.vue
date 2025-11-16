<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50 px-4">
    <div class="w-full max-w-md bg-white rounded-2xl shadow-lg p-8 relative">

      <!-- Back -->
      <div class="mb-4">
        <button 
          @click="goBack"
          class="inline-flex items-center text-indigo-600 hover:text-indigo-800 font-semibold"
        >
          ← Back
        </button>
      </div>

      <h1 class="text-2xl font-bold text-center text-indigo-600 mb-6">
        Enter Temporary Password
      </h1>

      <p class="text-center text-gray-600 mb-6 text-sm">
        A temporary password was sent to your email.  
        Enter it below to continue.
      </p>

      <!-- Form -->
      <form @submit.prevent="submitTempPassword" class="space-y-5">
        <div>
          <label class="block text-gray-700 mb-1">Temporary Password</label>

          <!-- 8 OTP Boxes -->
          <div class="flex justify-between gap-2">
            <input
              v-for="(box, index) in codeBoxes"
              :key="index"
              ref="inputs"
              maxlength="1"
              class="w-10 h-12 text-center text-xl border border-gray-300 rounded-lg
                     focus:ring-indigo-500 focus:border-indigo-500"
              v-model="codeBoxes[index]"
              @input="onInput(index)"
              @keydown.backspace.prevent="onBackspace(index)"
              @keydown.left.prevent="focusPrev(index)"
              @keydown.right.prevent="focusNext(index)"
              @paste="onPaste($event)"
            />
          </div>
        </div>

        <button
          type="submit"
          class="w-full bg-indigo-600 text-white font-medium py-2 rounded-lg hover:bg-indigo-700 transition"
        >
          Continue
        </button>
      </form>

      <p v-if="error" class="text-red-600 text-center mt-4">{{ error }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from '../api.js'

const router = useRouter()

// 8 ячеек
const codeBoxes = ref(Array(8).fill(""))
const inputs = ref([])
const tempPassword = ref("")
const error = ref("")

const goBack = () => router.push('/forgot-password')

// авто-фокус на первой ячейке
onMounted(() => {
  setTimeout(() => inputs.value[0]?.focus(), 50)
})

const onInput = (index) => {
  const value = codeBoxes.value[index]

  if (value.length > 1) {
    codeBoxes.value[index] = value.charAt(0)
  }

  if (value && index < 7) {
    inputs.value[index + 1].focus()
  }

  tempPassword.value = codeBoxes.value.join("")
}

const onBackspace = (index) => {
  if (!codeBoxes.value[index] && index > 0) {
    inputs.value[index - 1].focus()
  }
  codeBoxes.value[index] = ""
  tempPassword.value = codeBoxes.value.join("")
}

const focusPrev = (index) => {
  if (index > 0) inputs.value[index - 1].focus()
}

const focusNext = (index) => {
  if (index < 7) inputs.value[index + 1].focus()
}

// ---------------
//  ВСТАВКА (CTRL+V)
// ---------------
const onPaste = (event) => {
  const paste = event.clipboardData.getData("text").trim()

  if (!paste) return

  const chars = paste.slice(0, 8).split("") // максимум 8 символов

  for (let i = 0; i < 8; i++) {
    codeBoxes.value[i] = chars[i] || ""
  }

  // обновляем пароль
  tempPassword.value = codeBoxes.value.join("")

  // ставим фокус на последнюю непустую ячейку
  const lastIndex = chars.length - 1
  if (inputs.value[lastIndex]) {
    setTimeout(() => inputs.value[lastIndex].focus(), 10)
  }
}

const submitTempPassword = async () => {
  try {
    if (tempPassword.value.length !== 8) {
      error.value = "Temporary password must be 8 characters"
      return
    }

    router.push('/create-new-password')

  } catch (e) {
    error.value = "Wrong temporary password"
  }
}
</script>

<style scoped></style>
