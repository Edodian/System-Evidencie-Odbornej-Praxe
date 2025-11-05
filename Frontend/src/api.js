import axios from 'axios';

axios.defaults.baseURL = 'http://localhost:8081'; // адрес Spring Boot
axios.defaults.withCredentials = true; // разрешаем куки

export default axios;
