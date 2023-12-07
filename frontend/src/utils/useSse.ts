import { useUserStore } from '@/store/userStore'
import { computed, onUnmounted, ref, watch } from 'vue'
import queryString from 'query-string'
import { useEventSource } from '@vueuse/core'

export const dataByTarget = (target: string, res: string) => {
  const json = JSON.parse(res)
  if (json['target'] === target) return json['data']
  else return undefined
}

export const useSse = (url: string, extraParams: Record<string, any> = {}) => {
  const $userState = useUserStore()

  const params = computed(() => {
    const res = { Authorization: 'Bearer ' + $userState.token }

    return { ...res, ...extraParams }
  })
  const sseUrl = computed(() => `${url}?${queryString.stringify(params.value)}`)

  const { status, data, error, close } = useEventSource(sseUrl.value)

  watch(status, () => {
    if (status.value === 'CLOSED') close()
  })

  onUnmounted(() => close())

  return {
    status,
    data,
    error,
    close,
  }
}

export const useSseServerTime = () => {
  const { data } = useSse('/api/sse/main/server-time')

  const $userState = useUserStore()

  const serverTime = ref(undefined)

  watch(data, () => {
    if (data.value) {
      const serverTimeRes = dataByTarget('serverTime', data.value)
      if (serverTimeRes) serverTime.value = serverTimeRes
    }
  })

  const aliveTime = computed(() => {
    if ($userState.decoded && serverTime.value) {
      const expirationTime = $userState.decoded.exp
      const currentTime = new Date(serverTime.value)
      const alive = expirationTime - currentTime.getTime()

      return alive
    }
    return undefined
  })

  return {
    serverTime,
    aliveTime,
  }
}
