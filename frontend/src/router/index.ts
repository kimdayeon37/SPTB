import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../layout/MainLayout.vue'
import HomeView from '../views/HomeView.vue'
import LogView from '../views/LogView.vue'
import MMEView from '../views/ModbusMasterEthernetView.vue'
import MMSView from '../views/ModbusMasterSerialView.vue'
import MSEView from '../views/ModbusSlaveEthernetView.vue'
import MSSView from '../views/ModbusSlaveSerialView.vue'
import OCView from '../views/OPCUAClientView.vue'
import OSView from '../views/OPCUAServerView.vue'

import axios from 'axios'
import { useIdStore } from '../store/idStore'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: MainLayout,
      children: [
        {
          path: '/',
          component: HomeView,
        },
        {
          path: '/Modbus/MasterEthernet',
          component: MMEView,
          name: 'MasterEthernet',
          beforeEnter: async (to, from, next) => {
            // 블록킹 코드 실행
            try {
              const idStore = useIdStore()
              const response = await axios.get('/api/modbus/getClientId')
              const clientId = response.data
              idStore.set(clientId)
              console.log('Client ID set in idStore:', clientId)
              next() // 계속 다음 페이지로 이동
            } catch (error) {
              console.error('Error fetching client ID:', error)
              // 오류 발생 시 어떤 처리를 하거나 다른 경로로 리다이렉트할 수 있습니다.
            }
          },
        },

        {
          path: '/Modbus/SlaveEthernet',
          component: MSEView,
          name: 'SlaveEthernet',
          beforeEnter: async (to, from, next) => {
            // 블록킹 코드 실행
            try {
              const idStore = useIdStore()
              const response = await axios.get('/api/modbus/getClientId')
              const clientId = response.data
              idStore.set(clientId)
              console.log('Client ID set in idStore:', clientId)
              next() // 계속 다음 페이지로 이동
            } catch (error) {
              console.error('Error fetching client ID:', error)
              // 오류 발생 시 어떤 처리를 하거나 다른 경로로 리다이렉트할 수 있습니다.
            }
          },
        },
        {
          path: '/Modbus/MasterSerial',
          component: MMSView,
          name: 'MasterSerial',
        },
        {
          path: '/Modbus/SlaveSerial',
          component: MSSView,
          name: 'SlaveSerial',
        },
        {
          path: '/OPCUA/Client',
          component: OCView,
        },
        {
          path: '/OPCUA/Server',
          component: OSView,
        },
        {
          path: '/Log',
          component: LogView,
        },
      ],
    },
  ],
})

export default router
