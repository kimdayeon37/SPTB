import type { OUSNetworkData } from './../../types'
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useOUSNetworkStore = defineStore('networkData', () => {
  const networkData = ref<OUSNetworkData>()
  const checkData = (): boolean => {
    if (networkData.value?.certFile && networkData.value?.ip && networkData.value.keyFile && networkData.value.port && networkData.value.users) return true
    else return false
  }
  return {
    networkData,
    checkData,
  }
})
