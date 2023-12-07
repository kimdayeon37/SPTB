import { useUserStore } from '@/store/userStore'
import { useEventSource } from '@vueuse/core'
import { computed, ref, watch } from 'vue'

export const useSseServerTime = () => {
  const { data } = useEventSource('/api/sse/main/server-time')

  const $userState = useUserStore()

  const serverTime = ref<number>()

  watch(data, () => {
    if (data.value) {
      serverTime.value = Number(data.value)
    }
  })

  const aliveTime = computed(() => {
    if ($userState.decoded && serverTime.value) {
      const expirationTime = $userState.decoded.exp
      const alive = expirationTime - Math.floor(serverTime.value / 1000)
      return alive
    }
    return undefined
  })

  return {
    serverTime,
    aliveTime,
  }
}
