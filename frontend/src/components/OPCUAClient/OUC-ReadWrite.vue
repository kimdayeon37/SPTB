<script setup lang="ts">
import { $axios } from '@/axios/index'
import type { QTableProps } from 'quasar'
import { ref } from 'vue'
import { type ReadWriterData } from '../../types'
import ReadWriteDialog from './OUC-ReadWriteDialog.vue'

import { useOUCNetworkStore } from '../../store/OPCUAClient/OUC-NetworkStore'
import { useIdStore } from '../../store/idStore'
import { useStateStore } from '../../store/stateStore'

const stateStore = useStateStore()
const idStore = useIdStore()

// ReadWriter 데이터
const writersData = ref<ReadWriterData[]>([])
const selectedWritersData = ref<ReadWriterData[]>([])
const setSelectedWritersData = (datas: ReadWriterData[]) => {
  console.log('setSelectedWritersData')

  selectedWritersData.value = datas
  console.log(selectedWritersData.value)
}

const addWriter = (writer: ReadWriterData) => {
  emits('addWriter', writer)

  console.log('addWriter')
  const newWriter = JSON.parse(JSON.stringify(writer))
  writersData.value.push({
    ...newWriter,
  })
}
const deleteWriters = () => {
  const newWriters = writersData.value.filter(
    (item) =>
      !selectedWritersData.value.some((target) => {
        return target == item
      })
  )
  writersData.value = newWriters
}

const emits = defineEmits<{
  addWriter: [writer: ReadWriterData]
}>()

const addWriterToggle = ref<boolean>(false)

const columns: QTableProps['columns'] = [
  {
    name: 'name',
    required: true,
    label: 'Name',
    align: 'left',
    field: 'name',
  },
]

const startOpcuaReadWriteSimulator = async () => {
  const idStore = useIdStore()
  const networkStore = useOUCNetworkStore()

  try {
    await $axios()
      .post('/api/opcua/readwriter', {
        id: idStore.clientId,
        networkData: {
          ...networkStore.networkData,
          msgCount: writersData.value.length,
        },
        msgData: writersData.value,
      })
      .then(() => {
        console.log('전송 성공')
        stateStore.state = true
      })
  } catch (e) {
    console.log('전송 실패 : ', e)
  }
}

const sendExitSignal = async () => {
  if (stateStore.state)
    $axios()
      .post('/api/exit', {
        id: idStore.clientId,
      })
      .then(() => {
        stateStore.state = false
      })
      .catch((err) => {
        console.log(err)
      })
}
</script>
<template>
  <div class="col-12 col-md-5 column border-left">
    <div class="menu-bar-dense row items-center justify-end">
      <q-btn
        v-show="!stateStore.state"
        rounded
        unelevated
        color="positive"
        size="md"
        padding="0.1px 12px"
        class="q-mx-sm"
        @click="
          () => {
            //emits('setSelectedWritersData', selectedWritersData);
            setSelectedWritersData(selectedWritersData)
            startOpcuaReadWriteSimulator()
          }
        "
      >
        실행
      </q-btn>
      <q-btn
        v-if="stateStore.state"
        rounded
        unelevated
        color="negative"
        size="md"
        padding="0.1px 12px"
        class="q-mx-sm"
        @click="
          () => {
            sendExitSignal()
          }
        "
      >
        중지
      </q-btn>
      <q-btn
        flat
        color="main"
        size="md"
        padding="2px 12px"
        class="q-mx-sm"
        @click="
          () => {
            addWriterToggle = true
          }
        "
      >
        추가
      </q-btn>
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
            // emits('setSelectedWritersData', selectedWritersData);
            setSelectedWritersData(selectedWritersData)
            deleteWriters()
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
        :rows="writersData"
        :columns="columns"
        row-key="name"
        dense
        class="table"
        :rows-per-page-options="[0]"
        hide-no-data
        hide-pagination
        selection="multiple"
        v-model:selected="selectedWritersData"
      />
    </div>
    <ReadWriteDialog @addWriter="addWriter" v-model="addWriterToggle" />
  </div>
</template>
