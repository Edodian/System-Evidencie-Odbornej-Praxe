import { createApp } from 'vue'
import App from './App.vue'
import { router } from './router'
import './style.css'
import { initAuth } from './authinit'
createApp(App).use(router).mount('#app')

initAuth().finally(() => {
  app.mount('#app')
})