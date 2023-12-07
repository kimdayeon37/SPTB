import type { AxiosError, AxiosInstance, AxiosResponse, InternalAxiosRequestConfig } from 'axios'
import axios from 'axios'
import { useUserStore } from '@/store/userStore'
import { useRouter } from 'vue-router'
const router = useRouter()

// ====================================================================
interface IAxiosErrorLog {
  code: string
  message: string
  name: string
  request?: unknown
  response?: unknown
}

export const $axios = () => {
  const instance = axios.create()
  setPreRequest(instance)
  setPostRequest(instance)
  return instance
}

const setPreRequest = (instance: AxiosInstance) => {
  instance.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
      const $authState = useUserStore()

      config.headers = config.headers ?? {}

      if ($authState.token !== undefined) config.headers.Authorization = 'Bearer ' + $authState.token

      console.log(`==================== axios request, url : ${config.url}`)
      console.log(config)

      return config
    },
    (e) => {
      if (axios.isAxiosError(e)) {
        const err = e as AxiosError

        console.error(`==================== axios request error, url : ${err.config?.url}`)
        console.error(e as IAxiosErrorLog)
      } else {
        console.error('==================== request error')
        console.error(e)
      }

      return Promise.reject(e)
    }
  )
}

const setPostRequest = (instance: AxiosInstance) => {
  instance.interceptors.response.use(
    (response: AxiosResponse) => {
      console.log(`==================== axios response, url : ${response.config.url}`)
      console.log(response)

      return response
    },
    (e) => {
      if (axios.isAxiosError(e)) {
        const err = e as AxiosError
        router.push({ path: '/Login' })
        console.error(`==================== axios response error, url : ${err.config?.url}`)
        console.error(e as IAxiosErrorLog)
      } else {
        router.push({ path: '/Login' })
        console.error('==================== response error')
        console.error(e)
      }

      return Promise.reject(e)
    }
  )
}
