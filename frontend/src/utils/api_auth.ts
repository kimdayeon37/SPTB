import { $axios } from '@/axios'
import { useIdStore } from '@/store/idStore'
import { useUserStore } from '@/store/userStore'
import { useRouter } from 'vue-router'

export const refreshProc = async () => {
  try {
    const $userStore = useUserStore()
    const res = await $axios().post<{ token: string }>('/api/regenToken', { id: $userStore.id })

    $userStore.token = res.data.token
  } catch (e) {}
}

export const logoutUser = () => {
  const $userStore = useUserStore()
  const $router = useRouter()

  $userStore.clearUsername()
  $router.push({ path: '/Login' })
}

export const setClientId = async () => {
  try {
    const idStore = useIdStore()
    console.log(idStore.clientId)
    if (idStore.clientId == '') {
      const response = await $axios().get('/api/getClientId')
      const clientId = response.data

      idStore.clientId = clientId
      console.log('Client ID set in idStore:', clientId)
    }
  } catch (error) {
    console.error('Error fetching client ID:', error)
  }
}
