<script setup lang="ts">
import { ref, watch } from 'vue'
import type { QTableProps } from 'quasar'
import { useIdStore } from '../store/idStore'
import { useUserStore } from '../store/userStore'
const userStore = useUserStore()

const idStore = useIdStore()
type LogType = {
  time: string
  type: string
  content: string
}
const logs = ref<LogType[]>([])
logs.value = [
  {
    time: '2023',
    type: '시스템',
    content: '프로그램 시작',
  },
]
const columns: QTableProps['columns'] = [
  {
    name: 'time',
    label: '시간',
    field: 'time',
    sortable: true,
    align: 'left',
    headerStyle: 'width: 100px',
  },
  {
    name: 'type',
    required: true,
    label: '구분',
    align: 'left',
    sortable: true,
    field: 'type',
    headerStyle: 'width: 100px',
  },
  {
    name: 'content',
    required: true,
    label: '내용',
    align: 'left',
    field: 'content',
    sortable: true,
  },
]

watch(
  () => idStore.clientId,
  () => {
    const clientId = idStore.clientId
    console.log(clientId)
    console.log('sys sse connected')
    if (clientId.length > 0) {
      const eventSource = new EventSource('/api/sse/system?authorization=Bearer ' + userStore.token + '&clientId=' + clientId) // 서버 SSE 엔드포인트 주소

      // <쓸데없는 버블링 주의>  vueuse sse 연결 라이브러리
      eventSource.addEventListener('message', (event) => {
        const data = JSON.parse(event.data)
        logs.value.unshift({
          time: data.time,
          type: data.type,
          content: data.content,
        })
      })
    } else {
      console.log('No clientId ')
    }
  }
)
</script>
<template>
  <div class="col table-container">
    <q-table flat square :rows="logs" :columns="columns" row-key="time" dense class="table" :rows-per-page-options="[0]" hide-no-data hide-pagination />
  </div>
</template>
<style></style>
