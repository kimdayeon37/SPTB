<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useToggleStore } from '../store/modules/settingtoggle'

type SettingType = {
  protocol?: string
  slaveId?: number
  comPort?: number
  baudrate?: number
  dataBit?: number
  stopBit?: number
  parity?: 'None' | 'Odd' | 'Even'
}

const router = useRouter()
const openCard = (selectedData: SettingType) => {
  const toggleStore = useToggleStore()
  toggleStore.toggleSetting(false)

  // 선택한 데이터를 JSON 문자열로 변환하여 URL params로 전달
  const selectedDataJSON = JSON.stringify(selectedData)
  router.push({ name: 'SlaveSerial', query: { selectedData: selectedDataJSON } })
}

const deleteCard = (index: number) => {
  savedData.value.splice(index, 1)

  // Remove the corresponding local storage value
  const savedDataJSON = localStorage.getItem('slaveSerialData')
  if (savedDataJSON) {
    const parsedData = JSON.parse(savedDataJSON) as SettingType[]
    parsedData.splice(index, 1)
    localStorage.setItem('slaveSerialData', JSON.stringify(parsedData))
  }
}

let savedData = ref<SettingType[]>([])
let ratingModel = ref<number>(1)

// 데이터 로드 함수
const loadData = () => {
  try {
    const savedDataJSON = localStorage.getItem('slaveSerialData')
    if (savedDataJSON) {
      savedData.value = JSON.parse(savedDataJSON)
    }
  } catch (error) {
    console.error('Error loading data from localStorage:', error)
  }
}

// 페이지가 로드될 때 데이터 로드 실행
onMounted(() => {
  loadData()
})

let buttonActivated = ref(true)

const moveToTop = (index: number) => {
  if (buttonActivated.value) {
    // Deactivate the rating for the item being moved
    //savedData.value[index].isActive = false;

    // Remove the clicked card session from its current position
    const clickedData = savedData.value.splice(index, 1)[0]

    // Insert the clicked card session at the beginning
    savedData.value.unshift(clickedData)
    localStorage.setItem('slaveSerialData', JSON.stringify(savedData.value))
  }
}
</script>

<template>
  <div v-for="(data, i) in savedData" :key="i" v-if="savedData && savedData.length > 0">
    <q-card class="my-card bg-primary text-white">
      <q-card-section>
        <div class="text-h6">{{ data.protocol }}</div>
        <div class="text-subtitle2">[Slave ID] {{ data.slaveId }}</div>
        <div class="text-subtitle2">[ComPort] {{ data.comPort }}</div>
        <div class="text-subtitle2">[Baudrate] {{ data.baudrate }}</div>
        <div class="text-subtitle2">[Data Bit] {{ data.dataBit }}</div>
        <div class="text-subtitle2">[Stop Bit] {{ data.stopBit }}</div>
        <div class="text-subtitle2">[Parity] {{ data.parity }}</div>
      </q-card-section>

      <q-separator dark />

      <q-card-actions>
        <q-btn flat @click="deleteCard(i)">Delete</q-btn>
        <q-btn flat @click="() => openCard(data)">Open</q-btn>

        <q-btn flat @click="moveToTop(i)">
          <q-rating v-model="data.ratingModel" size="1em" :max="1" color="yellow" class="rating">
            <template v-slot:tip-1>
              <q-tooltip>고정!</q-tooltip>
            </template>
          </q-rating>
        </q-btn>
      </q-card-actions>
    </q-card>
  </div>
</template>
