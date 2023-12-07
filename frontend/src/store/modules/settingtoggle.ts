import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useToggleStore = defineStore('toggleStore', () => {
  const networkDialogToggle = ref<boolean>(true)
  const toggle = () => (networkDialogToggle.value = !networkDialogToggle.value)

  return {
    networkDialogToggle,
    toggle,
  }
})
