import Axios, { type AxiosRequestConfig } from 'axios'
import { getToken, signOut } from '@/lib/auth'

export const AXIOS_INSTANCE = Axios.create({
  baseURL: import.meta.env.VITE_API_URL,
})

AXIOS_INSTANCE.interceptors.request.use((config) => {
  const token = getToken()
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

AXIOS_INSTANCE.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      signOut()
    }
    return Promise.reject(error)
  },
)

export const customInstance = <T>(config: AxiosRequestConfig): Promise<T> =>
  AXIOS_INSTANCE(config).then(({ data }) => data as T)
