import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useToggleStore = defineStore({
  id: 'toggleStore',
  state: () => ({
    networkDialogToggle: ref<boolean>(true),
  }),
  actions: {
    toggleSetting(bool?: boolean) {
      if (bool !== undefined) this.networkDialogToggle = bool
      else this.networkDialogToggle = !this.networkDialogToggle
    },
  },
})
