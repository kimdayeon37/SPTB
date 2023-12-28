import { $axios } from '@/axios'
import type { NavigationGuardNext, RouteLocationNormalized } from 'vue-router'
import { useStateStore } from '@/store/stateStore'
import { useUserStore } from '@/store/userStore'

export const simulatorHandler = async () => {
  console.log('simulatorHandler')
  const stateStore = useStateStore()
  console.log(stateStore.state)

  if (stateStore.state) {
    alert('시뮬레이터 종료 후 이동해 주세요')

    return false
  }

  return true
}

export const blockedIpHandler = async (_to: RouteLocationNormalized, _from: RouteLocationNormalized, next: NavigationGuardNext) => {
  try {
    const response = await $axios().get('/api/checkIp')
    if (response.data.result) {
      return true
    } else {
      next('/Blocked')
      return false
    }
  } catch (error) {
    console.error('Error checking IP : ', error)
    next('/Blocked')
    // 수정해야됨 false로
    return false
  }
}

export const loginExistHandler = async (_to: RouteLocationNormalized, _from: RouteLocationNormalized, next: NavigationGuardNext) => {
  try {
    const userStore = useUserStore()
    if (userStore.token == undefined) {
      alert('로그인을 해주세요')
      next('/login')
      return false
    }
    return true // 계속 다음 페이지로 이동
  } catch (error) {
    console.error('Error fetching client ID:', error)
    // 오류 발생 시 어떤 처리를 하거나 다른 경로로 리다이렉트할 수 있습니다.
    return false
  }
}

export const loginAccecptHandler = async (_to: RouteLocationNormalized, _from: RouteLocationNormalized, next: NavigationGuardNext) => {
  console.log('loginAccecptHandler')
  try {
    const userStore = useUserStore()
    console.log(userStore.token)
    if (userStore.token) {
      next('/home')
      return false
    }
    return true // 계속 다음 페이지로 이동
  } catch (error) {
    console.error('Error fetching client ID:', error)
    // 오류 발생 시 어떤 처리를 하거나 다른 경로로 리다이렉트할 수 있습니다.
    return false
  }
}
