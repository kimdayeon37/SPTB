<script setup lang="ts">
import { ref, type PropType } from 'vue'
import { $axios } from '@/axios/index'
import { useIdStore } from '../store/idStore'

const idStore = useIdStore()
type NetworkSlaveData = {
  protocol?: string
  port?: number
  maxSessionCount?: number
  slaveId?: number
  waitTimeout?: number
}
const emits = defineEmits<{
  setRunningState: [bool: boolean]
}>()
const props = defineProps({
  networkData: {
    type: Object as PropType<NetworkSlaveData>,
    required: true,
  },

  viewLog: { type: Function, required: true },
  isRunning: { type: Boolean, required: true },
})

type MemoryType = {
  coils?: number
  distreteInputs?: number
  inputRegisters?: number
  holdingRegisters?: number
  byteSwap?: boolean
  wordSwap?: boolean
}
const inputedMemoryData = ref<MemoryType>({ byteSwap: false, wordSwap: false })
const startSimulator = async () => {
  if (!(props.networkData.maxSessionCount && props.networkData.port && props.networkData.slaveId && props.networkData.waitTimeout)) {
    alert('모든 값을 기입해주세요.')
    return
  }
  emits('setRunningState', true)
  await $axios()
    .post(
      '/api/MSE/start',
      {
        id: idStore.clientId,
        networkData: props.networkData,
        msgData: inputedMemoryData.value,
      },
      {
        headers: {
          'Content-Type': 'application/json',
        },
      }
    )
    .then(() => {
      props.viewLog(true)
    })
    .catch((err) => {
      console.log(err)
    })
}
</script>
<template>
  <div class="column q-px-md q-pt-md">
    <q-form @submit="startSimulator">
      <div class="row height">
        <div class="col-6 flex items-center">Coils</div>
        <q-input
          outlined
          v-model="inputedMemoryData.coils"
          dense
          label="1 ~ 1024"
          class="col-6"
          :rules="[(val) => !!val || '* Required', (val) => (1 <= val && val <= 1024) || 'Please check range']"
        />
      </div>
      <div class="row height">
        <div class="col-6 flex items-center">Distrete Inputs</div>
        <q-input
          outlined
          v-model="inputedMemoryData.distreteInputs"
          dense
          label="1 ~ 1024"
          class="col-6"
          :rules="[(val) => !!val || '* Required', (val) => (1 <= val && val <= 1024) || 'Please check range']"
        />
      </div>
      <div class="row height">
        <div class="col-6 flex items-center">Input Registers</div>
        <q-input
          outlined
          v-model="inputedMemoryData.inputRegisters"
          dense
          label="1 ~ 1024"
          class="col-6"
          :rules="[(val) => !!val || '* Required', (val) => (1 <= val && val <= 1024) || 'Please check range']"
        />
      </div>
      <div class="row height">
        <div class="col-6 flex items-center">Holding Registers</div>
        <q-input
          outlined
          v-model="inputedMemoryData.holdingRegisters"
          dense
          label="1 ~ 1024"
          class="col-6"
          :rules="[(val) => !!val || '* Required', (val) => (1 <= val && val <= 1024) || 'Please check range']"
        />
      </div>
      <div class="row height">
        <div class="col-6 flex items-center">Byte Swap</div>
        <q-toggle outlined v-model="inputedMemoryData.byteSwap" dense class="col-6" />
      </div>
      <div class="row height">
        <div class="col-6 flex items-center">Word Swap</div>
        <q-toggle outlined v-model="inputedMemoryData.wordSwap" dense class="col-6" />
      </div>
      <q-btn v-if="!isRunning" label="실행" type="submit" color="positive" padding="xs lg" class="q-mr-xl"></q-btn>
      <q-btn v-if="isRunning" label="중지" color="negative" padding="xs lg" @click="emits('setRunningState', false)"></q-btn>
    </q-form>
  </div>
</template>
<style></style>
