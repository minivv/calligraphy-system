import axios from 'axios'
import { Message } from 'element-ui'
import router from '../router'

const request = axios.create({
  baseURL: '',
  timeout: 30000
})

request.interceptors.request.use(config => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  if (user.token) {
    config.headers['token'] = user.token
  }
  return config
})

request.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code === 401) {
      Message.error('登录已过期，请重新登录')
      localStorage.removeItem('user')
      router.push('/login')
      return Promise.reject(new Error(res.msg))
    }
    if (res.code !== 200) {
      Message.error(res.msg || '操作失败')
      return Promise.reject(new Error(res.msg))
    }
    return res
  },
  error => {
    Message.error('网络错误')
    return Promise.reject(error)
  }
)

export default request
