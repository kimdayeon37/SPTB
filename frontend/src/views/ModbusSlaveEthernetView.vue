<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useToggleStore } from '../store/modules/settingtoggle'
import MSEMemory from '../components/MSE-Memory.vue'
import TransLog from '../components/Trans-Log.vue'
import { $axios } from '@/axios/index'
import { useRoute } from 'vue-router'
import { useIdStore } from '../store/idStore'
const idStore = useIdStore()
// 통신 dialog values
const protocolOptions = ['TCP', 'RTU', 'ASCII']

const isRunning = ref<boolean>(false)
const setRunningState = (bool: boolean) => {
  isRunning.value = bool
  console.log(isRunning.value)
  if (!isRunning.value) {
    $axios()
      .post('/api/exit', {
        id: idStore.clientId,
      })
      .catch((err) => {
        console.log(err)
      })
  }
}
type NetworkSlaveData = {
  protocol?: string
  port?: number
  maxSessionCount?: number
  slaveId?: number
  waitTimeout?: number
}
const slaveEthernetData = ref<NetworkSlaveData>({})

const toggleStore = useToggleStore()
//toggleStore.toggleSetting(false)
toggleStore.networkDialogToggle = false
toggleStore.toggle()

// Log
const viewLogToggle = ref<boolean>(false)

const viewLogToggleHandler = (bool: boolean) => {
  viewLogToggle.value = bool
}

const setNetwork = () => {
  // Check if currentData is not an empty object
  if (Object.keys(slaveEthernetData.value).length > 0) {
    try {
      // Retrieve the existing array from localStorage
      const savedDataJSON = localStorage.getItem('slaveEthernetData')
      let savedData = typeof savedDataJSON === 'string' ? JSON.parse(savedDataJSON) : []

      // Add the new data to the existing array
      savedData.unshift(slaveEthernetData.value)

      // Save the updated array back to localStorage
      localStorage.setItem('slaveEthernetData', JSON.stringify(savedData))
    } catch (error) {
      // Handle any JSON parsing or storage errors
      console.error('Error saving data to localStorage:', error)
    }
  }

  toggleStore.toggle()
}

const route = useRoute()
onMounted(() => {
  const querySelectedData = route.query.selectedData as string | undefined

  if (querySelectedData) {
    // query 파라미터가 존재하면 파싱하여 slaveEthernetData.value에 할당
    slaveEthernetData.value = JSON.parse(querySelectedData) as NetworkSlaveData
    console.log(slaveEthernetData.value)
  } else {
    // query 파라미터가 없을 경우
  }
  $axios()
    .post('/api/id', {
      id: idStore.clientId,
    })
    .catch((err) => {
      console.log(err)
    })
})
onUnmounted(() => {
  if (isRunning.value)
    $axios()
      .post('/api/exit', {
        id: idStore.clientId,
      })
      .catch((err) => {
        console.log(err)
      })
})
</script>
<template>
  <div class="column">
    <div class="title flex items-center q-pl-md">
      <div>Modbus > <strong>Slave Ethernet</strong></div>
    </div>
    <div class="menu-bar row items-center">
      <q-btn :outline="!toggleStore.networkDialogToggle" rounded size="md" padding="2px 12px" color="main" class="setting-btn q-mx-sm" @click="toggleStore.toggle()">
        통신 설정
      </q-btn>
      <q-btn :outline="viewLogToggle" rounded size="md" padding="2px 12px" color="main" class="setting-btn q-mx-sm" @click="viewLogToggleHandler(false)"> 메모리 </q-btn>
      <q-btn :outline="!viewLogToggle" rounded size="md" padding="2px 12px" color="main" class="setting-btn q-mx-sm" @click="viewLogToggleHandler(true)"> 로그 </q-btn>
    </div>

    <div v-show="viewLogToggle" class="col column">
      <TransLog />
    </div>
    <div v-show="!viewLogToggle" class="col setting-area row">
      <MSEMemory :networkData="slaveEthernetData" :viewLog="viewLogToggleHandler" :isRunning="isRunning" @setRunningState="setRunningState" />
    </div>
    <q-dialog v-model="toggleStore.networkDialogToggle" persistent>
      <q-card class="q-pa-md dialog-box">
        <q-form @submit="setNetwork">
          <div class="row height justify-center text-h6 text-weight-bold">Slave 통신 설정</div>
          <div class="row height">
            <div class="col-6 flex items-center">Protocol</div>
            <q-select outlined v-model="slaveEthernetData.protocol" :options="protocolOptions" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
          </div>
          <div class="row height">
            <div class="col-6 height flex items-center">Port</div>
            <q-input
              outlined
              v-model="slaveEthernetData.port"
              mask="#####"
              dense
              label="0 ~ 65535"
              class="col-6"
              :rules="[(val) => !!val || '* Required', (val) => (0 <= val && val <= 65535) || 'Please check range']"
            />
          </div>
          <div class="row height">
            <div class="col-6 height flex items-center">Max Session Count</div>
            <q-input
              outlined
              class="col-6"
              v-model="slaveEthernetData.maxSessionCount"
              dense
              label="10 ~ 100"
              :rules="[(val) => !!val || '* Required', (val) => (10 <= val && val <= 100) || 'Please check range']"
            />
          </div>
          <div class="row height">
            <div class="col-6 height flex items-center">Slave ID</div>
            <q-input
              outlined
              class="col-6"
              v-model="slaveEthernetData.slaveId"
              dense
              label="1 ~ 10"
              :rules="[(val) => !!val || '* Required', (val) => (1 <= val && val <= 10) || 'Please check range']"
            />
          </div>
          <div class="row height">
            <div class="col-6 height flex items-center">Timeout</div>
            <q-input
              outlined
              v-model="slaveEthernetData.waitTimeout"
              dense
              class="col-6"
              label="1000 ~ 3000"
              :rules="[(val) => !!val || '* Required', (val) => (1000 <= val && val <= 3000) || 'Please check range']"
            />
          </div>
          <div class="row justify-evenly items-center">
            <q-btn label="적용" type="submit" color="main" padding="xs lg"></q-btn>
            <q-btn label="취소" flat padding="xs lg" color="red" v-close-popup></q-btn>
          </div>
        </q-form>
      </q-card>
    </q-dialog>
  </div>
</template>
<style></style>
