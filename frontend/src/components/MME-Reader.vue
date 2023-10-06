<script setup lang="ts">
import axios from 'axios'
import type { QTableProps } from 'quasar'
import { ref, type PropType } from 'vue'
type NetworkData = {
  protocol?: string
  ip?: string
  port?: number
  transactionDelay?: number
  timeout?: number
}

const props = defineProps({
  networkData: {
    type: Object as PropType<NetworkData>,
    required: true,
  },
  viewLog: { type: Function, required: true },
})
const inputedReaderName = ref<string>()
const inputedReaderSlaveId = ref<number>()
const inputedReaderArea = ref<string>('Coil')
const areaOptions = ['Coil', 'DiscreteInput', 'InputRegister', 'HoldingRegister']
const inputedReaderAddress = ref<number>()
const inputedReaderQuantity = ref<number>()
const inputedReaderScanTime = ref<number>()
const inputedReaderByteSwap = ref<boolean>(false)
const inputedReaderWordSwap = ref<boolean>(false)
const addReaderToggle = ref<boolean>(false)
const addReaderToggleHandler = () => {
  addReaderToggle.value = !addReaderToggle.value
}
type ReaderType = {
  time: number
  name?: string
  slaveId?: number
  area?: string
  address?: number
  quantity?: number
  scanTime?: number
  byteSwap: '1' | '0'
  wordSwap: '1' | '0'
}
const readers = ref<ReaderType[]>([])
const selectedReaders = ref<ReaderType[]>([])
const columns: QTableProps['columns'] = [
  {
    name: 'time',
    label: 'Time',
    field: '',
    sortable: true,
    align: 'left',
    headerStyle: 'width: 5px',
  },
  {
    name: 'name',
    required: true,
    label: 'Name',
    align: 'left',
    field: 'name',
  },
  {
    name: 'slaveId',
    required: true,
    label: 'Slave ID',
    align: 'left',
    field: 'slaveId',
    sortable: true,
  },
  {
    name: 'area',
    required: true,
    label: 'Area',
    align: 'left',
    field: 'area',
    sortable: true,
  },
  {
    name: 'address',
    required: true,
    label: 'Address',
    align: 'left',
    field: 'address',
    sortable: true,
  },
  {
    name: 'quantity',
    required: true,
    label: 'Quantity',
    align: 'left',
    field: 'quantity',
  },
  {
    name: 'scanTime',
    required: true,
    label: 'Scan Time',
    align: 'left',
    field: 'scanTime',
  },
]

const addReader = () => {
  console.log('addReader')
  readers.value?.push({
    time: Date.now(),
    name: inputedReaderName.value,
    slaveId: inputedReaderSlaveId.value,
    area: inputedReaderArea.value,
    address: inputedReaderAddress.value,
    quantity: inputedReaderQuantity.value,
    scanTime: inputedReaderScanTime.value,
    byteSwap: inputedReaderByteSwap.value ? '1' : '0',
    wordSwap: inputedReaderWordSwap.value ? '1' : '0',
  })
  addReaderToggleHandler()
  console.log(readers)
}

