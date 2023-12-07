<script setup lang="ts">
import { $axios } from '@/axios/index'
import type { QTableProps } from 'quasar'
import { ref } from 'vue'
import { type OUCMemoryData } from '../../types'

import { useOUCNetworkStore } from '../../store/OPCUAClient/OUC-NetworkStore'
import { useIdStore } from '../../store/idStore'
import { useStateStore } from '../../store/stateStore'
import OUCMemoryAddDialog from './OUC-MemoryAddDialog.vue'

const stateStore = useStateStore()
const idStore = useIdStore()

// memory 데이터
const memoriesData = ref<OUCMemoryData[]>([])
const selectedMemoryData = ref<OUCMemoryData[]>([])
const setSelectedMemoryData = (datas: OUCMemoryData[]) => {
  selectedMemoryData.value = datas
  console.log(selectedMemoryData.value)
}

const addWriter = (writer: OUCMemoryData) => {
  emits('addWriter', writer)

  console.log('addWriter')
  const newWriter = JSON.parse(JSON.stringify(writer))
  memoriesData.value.push({
    ...newWriter,
  })
}
const deleteMemories = () => {
  const newWriters = memoriesData.value.filter(
    (item) =>
      !selectedMemoryData.value.some((target) => {
        return target == item
      })
  )
  memoriesData.value = newWriters
}

const emits = defineEmits<{
  addWriter: [writer: OUCMemoryData]
  deleteMemories: []
  setSelectedMemoryData: [datas: OUCMemoryData[]]
}>()

const selectedIndex = ref<string>()

const addWriterToggle = ref<boolean>(false)

const columns: QTableProps['columns'] = [
  {
    name: 'nodeId',
    required: true,
    label: 'Subscriptions',
    align: 'left',
    field: 'nodeId',
  },
]

const startOpcuaClientSimulator = async () => {
  const idStore = useIdStore()
  const networkStore = useOUCNetworkStore()

  try {
    await $axios()
      .post('/api/opcua/client', {
        id: idStore.clientId,
        oucNetworkData: {
          ...networkStore.networkData,
          msgCount: memoriesData.value.length,
        },
        msgData: memoriesData.value,
      })
      .then(() => {
        console.log('전송 성공')
        stateStore.isRunning = true
      })
  } catch (e) {
    console.log('전송 실패 : ', e)
  }
}

const sendExitSignal = async () => {
  if (stateStore.isRunning)
    $axios()
      .post('/api/exit', {
        id: idStore.clientId,
      })
      .then(() => {
        stateStore.isRunning = false
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
        v-show="!stateStore.isRunning"
        rounded
        unelevated
        color="positive"
        size="md"
        padding="0.1px 12px"
        class="q-mx-sm"
        @click="
          () => {
            //emits('setSelectedMemoryData', selectedMemoryData);
            setSelectedMemoryData(selectedMemoryData)
            startOpcuaClientSimulator()
          }
        "
      >
        시작
      </q-btn>
      <q-btn
        v-if="stateStore.isRunning"
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
            // emits('setSelectedMemoryData', selectedMemoryData);
            // emits('deleteMemories');
            setSelectedMemoryData(selectedMemoryData)
            deleteMemories()
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
        :rows="memoriesData"
        :columns="columns"
        row-key="nodeId"
        dense
        class="table"
        :rows-per-page-options="[0]"
        hide-no-data
        hide-pagination
        selection="multiple"
        v-model:selected="selectedMemoryData"
      />
    </div>
    <OUCMemoryAddDialog @addWriter="addWriter" v-model="addWriterToggle" :selectedIndex="selectedIndex" />
  </div>
</template>
