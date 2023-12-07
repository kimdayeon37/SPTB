<script setup lang="ts">
import { ref, type PropType } from 'vue'
import axios from 'axios'
type NetworkData = {
  protocol?: string
  slaveId?: number
  comPort?: number
  baudrate?: number
  dataBit?: number
  stopBit?: number
  parity?: 'None' | 'Odd' | 'Even'
}

const props = defineProps({
  networkData: {
    type: Object as PropType<NetworkData>,
    required: true,
  },

  viewLog: { type: Function, required: true },
})
const inputedCoils = ref<number>()
const inputedDistreteInputs = ref<number>()
const inputedInputRegisters = ref<number>()
const inputedHoldingRegister = ref<number>()
const inputedByteSwap = ref<boolean>(false)
const inputedWordSwap = ref<boolean>(false)

const startSimulator = async () => {
  if (
    !(
      inputedCoils.value &&
      inputedDistreteInputs.value &&
      inputedInputRegisters.value &&
      inputedHoldingRegister.value &&
      props.networkData.baudrate &&
      props.networkData.comPort &&
      props.networkData.dataBit &&
      props.networkData.parity &&
      props.networkData.protocol &&
      props.networkData.slaveId &&
      props.networkData.stopBit
    )
  ) {
    console.log('모든 값을 기입해주세요.')
    return
  }
  const memoryData: any = {
    coils: inputedCoils.value,
    distreteInputs: inputedDistreteInputs.value,
    inputRegisters: inputedInputRegisters.value,
    holdingRegisters: inputedHoldingRegister.value,
    byteSwap: inputedByteSwap.value ? '1' : '0',
    wordSwap: inputedWordSwap.value ? '1' : '0',
  }

  await axios
    .post(
      'http://localhost:4000/MSE/start',
      {
        networkData: props.networkData,
        msgData: memoryData,
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
          v-model="inputedCoils"
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
          v-model="inputedDistreteInputs"
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
          v-model="inputedInputRegisters"
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
          v-model="inputedHoldingRegister"
          dense
          label="1 ~ 1024"
          class="col-6"
          :rules="[(val) => !!val || '* Required', (val) => (1 <= val && val <= 1024) || 'Please check range']"
        />
      </div>
      <div class="row height">
        <div class="col-6 flex items-center">Byte Swap</div>
        <q-toggle outlined v-model="inputedByteSwap" dense class="col-6" />
      </div>
      <div class="row height">
        <div class="col-6 flex items-center">Word Swap</div>
        <q-toggle outlined v-model="inputedWordSwap" dense class="col-6" />
      </div>
      <q-btn label="실행" type="submit" color="positive" padding="xs lg" class="q-mr-xl"></q-btn>
      <q-btn label="중지" color="negative" padding="xs lg"></q-btn>
    </q-form>
  </div>
</template>
<style></style>