const deleteReaders = () => {
  console.log(selectedReaders.value)
  console.log(readers.value)

  const newReaders = readers.value.filter(
    (item) =>
      !selectedReaders.value.some((target) => {
        console.log(target.time + ' : ' + item.time)

        return target.time == item.time
      })
  )
  readers.value = newReaders
  console.log(readers.value)
}
const startSimulator = async () => {
  if (selectedReaders.value.length === 0) {
    alert('시작할 Reader들을 선택해주세요.')
    return
  }
  if (!(props.networkData.ip && props.networkData.port && props.networkData.timeout && props.networkData.transactionDelay)) {
    alert('통신 설정 값을 기입해주세요.')
    return
  }
  console.log({
    networkData: {
      timeout: props.networkData.timeout,
      msgCount: selectedReaders.value.length,
    },
    msgData: selectedReaders.value,
  })

  await axios
    .post(
      '/api/modbus/reader',
      {
        networkData: {
          ...props.networkData,
          msgCount: selectedReaders.value.length,
        },
        msgData: selectedReaders.value,
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
  <div class="col-12 col-md-7 column">
    <div class="title q-pl-md flex items-center">
      <strong class="text-subtitle1">Read</strong>
    </div>
    <div class="menu-bar-dense row items-center justify-end">
      <q-btn rounded unelevated size="md" padding="0.1px 12px" color="positive" class="setting-btn q-mx-sm" @click="startSimulator"> 시작 </q-btn>
      <q-btn rounded unelevated size="md" padding="0.1px 12px" color="negative" class="setting-btn q-mx-md"> 중지 </q-btn>
      <q-separator vertical />
      <q-btn flat color="main" size="md" padding="2px 12px" class="q-mx-sm" @click="addReaderToggleHandler"> 추가 </q-btn>
      <q-separator vertical inset />
      <q-btn flat color="negative" size="md" padding="2px 12px" class="q-mx-sm" @click="deleteReaders"> 삭제 </q-btn>
    </div>
    <div class="col overflow-hidden table-container">
      <q-table
        flat
        square
        :rows="readers"
        :columns="columns"
        row-key="time"
        dense
        class="table"
        :rows-per-page-options="[0]"
        hide-no-data
        hide-pagination
        selection="multiple"
        v-model:selected="selectedReaders"
      />
    </div>
    <q-dialog v-model="addReaderToggle">
      <q-card class="q-pa-md dialog-box">
        <q-form @submit="addReader">
          <div class="row height">
            <div class="col-6 flex items-center">Name</div>
            <q-input outlined v-model="inputedReaderName" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
          </div>
          <div class="row height">
            <div class="col-6 height flex items-center">Slave ID</div>
            <q-input
              outlined
              v-model="inputedReaderSlaveId"
              dense
              class="col-6"
              label="1 ~ 3"
              :rules="[(val) => !!val || '* Required', (val) => (1 <= val && val <= 3) || 'Please check range']"
            />
          </div>
          <div class="row height">
            <div class="col-6 height flex items-center">Area</div>
            <q-select outlined v-model="inputedReaderArea" mask="#####" dense :options="areaOptions" class="col-6" :rules="[(val) => !!val || '* Required']" />
          </div>
          <div class="row height">
            <div class="col-6 height flex items-center">Address</div>
            <q-input
              outlined
              class="col-6"
              v-model="inputedReaderAddress"
              dense
              label="0 ~ 49999"
              :rules="[(val) => !!val || '* Required', (val) => (0 <= val && val <= 4999) || 'Please check range']"
            />
          </div>
          <div class="row height">
            <div class="col-6 height flex items-center">Quantity</div>
            <q-input
              outlined
              v-model="inputedReaderQuantity"
              dense
              class="col-6"
              label="1 ~ 9999"
              :rules="[(val) => !!val || '* Required', (val) => (1 <= val && val <= 9999) || 'Please check range']"
            />
          </div>
          <div class="row height">
            <div class="col-6 height flex items-center">Scan Time(ms)</div>
            <q-input
              outlined
              v-model="inputedReaderScanTime"
              dense
              class="col-6"
              label="1 ~ 1000"
              :rules="[(val) => !!val || '* Required', (val) => (1 <= val && val <= 1000) || 'Please check range']"
            />
          </div>
          <div class="row height">
            <div class="col-6 height flex items-center">Byte Swap</div>
            <q-toggle color="main" v-model="inputedReaderByteSwap" />
          </div>
          <div class="row height">
            <div class="col-6 height flex items-center">Word Swap</div>
            <q-toggle color="main" v-model="inputedReaderWordSwap" />
          </div>
          <div class="row justify-evenly items-center">
            <q-btn label="추가" type="submit" color="main" padding="xs lg"></q-btn>
            <q-btn label="취소" flat padding="xs lg" color="red" v-close-popup></q-btn>
          </div>
        </q-form>
      </q-card>
    </q-dialog>
  </div>
</template>
<style></style>
