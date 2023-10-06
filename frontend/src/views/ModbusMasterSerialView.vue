<script setup lang="ts">
import axios from 'axios'
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import ModbusMasterReader from '../components/MME-Reader.vue'
import ModbusMasterSerialWriter from '../components/MMS-Writer.vue'
import LiveLog from '../components/Trans-Log.vue'
import { useIdStore } from '../store/idStore'
import { useToggleStore } from '../store/modules/settingtoggle'
import type { MasterSerialNetworkData } from '../types'
const idStore = useIdStore()
// 통신 dialog values
// const selectedProtocol = ref<"RTU" | "ASCII">("RTU");
const protocolOptions = ['RTU', 'ASCII']
// const inputedComPort = ref<number>();
// const inputedBaudrate = ref<number>();
// const inputedDataBit = ref<number>();
// const inputedStopBit = ref<number>();
// const selectedParity = ref<"None" | "Odd" | "Even">("None");
const parityOptions = ['None', 'Odd', 'Even']
// const inputedTD = ref<number>();
// const inputedTO = ref<number>();
const inputedNetworkData = ref<MasterSerialNetworkData>({})

const masterSerialData = ref<MasterSerialNetworkData>({})
// const settingToggleHandler = () => {
//   settingToggle.value = !settingToggle.value;
// };

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
  const data: MasterSerialNetworkData = inputedNetworkData.value
  masterSerialData.value = data
  // Check if masterSerialData.value is not an empty object
  if (Object.keys(masterSerialData.value).length > 0) {
    try {
      // Retrieve the existing array from localStorage
      const savedDataJSON = localStorage.getItem('masterSerialData')
      let savedData = typeof savedDataJSON === 'string' ? JSON.parse(savedDataJSON) : []
      // Add the new data to the existing array
      savedData.unshift(masterSerialData.value)

      // Save the updated array back to localStorage
      localStorage.setItem('masterSerialData', JSON.stringify(savedData))
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
    // query 파라미터가 존재하면 파싱하여 salveEthernetData.value에 할당
    masterSerialData.value = JSON.parse(querySelectedData) as MasterSerialNetworkData
    console.log(masterSerialData.value)
  } else {
    // query 파라미터가 없을 경우 기본값으로 설정
    masterSerialData.value = {
      protocol: 'RTU',
      comPort: 0,
      baudrate: 0,
      dataBit: 0,
      stopBit: 0,
      parity: 'None',
      transactionDelay: 0,
      timeout: 0,
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
      <div>Modbus > <strong>Master Serial</strong></div>
    </div>
    <div class="menu-bar row items-center">
      <q-btn :outline="!toggleStore.networkDialogToggle" rounded size="md" padding="2px 12px" color="main" class="setting-btn q-mx-sm" @click="settingToggleHandler">
        통신 설정
      </q-btn>
      <q-btn :outline="viewLogToggle" rounded size="md" padding="2px 12px" color="main" class="setting-btn q-mx-sm" @click="viewLogToggleHandler(false)"> 메세지 </q-btn>
      <q-btn :outline="!viewLogToggle" rounded size="md" padding="2px 12px" color="main" class="setting-btn q-mx-sm" @click="viewLogToggleHandler(true)"> 로그 </q-btn>
    </div>
    <div v-show="viewLogToggle" class="col column"><LiveLog /></div>
    <div v-show="!viewLogToggle" class="col setting-area row">
      <ModbusMasterReader :networkData="inputedNetworkData" :viewLog="viewLogToggleHandler" />
      <ModbusMasterSerialWriter :networkData="inputedNetworkData" :viewLog="viewLogToggleHandler" />
    </div>
    <q-dialog v-show="toggleStore.networkDialogToggle" v-model="toggleStore.networkDialogToggle" persistent>
      <q-card class="q-pa-md dialog-box">
        <q-form @submit="setNetwork">
          <div class="row height justify-center text-h6 text-weight-bold">Master 통신 설정</div>
          <div class="row height">
            <div class="col-6 flex items-center">Protocol</div>
            <q-select outlined v-model="inputedNetworkData.protocol" :options="protocolOptions" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
          </div>
          <div class="row height">
            <div class="col-6 height flex items-center">COM Port</div>
            <q-input outlined v-model="inputedNetworkData.comPort" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
          </div>
          <div class="row height">
            <div class="col-6 height flex items-center">Baudrate</div>
            <q-input
              outlined
              v-model="inputedNetworkData.baudrate"
              mask="#####"
              dense
              label="0 ~ 65535"
              class="col-6"
              :rules="[(val) => !!val || '* Required', (val) => (0 <= val && val <= 65535) || 'Please check range']"
            />
          </div>
          <div class="row height">
            <div class="col-6 height flex items-center">Data Bit</div>
            <q-input
              outlined
              class="col-6"
              v-model="inputedNetworkData.dataBit"
              dense
              label="100 ~ 2000"
              :rules="[(val) => !!val || '* Required', (val) => (100 <= val && val <= 2000) || 'Please check range']"
            />
          </div>
          <div class="row height">
            <div class="col-6 height flex items-center">Stop Bit</div>
            <q-input
              outlined
              v-model="inputedNetworkData.stopBit"
              dense
              class="col-6"
              label="5 ~ 30"
              :rules="[(val) => !!val || '* Required', (val) => (5 <= val && val <= 30) || 'Please check range']"
            />
          </div>
          <div class="row height">
            <div class="col-6 flex items-center">Parity</div>
            <q-select outlined v-model="inputedNetworkData.parity" :options="parityOptions" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
          </div>
          <div class="row height">
            <div class="col-6 height flex items-center">transaction Delay</div>
            <q-input
              outlined
              class="col-6"
              v-model="inputedNetworkData.transactionDelay"
              dense
              label="100 ~ 2000"
              :rules="[(val) => !!val || '* Required', (val) => (100 <= val && val <= 2000) || 'Please check range']"
            />
          </div>
          <div class="row height">
            <div class="col-6 height flex items-center">Timeout(s)</div>
            <q-input
              outlined
              v-model="inputedNetworkData.timeout"
              dense
              class="col-6"
              label="5 ~ 30"
              :rules="[(val) => !!val || '* Required', (val) => (5 <= val && val <= 30) || 'Please check range']"
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
<style>
.title {
  height: 40px;
  border-bottom: solid 1px;
  border-color: #bcbcbc;
  background: #f3f4f5;
}

.dialog-box {
  width: 45%;

  @media (max-width: 800px) {
    width: 90%;
  }
}

.height {
  min-height: 60px;
}

.menu-bar {
  height: 40px;
  border-bottom: solid 1px;
  border-color: #bcbcbc;
  background: #dfdfdf;
}

.menu-bar-dense {
  height: 30px;
  border-bottom: solid 1px;
  border-color: #bcbcbc;
  background: #dfdfdf;
}
</style>
