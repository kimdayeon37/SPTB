<script setup lang="ts">
import axios from 'axios'
import { onMounted, onUnmounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { useIdStore } from '../store/idStore'

import MMEReader from '../components/MME/MME-Reader.vue'
import MMETopbar from '../components/MME/MME-Topbar.vue'
import MMEWriter from '../components/MME/MME-Writer.vue'
import TransLog from '../components/Trans-Log.vue'

import type { NetworkMasterData, ReaderData, WriterData } from '../types'

const idStore = useIdStore()
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

// Topbar 데이터
const networkMasterData = ref<NetworkMasterData>({
  protocol: 'TCP',
})

const setNetworkMasterData = (data: NetworkMasterData) => {
  networkMasterData.value = data
}

const viewLogToggle = ref<boolean>(false)
const setViewLogToggle = (bool: boolean) => {
  viewLogToggle.value = bool
}

// Reader 데이터
const readersData = ref<ReaderData[]>([])
const selectedReadersData = ref<ReaderData[]>([])
const setSelectedReadersData = (datas: ReaderData[]) => {
  selectedReadersData.value = datas
}

const addReader = (reader: ReaderData) => {
  console.log('addReader')
  readersData.value.push({
    time: Date.now(),
    ...reader,
  })
}
const deleteReaders = () => {
  const newReaders = readersData.value.filter(
    (item) =>
      !!selectedReadersData.value.some((target) => {
        console.log(target.time + ' : ' + item.time)

        return target.time == item.time
      })
  )
  readersData.value = newReaders
}
const startReaderSimulator = async () => {
  if (!(networkMasterData.value.ip && networkMasterData.value.port && networkMasterData.value.timeout && networkMasterData.value.transactionDelay)) {
    alert('통신 설정 값을 기입해주세요.')
    return
  }

  await axios
    .post(
      '/api/modbus/reader',
      {
        id: idStore.get(),
        networkData: {
          ...networkMasterData.value,
          msgCount: readersData.value.length,
        },
        msgData: readersData.value,
      },
      {
        headers: {
          'Content-Type': 'application/json',
        },
      }
    )
    .then(() => {
      setViewLogToggle(true)
      setRunningState(true)
    })
    .catch((err) => {
      console.log(err)
    })
}

// Writer 데이터
const writersData = ref<WriterData[]>([])
const selectedWritersData = ref<WriterData[]>([])
const setSelectedWritersData = (datas: WriterData[]) => {
  console.log('setSelectedWritersData')

  selectedWritersData.value = datas
  console.log(selectedWritersData.value)
}

const addWriter = (writer: WriterData) => {
  console.log('addWriter')
  const newWriter = JSON.parse(JSON.stringify(writer))
  writersData.value.push({
    time: Date.now(),
    ...newWriter,
  })
}
const deleteWriters = () => {
  const newWriters = writersData.value.filter(
    (item) =>
      !!selectedWritersData.value.some((target) => {
        return target.time == item.time
      })
  )
  writersData.value = newWriters
}
const startWriterSimulator = async () => {
  if (!(networkMasterData.value.ip && networkMasterData.value.port && networkMasterData.value.timeout && networkMasterData.value.transactionDelay)) {
    alert('통신 설정 값을 기입해주세요.')
    return
  }
  // setViewLogToggle(true);
  await axios
    .post('/api/modbus/writer', {
      id: idStore.get(),
      networkData: {
        ...networkMasterData.value,
        msgCount: writersData.value.length,
      },
      msgData: writersData.value,
    })
    .then(() => {
      setViewLogToggle(true)
    })
    .catch((err) => {
      console.log(err)
    })
}

const route = useRoute()
onMounted(() => {
  const querySelectedData = route.query.selectedData as string | undefined

  if (querySelectedData) {
    // query 파라미터가 존재하면 파싱하여 networkMasterData.value에 할당
    networkMasterData.value = JSON.parse(querySelectedData) as NetworkMasterData
    console.log(networkMasterData.value)
  } else {
    // query 파라미터가 없을 경우
  }
  console.log('id : ' + idStore.get())
  axios
    .post('/api/modbus/id', {
      id: idStore.get(),
    })
    .then(() => {
      console.log('id : ' + idStore.get())
    })
    .catch((err) => {
      console.log(err)
    })
})
onUnmounted(() => {
  if (isRunning.value)
    axios
      .post('/api/modbus/exit', {
        id: idStore.get(),
      })
      .catch((err) => {
        console.log(err)
      })
})
</script>
<template>
  <div class="column">
    <MMETopbar @setViewLogToggle="setViewLogToggle" @setNetworkMasterData="setNetworkMasterData" :networkData="networkMasterData" :viewLogToggle="viewLogToggle" />
    <div v-show="!viewLogToggle" class="col row">
      <MMEReader
        @setViewLogToggle="setViewLogToggle"
        @startReaderSimulator="startReaderSimulator"
        @addReader="addReader"
        @deleteReaders="deleteReaders"
        @setSelectedReadersData="setSelectedReadersData"
        @setRunningState="setRunningState"
        :networkData="networkMasterData"
        :readersData="readersData"
        :isRunning="isRunning"
      />
      <MMEWriter
        @setViewLogToggle="setViewLogToggle"
        @setSelectedWritersData="setSelectedWritersData"
        @startWriterSimulator="startWriterSimulator"
        @addWriter="addWriter"
        @deleteWriters="deleteWriters"
        @setRunningState="setRunningState"
        :networkData="networkMasterData"
        :writersData="writersData"
        :isRunning="isRunning"
      />
    </div>
    <div v-show="viewLogToggle" class="col row">
      <TransLog />
    </div>
  </div>
</template>
<style></style>
