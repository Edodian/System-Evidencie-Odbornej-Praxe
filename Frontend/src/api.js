import axios from 'axios'

axios.defaults.baseURL = 'http://localhost:8081' // имя сервиса бэка из docker-compose
axios.defaults.withCredentials = false // пока токены не используем

export default axios
