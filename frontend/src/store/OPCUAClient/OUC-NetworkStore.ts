import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { OUCNetworkData } from './../../types'

export const useOUCNetworkStore = defineStore('clientId', () => {
  const networkData = ref<OUCNetworkData>()
  return {
    networkData,
  }
})
