import { createApp } from 'vue'
import App from './App.vue'
import './style.css'
import router from './router'
import { initAuth } from './authinit' 
const app = createApp(App)
app.use(router)

initAuth()
  .catch(err => console.error('initAuth error', err))
  .finally(() => {
    app.mount('#app')  
  })
