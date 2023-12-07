<script setup lang="ts">
import { ref, watch } from 'vue'
import type { NetworkMasterData } from '../../types'
import { useToggleStore } from '../../store/modules/settingtoggle'
const props = defineProps<{
  networkData: NetworkMasterData
}>()
const newNetworkData = ref<NetworkMasterData>({
  protocol: 'TCP',
})

const emits = defineEmits<{
  setNetworkMasterData: [data: NetworkMasterData]
  setNetworkDialogToggle: [bool?: boolean]
}>()

const toggleStore = useToggleStore()
//toggleStore.toggleSetting(false)
toggleStore.networkDialogToggle = false
toggleStore.toggle()

const setNetwork = () => {
  const data = newNetworkData.value
  // Check if currentData is not an empty object
  if (Object.keys(data).length > 0) {
    try {
      // Retrieve the existing array from localStorage
      const savedDataJSON = localStorage.getItem('networkMasterData')
      let savedData = typeof savedDataJSON === 'string' ? JSON.parse(savedDataJSON) : []

      // Add the new data to the existing array
      savedData.unshift(data)

      // Save the updated array back to localStorage
      localStorage.setItem('networkMasterData', JSON.stringify(savedData))
    } catch (error) {
      // Handle any JSON parsing or storage errors
      console.error('Error saving data to localStorage:', error)
    }
  }
  toggleStore.toggle()

  //()=> { emits('setNetworkDialogToggle');}
}
const protocolOptions = ['TCP']
watch(
  () => props.networkData,
  () => {
    newNetworkData.value = props.networkData
  }
)
</script>
<template>
  <q-dialog v-if="toggleStore.networkDialogToggle" v-model="toggleStore.networkDialogToggle" persistent>
    <q-card class="q-pa-md dialog-box">
      <q-form
        @submit="
          () => {
            emits('setNetworkMasterData', newNetworkData)
            emits('setNetworkDialogToggle', false)
            setNetwork()
          }
        "
      >
        <div class="row height justify-center text-h6 text-weight-bold">Master 통신 설정</div>
        <div class="row height">
          <div class="col-6 flex items-center">Protocol</div>
          <q-select outlined v-model="newNetworkData.protocol" :options="protocolOptions" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
        </div>
        <div class="row height">
          <div class="col-6 height flex items-center">IP</div>
          <q-input
            outlined
            v-model="newNetworkData.ip"
            label="###.###.###.###"
            dense
            class="col-6"
            :rules="[
              (val) => !!val || '* Required',
              (val) => {
                const ipv4Pattern = /^(\d{1,3}\.){3}\d{1,3}$/ // IPv4 형식을 검증하는 정규 표현식
                return ipv4Pattern.test(val) // 정규 표현식과 매치되는지 확인
              },
            ]"
          />
        </div>
        <div class="row height">
          <div class="col-6 height flex items-center">Port</div>
          <q-input
            outlined
            v-model="newNetworkData.port"
            mask="#####"
            dense
            label="0 ~ 65535"
            class="col-6"
            :rules="[(val) => !!val || '* Required', (val) => (0 <= val && val <= 65535) || 'Please check range']"
          />
        </div>
        <div class="row height">
          <div class="col-6 height flex items-center">transaction Delay</div>
          <q-input
            outlined
            class="col-6"
            v-model="newNetworkData.transactionDelay"
            dense
            label="100 ~ 2000"
            :rules="[(val) => !!val || '* Required', (val) => (100 <= val && val <= 2000) || 'Please check range']"
          />
        </div>
        <div class="row height">
          <div class="col-6 height flex items-center">Timeout(s)</div>
          <q-input
            outlined
            v-model="newNetworkData.timeout"
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
</template>
<style></style>
