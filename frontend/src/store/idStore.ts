import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useIdStore = defineStore('clientId', () => {
  const clientId = ref<string>('')
  return {
    clientId,
  }
},
{
    persist: {
      storage: sessionStorage,
    },
  }
)
