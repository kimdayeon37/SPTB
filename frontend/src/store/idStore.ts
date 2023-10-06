import { defineStore } from 'pinia'

export const useIdStore = defineStore('clientId', {
  state: () => ({
    clientId: '',
  }),
  actions: {
    set(newId: string) {
      this.clientId = newId
    },
    get() {
      return this.clientId
    },
  },
})
