<script setup lang="ts">
import { ref } from 'vue'

import type { QTableProps } from 'quasar'
import type { NetworkMasterData, WriterData } from '../../types'
import MMEWriterDialog from './MME-WriterDialog.vue'
const props = defineProps<{
  networkData: NetworkMasterData
  writersData: WriterData[]
  isRunning: boolean
}>()

const emits = defineEmits<{
  addWriter: [writer: WriterData]
  deleteWriters: []
  setSelectedWritersData: [datas: WriterData[]]
  startWriterSimulator: []
  setRunningState: [bool: boolean]
}>()

const addWriter = (data: WriterData) => {
  emits('addWriter', data)
}
const selectedWritersData = ref<WriterData[]>([])

const addWriterToggle = ref<boolean>(false)
const addWriterToggleHandler = (bool?: boolean) => {
  if (bool) addWriterToggle.value = bool
  else addWriterToggle.value = !addWriterToggle.value
}

const columns: QTableProps['columns'] = [
  {
    name: 'time',
    label: 'Time',
    field: '',
    sortable: true,
    align: 'left',
    headerStyle: 'width: 10px',
  },
  {
    name: 'name',
    required: true,
    label: 'Name',
    align: 'left',
    field: 'name',
  },
]
</script>
<template>
  <div class="col-12 col-md-5 column border-left">
    <div class="title q-pl-md flex items-center">
      <strong class="text-subtitle1">Write</strong>
    </div>
    <div class="menu-bar-dense row items-center justify-end">
      <q-btn
        rounded
        unelevated
        color="positive"
        size="md"
        padding="0.1px 12px"
        class="q-mx-sm"
        @click="
          () => {
            emits('setSelectedWritersData', selectedWritersData)
            emits('startWriterSimulator')
          }
        "
      >
        실행
      </q-btn>
      <q-btn flat color="main" size="md" padding="2px 12px" class="q-mx-sm" @click="addWriterToggleHandler()"> 추가 </q-btn>
      <q-separator vertical />
      <q-btn
        flat
        color="negative"
        size="md"
        padding="2px 12px"
        class="q-mx-sm"
        @click="
          () => {
            console.log('삭제')
            emits('setSelectedWritersData', selectedWritersData)
            emits('deleteWriters')
          }
        "
      >
        삭제
      </q-btn>
    </div>
    <div class="col table-container">
      <q-table
        flat
        square
        :rows="props.writersData"
        :columns="columns"
        row-key="time"
        dense
        class="table"
        :rows-per-page-options="[0]"
        hide-no-data
        hide-pagination
        selection="multiple"
        v-model:selected="selectedWritersData"
      />
    </div>
    <MMEWriterDialog v-model="addWriterToggle" @addWriter="addWriter" @addWriterToggleHandler="addWriterToggleHandler" />
  </div>
</template>
<style scoped></style>
