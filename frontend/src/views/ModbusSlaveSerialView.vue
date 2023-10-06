<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import MSEMemory from '../components/MSE-Memory.vue'
import TransLog from '../components/Trans-Log.vue'
import { useIdStore } from '../store/idStore'

import axios from 'axios'
import { useToggleStore } from '../store/modules/settingtoggle'
const idStore = useIdStore()

// 통신 dialog values
const selectedProtocol = ref<'TCP' | 'RTU' | 'ASCII'>('TCP')
const protocolOptions = ['TCP', 'RTU', 'ASCII']

const inputedSlaveId = ref<number>()
const inputedCOMPort = ref<number>()
const inputedBaudrate = ref<number>()
const inputedDataBit = ref<number>()
const inputedStopBit = ref<number>()
const selectedParity = ref<'None' | 'Odd' | 'Even'>('None')
const parityOptions = ['None', 'Odd', 'Even']

const isRunning = ref<boolean>(false)
const setRunningState = (bool: boolean) => {
  isRunning.value = bool
  console.log(isRunning.value)
  if (!isRunning.value) {
    axios
      .post('/api/modbus/exit', {
        id: idStore.get(),
      })
      .catch((err) => {
        console.log(err)
      })
  }
}
type NetworkData = {
  protocol?: string
  slaveId?: number
  comPort?: number
  baudrate?: number
  dataBit?: number
  stopBit?: number
  parity?: 'None' | 'Odd' | 'Even'
}
const slaveSerialData = ref<NetworkData>({})

const toggleStore = useToggleStore()
const settingToggleHandler = () => {
  toggleStore.toggleSetting()
}

// Log
const viewLogToggle = ref<boolean>(false)

const viewLogToggleHandler = (bool: boolean) => {
  viewLogToggle.value = bool
}

const setNetwork = () => {
  const data = {
    protocol: selectedProtocol.value,
    slaveId: inputedSlaveId.value,
    comPort: inputedCOMPort.value,
    baudrate: inputedBaudrate.value,
    dataBit: inputedDataBit.value,
    stopBit: inputedStopBit.value,
    parity: selectedParity.value,
  }
  slaveSerialData.value = data

  // Check if currentData is not an empty object
  if (Object.keys(slaveSerialData.value).length > 0) {
    try {
      // Retrieve the existing array from localStorage
      const savedDataJSON = localStorage.getItem('slaveSerialData')
      let savedData = typeof savedDataJSON === 'string' ? JSON.parse(savedDataJSON) : []

      // Add the new data to the existing array
      savedData.unshift(slaveSerialData.value)

      // Save the updated array back to localStorage
      localStorage.setItem('slaveSerialData', JSON.stringify(savedData))
    } catch (error) {
      // Handle any JSON parsing or storage errors
      console.error('Error saving data to localStorage:', error)
    }
  }

  settingToggleHandler()
}

const route = useRoute()
onMounted(() => {
  const querySelectedData = route.query.selectedData as string | undefined

  if (querySelectedData) {
    // query 파라미터가 존재하면 파싱하여 slaveSerialData.value에 할당
    slaveSerialData.value = JSON.parse(querySelectedData) as NetworkData
    console.log(slaveSerialData.value)
  } else {
    // query 파라미터가 없을 경우 기본값으로 설정
    slaveSerialData.value = {
      protocol: 'TCP',
      slaveId: 0,
      comPort: 0,
      baudrate: 0,
      dataBit: 0,
      stopBit: 0,
      parity: 'None',
    }
  }
  axios
    .post('/api/modbus/id', {
      id: idStore.get(),
    })
    .catch((err) => {
      console.log(err)
    })
})
</script>
<template>
  <div class="column">
    <div class="title flex items-center q-pl-md">
      <div>Modbus > <strong>Slave Serial</strong></div>
    </div>
    <div class="menu-bar row items-center">
      <q-btn :outline="!toggleStore.networkDialogToggle" rounded size="md" padding="2px 12px" color="main" class="setting-btn q-mx-sm" @click="settingToggleHandler">
        통신 설정
      </q-btn>
      <q-btn :outline="viewLogToggle" rounded size="md" padding="2px 12px" color="main" class="setting-btn q-mx-sm" @click="viewLogToggleHandler(false)"> 메모리 </q-btn>
      <q-btn :outline="!viewLogToggle" rounded size="md" padding="2px 12px" color="main" class="setting-btn q-mx-sm" @click="viewLogToggleHandler(true)"> 로그 </q-btn>
    </div>

    <div v-show="viewLogToggle" class="col column"><TransLog /></div>
    <div v-show="!viewLogToggle" class="col setting-area row">
      <MSEMemory :networkData="slaveSerialData" :viewLog="viewLogToggleHandler" :isRunning="isRunning" @setRunningState="setRunningState" />
    </div>
    <q-dialog v-show="toggleStore.networkDialogToggle" v-model="toggleStore.networkDialogToggle" persistent>
      <q-card class="q-pa-md dialog-box">
        <q-form @submit="setNetwork">
          <div class="row height justify-center text-h6 text-weight-bold">Slave 통신 설정</div>
          <div class="row height">
            <div class="col-6 flex items-center">Protocol</div>
            <q-select outlined v-model="selectedProtocol" :options="protocolOptions" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
          </div>
          <div class="row height">
            <div class="col-6 height flex items-center">Slave ID</div>
            <q-input
              outlined
              class="col-6"
              v-model="inputedSlaveId"
              dense
              label="1 ~ 10"
              :rules="[(val) => !!val || '* Required', (val) => (1 <= val && val <= 10) || 'Please check range']"
            />
          </div>
          <div class="row height">
            <div class="col-6 height flex items-center">COM Port</div>
            <q-input
              outlined
              v-model="inputedCOMPort"
              dense
              class="col-6"
              label="1000 ~ 3000"
              :rules="[(val) => !!val || '* Required', (val) => (1000 <= val && val <= 3000) || 'Please check range']"
            />
          </div>
          <div class="row height">
            <div class="col-6 height flex items-center">Baudrate</div>
            <q-input
              outlined
              v-model="inputedBaudrate"
              dense
              class="col-6"
              label="1000 ~ 3000"
              :rules="[(val) => !!val || '* Required', (val) => (1000 <= val && val <= 3000) || 'Please check range']"
            />
          </div>
          <div class="row height">
            <div class="col-6 height flex items-center">Data Bit</div>
            <q-input
              outlined
              v-model="inputedDataBit"
              dense
              class="col-6"
              label="1000 ~ 3000"
              :rules="[(val) => !!val || '* Required', (val) => (1000 <= val && val <= 3000) || 'Please check range']"
            />
          </div>
          <div class="row height">
            <div class="col-6 height flex items-center">Stop Bit</div>
            <q-input
              outlined
              v-model="inputedStopBit"
              dense
              class="col-6"
              label="1000 ~ 3000"
              :rules="[(val) => !!val || '* Required', (val) => (1000 <= val && val <= 3000) || 'Please check range']"
            />
          </div>
          <div class="row height">
            <div class="col-6 flex items-center">Parity</div>
            <q-select outlined v-model="selectedParity" :options="parityOptions" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
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
