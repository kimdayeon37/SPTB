<script setup lang="ts">
import { onMounted, onUnmounted, ref } from 'vue'
import { $axios } from '@/axios/index'
import { useIdStore } from '../store/idStore'
import { useRoute } from 'vue-router'

import MMETopbar from '../components/MME/MME-Topbar.vue'
import MMEReader from '../components/MME/MME-Reader.vue'
import MMEWriter from '../components/MME/MME-Writer.vue'
import TransLog from '../components/Trans-Log.vue'

import type { NetworkMasterData, ReaderData, WriterData } from '../types'

const idStore = useIdStore()
const isRunning = ref<boolean>(false)
const setRunningState = (bool: boolean) => {
  isRunning.value = bool
  console.log(isRunning.value)
  if (!isRunning.value) {
    $axios()
      .post('/api/exit', {
        id: idStore.clientId,
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
      !selectedReadersData.value.some((target) => {
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

  await $axios()
    .post(
      '/api/reader',
      {
        id: idStore.clientId,
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
      !selectedWritersData.value.some((target) => {
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
  await $axios()
    .post(
      '/api/writer',
      {
        id: idStore.clientId,
        networkData: {
          ...networkMasterData.value,
          msgCount: writersData.value.length,
        },
        msgData: writersData.value,
      },
      {
        headers: {
          'Content-Type': 'application/json',
        },
      }
    )
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
  $axios()
    .post(
      '/api/id',
      {
        id: idStore.clientId,
      },
      {
        headers: {
          'Content-Type': 'application/json',
        },
      }
    )
    .then(() => {
      console.log('id : ' + idStore.clientId)
    })
    .catch((err) => {
      console.log(err)
    })
})
onUnmounted(() => {
  if (isRunning.value)
    $axios()
      .post(
        '/api/exit',
        {
          id: idStore.clientId,
        },
        {
          headers: {
            'Content-Type': 'application/json',
          },
        }
      )
      .catch((err) => {
        console.log(err)
      })
})
</script>
<template>
  <div class="column">
    <MMETopbar v-model:view-log-toggle="viewLogToggle" @setNetworkMasterData="setNetworkMasterData" :networkData="networkMasterData" />
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
.border-left {
  border-left: solid 1px;
  border-color: #bcbcbc;
}
.input-box {
  .q-field--dense .q-field__control {
    height: 100% !important;
  }
  .q-field__bottom {
    height: 0px;
    overflow: hidden;
    margin: 0px;
    padding: 0px;
    display: none !important;
  }
}
</style>
