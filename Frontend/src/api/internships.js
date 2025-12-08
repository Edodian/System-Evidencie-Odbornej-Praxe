import axios from './api'

export const getAllInternships = async () => {
  const res = await axios.get('/api/internship/show-internships')
  return res.data
}

export const changeInternshipStatus = async (id, status) => {
  await axios.post('/api/internship/change_status', { id, status })
}
