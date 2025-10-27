<script setup>
import { UserPlus, LogIn, FileEdit, Upload, ArrowRight } from "lucide-vue-next"
</script>

<template>
  <div class="bg-gray-50 text-gray-800">
    <!-- NAVBAR -->
    <nav class="bg-white shadow-sm fixed w-full top-0 left-0 z-10">
      <div class="max-w-7xl mx-auto flex justify-between items-center py-3 px-6">
        <h1 class="text-2xl font-bold text-indigo-600">Internship Portal</h1>
        <div class="space-x-4">
          <a href="#" class="text-gray-600 hover:text-indigo-600 font-medium">Login</a>
          <a
            href="#"
            class="bg-indigo-600 text-white px-4 py-2 rounded hover:bg-indigo-700 font-medium"
          >Register</a>
        </div>
      </div>
    </nav>

    <!-- HERO SECTION -->
    <section class="pt-24 pb-16 bg-gradient-to-r from-indigo-50 to-white text-center">
      <div class="max-w-3xl mx-auto px-4">
        <h2 class="text-4xl font-bold mb-4 text-indigo-700">
          Manage Your Internship Seamlessly
        </h2>
        <p class="text-gray-600 text-lg mb-8">
          A unified platform for students, companies, and guarantors to manage internship registration,
          document exchange, and progress tracking.
        </p>
        <div class="flex justify-center space-x-4">
          <a
            href="#"
            class="bg-indigo-600 text-white px-6 py-3 rounded-lg shadow hover:bg-indigo-700 font-medium"
          >
            Register as Student
          </a>
          <a
            href="#"
            class="bg-gray-200 px-6 py-3 rounded-lg shadow hover:bg-gray-300 font-medium"
          >
            Register as Company
          </a>
        </div>
      </div>
    </section>

    <!-- INFO SECTION -->
    <section class="py-16 bg-white">
      <div class="max-w-6xl mx-auto px-6 text-center">
        <h3 class="text-3xl font-semibold mb-10 text-indigo-700">How It Works</h3>
        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
          <div class="bg-indigo-50 p-6 rounded-xl shadow-sm hover:shadow-md transition">
            <h4 class="text-xl font-bold text-indigo-600 mb-3">For Students</h4>
            <p class="text-gray-600 mb-4">
              Register with your student email, create an internship, and upload required documents.
              Track progress and receive notifications about status changes.
            </p>
            <span class="text-sm text-gray-500">Role: Student</span>
          </div>

          <div class="bg-indigo-50 p-6 rounded-xl shadow-sm hover:shadow-md transition">
            <h4 class="text-xl font-bold text-indigo-600 mb-3">For Companies</h4>
            <p class="text-gray-600 mb-4">
              Accept or reject student internship requests, review agreements, and upload evaluation reports.
            </p>
            <span class="text-sm text-gray-500">Role: Company</span>
          </div>

          <div class="bg-indigo-50 p-6 rounded-xl shadow-sm hover:shadow-md transition">
            <h4 class="text-xl font-bold text-indigo-600 mb-3">For Guarantors</h4>
            <p class="text-gray-600 mb-4">
              Monitor all internships, filter by year or program, approve results, and export reports.
            </p>
            <span class="text-sm text-gray-500">Role: Guarantor</span>
          </div>
        </div>

        <!-- 👇 ДОБАВЛЕН твой красивый шаговый блок под существующими карточками -->
        <div class="mt-20 relative">
          <h3 class="text-2xl font-semibold text-indigo-700 mb-12">Student Registration Steps</h3>

          <!-- Connection Line -->
          <div
            class="absolute top-16 left-0 right-0 h-1 bg-gradient-to-r from-indigo-200 via-purple-200 to-indigo-200 mx-32"
            aria-hidden="true"
          ></div>

          <div class="grid grid-cols-1 md:grid-cols-4 gap-8 relative">
            <!-- Step 1 -->
            <div class="flex flex-col items-center text-center group relative">
              <div
                class="w-32 h-32 bg-gradient-to-br from-indigo-500 to-indigo-600 rounded-2xl flex items-center justify-center mb-6 shadow-lg group-hover:shadow-xl group-hover:scale-105 transition-all duration-300 relative z-10"
              >
                <UserPlus class="w-12 h-12 text-white" />
              </div>
              <div
                class="bg-white rounded-xl p-6 shadow-md border-2 border-indigo-100 h-full group-hover:border-indigo-300 transition-all"
              >
                <h3 class="text-gray-900 mb-3">Register as Student</h3>
                <p class="text-gray-600">
                  Fill your name, student email, and program
                </p>
              </div>
              <div
                class="absolute top-16 -right-4 text-indigo-400 hidden xl:block"
                aria-hidden="true"
              >
                <ArrowRight class="w-8 h-8" />
              </div>
            </div>

            <!-- Step 2 -->
            <div class="flex flex-col items-center text-center group relative">
              <div
                class="w-32 h-32 bg-gradient-to-br from-purple-500 to-purple-600 rounded-2xl flex items-center justify-center mb-6 shadow-lg group-hover:shadow-xl group-hover:scale-105 transition-all duration-300 relative z-10"
              >
                <LogIn class="w-12 h-12 text-white" />
              </div>
              <div
                class="bg-white rounded-xl p-6 shadow-md border-2 border-purple-100 h-full group-hover:border-purple-300 transition-all"
              >
                <h3 class="text-gray-900 mb-3">Log In</h3>
                <p class="text-gray-600">
                  Use student credentials to access your dashboard
                </p>
              </div>
              <div
                class="absolute top-16 -right-4 text-purple-400 hidden xl:block"
                aria-hidden="true"
              >
                <ArrowRight class="w-8 h-8" />
              </div>
            </div>

            <!-- Step 3 -->
            <div class="flex flex-col items-center text-center group relative">
              <div
                class="w-32 h-32 bg-gradient-to-br from-indigo-500 to-purple-500 rounded-2xl flex items-center justify-center mb-6 shadow-lg group-hover:shadow-xl group-hover:scale-105 transition-all duration-300 relative z-10"
              >
                <FileEdit class="w-12 h-12 text-white" />
              </div>
              <div
                class="bg-white rounded-xl p-6 shadow-md border-2 border-indigo-100 h-full group-hover:border-indigo-300 transition-all"
              >
                <h3 class="text-gray-900 mb-3">Create Internship</h3>
                <p class="text-gray-600">
                  Select company, enter internship details, and submit for approval
                </p>
              </div>
              <div
                class="absolute top-16 -right-4 text-indigo-400 hidden xl:block"
                aria-hidden="true"
              >
                <ArrowRight class="w-8 h-8" />
              </div>
            </div>

            <!-- Step 4 -->
            <div class="flex flex-col items-center text-center group relative">
              <div
                class="w-32 h-32 bg-gradient-to-br from-purple-600 to-indigo-600 rounded-2xl flex items-center justify-center mb-6 shadow-lg group-hover:shadow-xl group-hover:scale-105 transition-all duration-300 relative z-10"
              >
                <Upload class="w-12 h-12 text-white" />
              </div>
              <div
                class="bg-white rounded-xl p-6 shadow-md border-2 border-purple-100 h-full group-hover:border-purple-300 transition-all"
              >
                <h3 class="text-gray-900 mb-3">Upload Documents</h3>
                <p class="text-gray-600">
                  Upload signed agreement and report for verification
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- EXTERNAL SYSTEM / API INFO -->
    <section class="py-12 bg-indigo-600 text-white text-center">
      <div class="max-w-3xl mx-auto px-6">
        <h3 class="text-2xl font-semibold mb-4">External Integrations</h3>
        <p class="text-indigo-100 mb-6">
          The system supports secure API access via OAuth2 for external systems
          to update internship statuses or retrieve data.
        </p>
        <a
          href="#"
          class="bg-white text-indigo-600 px-5 py-2 rounded font-medium hover:bg-gray-100"
        >
          Learn More
        </a>
      </div>
    </section>

    <!-- FOOTER -->
    <footer class="bg-gray-900 text-gray-300 text-center py-6">
      <p>© 2025 Internship Portal. All rights reserved.</p>
    </footer>
  </div>
</template>
