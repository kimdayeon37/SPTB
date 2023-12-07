import { jwtDecode } from 'jwt-decode';
import { defineStore } from 'pinia';
import { computed, ref } from 'vue';

interface IPayload {
  iat: number
  exp: number
}


export const useUserStore = defineStore('login', () => {
  const id = ref<string>();
  const token = ref<string>();
  const decoded = computed(() => (token.value ? jwtDecode<IPayload>(token.value) : undefined))


  const isLogin = () => {
    return id.value !== undefined;
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
    decoded,
    isLogin,
    setUsername,
    clearUsername,
    setToken,
  }
},
  {
    persist: {
      storage: sessionStorage,
    },
  }
);