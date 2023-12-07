import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useStateStore = defineStore(
  'state',
  () => {
    const state = ref<boolean>(false)
    const isRunning = ref<boolean>(false)
    return {
      state,
      isRunning,
    }
  },
  {
    persist: {
      storage: sessionStorage,
    },
  }
)
