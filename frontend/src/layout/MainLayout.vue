<script setup lang="ts">
import { ref, watch } from 'vue'
import SystemLog from '../components/System-Log.vue'
import { useToggleStore } from '../store/modules/settingtoggle'
import { useUserStore } from '../store/userStore'
import { useRouter } from 'vue-router'
import { useSseServerTime } from '@/utils/useSse'
import { useQuasar } from 'quasar'
import { refreshProc } from '@/utils/api_auth'

const leftDrawerOpen = ref(false)
const toggleLeftDrawer = () => {
  leftDrawerOpen.value = !leftDrawerOpen.value
}

const toggleStore = useToggleStore()
//toggleStore.toggleSetting(false)
toggleStore.networkDialogToggle = false
toggleStore.toggle()

const router = useRouter()
const userStore = useUserStore()
const $q = useQuasar()

const logoutUser = () => {
  userStore.clearUsername()
  router.push({ path: '/Login' })
}
const isUserLogin = () => {
  return userStore.isLogin()
}
const isShowExpiredDialog = ref(false)
const { aliveTime } = useSseServerTime()

watch(aliveTime, (newVal) => {
  if (newVal) {
    if (newVal <= 10 && !isShowExpiredDialog.value) {
      isShowExpiredDialog.value = true
      $q.dialog({
        title: `로그인이 ${newVal}초 후 만료됩니다. 연장하시겠습니까?`,
        message: 'CANCEL을 누르면 로그아웃 됩니다.',
        cancel: true,
        persistent: true,
      })
        .onOk(async () => {
          isUserLogin()
          await refreshProc()
          isShowExpiredDialog.value = false
        })
        .onCancel(() => {
          logoutUser()
        })
    }
  }
})
</script>
<template>
  <q-layout view="hHh lpR fFf">
    <q-header reveal bordered class="bg-main text-white">
      <q-toolbar>
        <q-btn dense flat round icon="menu" @click="toggleLeftDrawer" />

        <q-toolbar-title>
          <div class="q-ml-sm text-bold">Naonworks SPTB</div>
        </q-toolbar-title>
        <template v-if="isUserLogin()">
          <div class="q-gutter-x-md">
            <span v-if="aliveTime"> Alive time: {{ aliveTime }}</span>
            <span> {{ userStore.id }} 님 환영합니다!</span>
            <q-btn color="secondary"><a href="javascript:;" @click="logoutUser" style="text-decoration: none; color: white">Logout</a></q-btn>
          </div>
        </template>
      </q-toolbar>
    </q-header>

    <q-drawer show-if-above v-model="leftDrawerOpen" side="left" bordered class="sidebar" :width="220">
      <q-list>
        <template v-if="!isUserLogin()">
          <q-item to="/Login" clickable v-ripple>
            <q-item-section avatar>
              <q-icon name="login" />
            </q-item-section>
            <q-item-section> Log In </q-item-section>
          </q-item>
        </template>
        <template v-if="isUserLogin()">
          <q-item to="/Home" clickable v-ripple>
            <q-item-section avatar>
              <q-icon name="home" />
            </q-item-section>
            <q-item-section> Home </q-item-section>
          </q-item>
          <q-item to="/Log" clickable v-ripple>
            <q-item-section avatar>
              <q-icon name="search" />
            </q-item-section>
            <q-item-section> Log Search </q-item-section>
          </q-item>
          <q-item to="/Setting" clickable v-ripple>
            <q-item-section avatar>
              <q-icon name="settings" />
            </q-item-section>
            <q-item-section> Setting! </q-item-section>
          </q-item>
          <q-expansion-item expand-separator label="Modbus" default-opened :content-inset-level="0.5">
            <q-item to="/Modbus/MasterEthernet"><q-item-section @click="toggleStore.toggle()">Master Ethernet</q-item-section></q-item>
            <q-item to="/Modbus/SlaveEthernet"><q-item-section @click="toggleStore.toggle()">Slave Ethernet</q-item-section></q-item>
          </q-expansion-item>
          <q-expansion-item :content-inset-level="0.5" expand-separator label="OPC-UA" default-opened>
            <q-item to="/OPCUA/Client"><q-item-section>Client</q-item-section></q-item>
            <q-item to="/OPCUA/Server"><q-item-section>Server</q-item-section></q-item>
          </q-expansion-item>
        </template>
      </q-list>
    </q-drawer>

    <q-page-container>
      <q-page>
        <router-view style="min-height: inherit" />
      </q-page>
    </q-page-container>

    <q-footer reveal bordered class="bg-grey-8 text-white">
      <q-toolbar>
        <div class="system-log-container row">
          <SystemLog />
        </div>
      </q-toolbar>
    </q-footer>
  </q-layout>
</template>
<style>
.sidebar {
  background: #f5f5f5;
  overflow: hidden;
}

.text-gray {
  color: gray !important;
}

.q-toolbar {
  padding: 0px !important;
}

.table-container {
  background: #fafafa;
  position: relative;
}

.table {
  position: absolute;
  height: 100%;
  width: 100%;
}

.table .q-table__top,
.table .q-table__bottom,
.table thead tr:first-child th

/* bg color is important for th; just specify one */ {
  background-color: #ffffff;
}

.table thead tr th {
  position: sticky;
  z-index: 1;
}

.table thead tr:first-child th {
  top: 0;
}

.system-log-container {
  height: 22vh;
  width: 100%;
}
</style>
virtual-scroll
