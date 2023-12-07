<script setup lang="ts">
import { ref } from 'vue'
import type { NetworkMasterData, ReaderData } from '../../types'
import type { QTableProps } from 'quasar'
import MMEReaderDialog from './MME-ReaderDialog.vue'
const props = defineProps<{
  networkData: NetworkMasterData
  readersData: ReaderData[]
  isRunning: boolean
}>()

const emits = defineEmits<{
  addReader: [reader: ReaderData]
  deleteReaders: []
  setSelectedReadersData: [datas: ReaderData[]]
  startReaderSimulator: []
  setRunningState: [bool: boolean]
}>()
const addReader = (data: ReaderData) => {
  emits('addReader', data)
}
const selectedReadersData = ref<ReaderData[]>([])
const addReaderToggle = ref<boolean>(false)
const addReaderToggleHandler = (bool?: boolean) => {
  if (bool) addReaderToggle.value = bool
  else addReaderToggle.value = !addReaderToggle.value
}
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
</script>
<template>
  <div class="col-12 col-md-7 column">
    <div class="title q-pl-md flex items-center">
      <strong class="text-subtitle1">Read</strong>
    </div>
    <div class="menu-bar-dense row items-center justify-end">
      <q-btn
        v-show="!isRunning"
        rounded
        unelevated
        size="md"
        padding="0.1px 12px"
        color="positive"
        class="setting-btn q-mx-md"
        @click="
          () => {
            emits('setSelectedReadersData', selectedReadersData)
            emits('startReaderSimulator')
          }
        "
      >
        시작
      </q-btn>
      <q-btn
        v-if="isRunning"
        rounded
        unelevated
        size="md"
        padding="0.1px 12px"
        color="negative"
        class="setting-btn q-mx-md"
        @click="
          () => {
            emits('setRunningState', false)
          }
        "
      >
        중지
      </q-btn>
      <q-separator vertical />
      <q-btn flat color="main" size="md" padding="2px 12px" class="q-mx-sm" @click="addReaderToggleHandler()"> 추가 </q-btn>
      <q-separator vertical inset />
      <q-btn
        flat
        color="negative"
        size="md"
        padding="2px 12px"
        class="q-mx-sm"
        @click="
          () => {
            emits('setSelectedReadersData', selectedReadersData)
            emits('deleteReaders')
          }
        "
      >
        삭제
      </q-btn>
    </div>
    <div class="col overflow-hidden table-container">
      <q-table
        flat
        square
        :rows="props.readersData"
        :columns="columns"
        row-key="time"
        dense
        class="table"
        :rows-per-page-options="[0]"
        hide-no-data
        hide-pagination
        selection="multiple"
        v-model:selected="selectedReadersData"
      />
    </div>
    <MMEReaderDialog v-model="addReaderToggle" @addReader="addReader" @addReaderToggleHandler="addReaderToggleHandler" />
  </div>
</template>
<style scoped></style>
