import type { RouteRecordRaw } from 'vue-router'
import MainLayout from '../layout/MainLayout.vue'
import HomeView from '../views/HomeView.vue'
import LogView from '../views/LogView.vue'
import LoginView from '../views/LoginView.vue'
import MMEView from '../views/ModbusMasterEthernetView.vue'
import MSEView from '../views/ModbusSlaveEthernetView.vue'
import OCView from '../views/OPCUAClientView.vue'
import OSView from '../views/OPCUAServerView.vue'
import SignupView from '../views/SignupView.vue'
import SettingView from '../views/SettingView.vue'
import BlockedView from '@/views/BlockedView.vue'
import SmtpView from '@/views/SmtpView.vue'
const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'login',
    redirect: '/Login',
    component: MainLayout,

    children: [
      {
        path: '/Login',
        component: LoginView,
        name: 'login',
      },
      {
        path: '/Signup',
        component: SignupView,
        name: 'signup',
      },
      {
        path: '/Home',
        component: HomeView,
        name: 'home',
      },
      {
        path: '/Modbus/MasterEthernet',
        component: MMEView,
        name: 'MasterEthernet',
      },

      {
        path: '/Modbus/SlaveEthernet',
        component: MSEView,
        name: 'SlaveEthernet',
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
      {
        path: '/Setting',
        component: SettingView,
      },
      {
        path:'/Smtp',
        component: SmtpView,
      }
    ],
  },
  {
    path: '/Blocked',
    component: BlockedView,
  },
]

export default routes
