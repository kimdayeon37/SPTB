<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useToggleStore } from '../store/modules/settingtoggle'
import type { NetworkMasterData } from '../types'

let savedData = ref<NetworkMasterData[]>([])

const deleteCard = (index: number) => {
  savedData.value.splice(index, 1)

  // Remove the corresponding local storage value
  const savedDataJSON = localStorage.getItem('networkMasterData')
  if (savedDataJSON) {
    const parsedData = JSON.parse(savedDataJSON) as NetworkMasterData[]
    parsedData.splice(index, 1)
    localStorage.setItem('networkMasterData', JSON.stringify(parsedData))
  }
}

const router = useRouter()
const openCard = (selectedData: NetworkMasterData) => {
  const toggleStore = useToggleStore()
  toggleStore.toggleSetting(false)

  // 선택한 데이터를 JSON 문자열로 변환하여 URL params로 전달
  const selectedDataJSON = JSON.stringify(selectedData)
  router.push({ name: 'MasterEthernet', query: { selectedData: selectedDataJSON } })
}

// 데이터 로드 함수
const loadData = () => {
  try {
    const savedDataJSON = localStorage.getItem('networkMasterData')
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
  console.log(savedData.value)
})

const ratingModel = ref<number>(2)

let buttonActivated = ref(true)

const moveToTop = (index: number) => {
  if (buttonActivated.value && savedData.value) {
    // Remove the clicked card session from its current position
    const clickedData = savedData.value.splice(index, 1)[0]

    // Insert the clicked card session at the beginning
    savedData.value.unshift(clickedData)
    localStorage.setItem('networkMasterData', JSON.stringify(savedData.value))
  }
}
</script>

<template>
  <div v-for="(data, i) in savedData" :key="i">
    <q-card class="my-card bg-secondary text-white">
      <q-card-section>
        <div class="text-h6">{{ data.protocol }}</div>
        <div class="text-subtitle2">{{ data.ip }}</div>
        <div class="text-subtitle2">[Port] {{ data.port }}</div>
        <div class="text-subtitle2">[Transaction Delay] {{ data.transactionDelay }}</div>
        <div class="text-subtitle2">[Timeout] {{ data.timeout }}</div>
      </q-card-section>

      <q-separator dark />

      <q-card-actions>
        <q-btn flat @click="deleteCard(i)">Delete</q-btn>
        <q-btn flat @click="() => openCard(data)">Open</q-btn>

        <q-btn flat @click="moveToTop(i)">
          <q-rating v-model="ratingModel" size="1em" :max="1" color="yellow" class="rating">
            <template v-slot:tip-1>
              <q-tooltip>고정!</q-tooltip>
            </template>
          </q-rating>
        </q-btn>
      </q-card-actions>
    </q-card>
  </div>
</template>
