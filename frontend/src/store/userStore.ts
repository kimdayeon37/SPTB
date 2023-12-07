import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useUserStore = defineStore('login', () => {
  const id = ref('');
  const token = ref('');

  const isLogin = () => {
    return id.value !== '';
  }

  const setUsername = (newId: string) => {
    id.value = newId;
  }

  const clearUsername = () => {
    id.value = '';
    token.value = '';
    localStorage.removeItem('login');
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