import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useUserStore = defineStore('login', () => {
  const id = ref<string>();
  const token = ref<string>();

  const isLogin = () => {
    return id.value !== undefined;
  }

  const setUsername = (newId: string) => {
    id.value = newId;
  }

  const clearUsername = () => {
    id.value = undefined;
    token.value = undefined;
  }

  const setToken = (newToken: string) => {
    token.value = newToken;
  }

  return {
    id,
    token,
    isLogin,
    setUsername,
    clearUsername,
    setToken,
  }
},
{ persist: true }
);