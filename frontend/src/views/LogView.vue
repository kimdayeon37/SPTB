<script setup lang="ts">
import axios from 'axios'
import type { QTableProps } from 'quasar'
import { computed, onMounted, ref } from 'vue'

type LogType = {
  time: string
  type: string
  content: string
}
const selectedDate = ref({ from: '', to: '' })

const records = ref<LogType[]>([])
records.value = [
  {
    time: '2023-09-07T02:58:27.355Z',
    type: '시스템',
    content: '프로그램 시작',
  },
  {
    time: '2023-09-07T02:58:27.355Z',
    type: '시스템',
    content: '프로그램 시작',
  },
  {
    time: '2023-09-07T02:58:27.355Z',
    type: '시스템',
    content: '프로그램 시작uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu',
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
onMounted(() => {
  axios
    .get('/api/modbus/log')
    .then((response) => {
      const data = response.data.data
      for (let i = 0; i < data.length; i++) {
        const item = data[i]
        records.value.push({
          time: item.Time,
          type: item.Type,
          content: item.Content,
        })
      }
    })
    .catch((error) => {
      console.error('Error fetching data:', error)
    })
})

const filteredRecords = computed(() => {
  const fromDate = new Date(selectedDate.value.from)
  const toDate = new Date(selectedDate.value.to)

  return records.value.filter((record) => {
    const recordDate = new Date(record.time)
    return recordDate >= fromDate && recordDate <= toDate
  })
})

const totalValue = computed(() => {
  return filteredRecords.value.length
})
</script>

<template>
  <div class="title flex items-center q-pl-md">
    <div>Log 검색 <strong> > 원하는 날짜 선택</strong></div>
  </div>
  <div class="q-pb-sm">
    <div class="row">
      <div>
        <q-date v-model="selectedDate" range />
      </div>
      <div class="col table-container">
        <q-table flat square :rows="filteredRecords" :columns="columns" row-key="time" dense class="table" :rows-per-page-options="[0]" hide-no-data hide-pagination />
      </div>
    </div>
    <q-badge outline color="primary">Total Value: {{ totalValue }}</q-badge>
  </div>
</template>
