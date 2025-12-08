
import axios from './api' 
//autoauth if the token is still valid 
export async function initAuth() {
  const token = localStorage.getItem('token')
  if (!token) return
  try {
    const res = await axios.get('/api/student/profile')
    const data = res.data
    localStorage.setItem('role', data.role)
    localStorage.setItem('email', data.email)
  } catch (e) {

    console.warn('initAuth: token invalid, clearing localStorage')
    localStorage.clear()
  }
}
