<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import IPList from '@/components/Setting/IP-List.vue'
import { $axios } from '@/axios/index'

const active = ref(false)
const showInput = ref(false)
const toggleInput = () => {
  showInput.value = !showInput.value
}
const searchIp = ref('')
const newIp = ref('')
let ipList: string[] = []
const filtedIpList = ref<string[]>([])
const testIpList = ['125.32.123.1', '125.32.123.2', '125.32.123.3', '125.32.124.1']

const sendToggleValue = async () => {
  try {
    const response = await $axios().post('/api/ipActive', {
      active: active,
    })
    if (response.data.result) {
      if (active.value) console.log('활성화 성공')
      else console.log('비활성화 성공')
    } else {
      console.log('활성 상태 변경 실패')
    }
  } catch (error) {}
}

const deleteIp = async (ip: string) => {
  try {
    const response = await $axios().post('/api/deleteIp', { ip })
    if (response.data.result) {
      console.log('ip 삭제 성공')
      await getIpList()
    } else console.log('ip 삭제 실패')
  } catch (err) {
    console.log(err)
  }
}

const addIp = async () => {
  try {
    const response = await $axios().post('/api/addIp', { ip: newIp.value })
    if (response.data.result) {
      console.log('ip 추가 성공')
      await getIpList()
    } else console.log('ip 추가 실패')
  } catch (err) {
    console.log(err)
  }
}

const getIpList = async () => {
  try {
    const response = await $axios().get('/api/iplist')
    active.value = response.data.active
    filtedIpList.value = [...response.data.result]
  } catch (error) {
    console.error(error)
    filtedIpList.value = [...testIpList]
    ipList = [...testIpList]
  }
}

watch(searchIp, () => {
  if (searchIp.value === '') {
    // searchIp가 비어있는 경우 모든 값을 담음
    filtedIpList.value = [...ipList]
  } else {
    // searchIp로 시작하는 IP 주소만 필터링
    filtedIpList.value = [...ipList.filter((ip) => ip.startsWith(searchIp.value))]
  }
})

onMounted(async () => {
  await getIpList()
})
</script>
<template>
  <div>
    <div class="title flex items-center q-pl-md">
      <div>Setting > <strong>IP Blocking</strong></div>
    </div>

    <div class="top col-12 row q-pa-sm">
      <div class="col-2 row items-center justify-center">
        <q-toggle color="main" v-model="active" :onUpdate="() => sendToggleValue()" />
        <q-icon :color="active ? 'positive' : 'negative'" :name="active ? 'verified_user' : 'remove_moderator'" size="sm"></q-icon>
      </div>

      <q-input outlined class="col-9" color="main" v-model="searchIp" dense>
        <template v-slot:prepend>
          <q-icon name="search" />
        </template>
      </q-input>

      <q-btn flat color="main" class="col-1" @click="toggleInput" :icon="showInput ? 'expand_less' : 'add'"></q-btn>
    </div>

    <div class="row justify-end items-center" :class="showInput ? 'input-box input-box-after' : 'input-box'">
      <q-input
        class="col-9 noPadding"
        outlined
        dense
        v-if="showInput"
        v-model:model-value="newIp"
        label="###.###.###.###"
        :rules="[
          (val) => !!val || '* Required',
          (val) => {
            const ipv4Pattern = /^(\d{1,3}\.){3}\d{1,3}$/ // IPv4 형식을 검증하는 정규 표현식
            return ipv4Pattern.test(val) // 정규 표현식과 매치되는지 확인
          },
        ]"
      >
        <template v-slot:prepend>
          <q-icon name="public" />
        </template>
      </q-input>
      <q-btn flat dense color="main" class="col-1" @click="() => addIp()" icon="add"></q-btn>
    </div>

    <IPList v-model:active="active" v-model:searchIp="searchIp" v-model:filtedIpList="filtedIpList" @deleteIp="deleteIp" />
  </div>
</template>
<style lang="scss" scoped>
.top {
  border-bottom: solid 1px;
  border-color: #bcbcbc;
  background: #f3f4f5;
}

.input-box {
  height: 0;
  border-bottom: solid 1px;
  border-color: #bcbcbc;
  background: #f3f4f5;
  transition-property: all;
  transition-duration: 0.5s;
  overflow: hidden;
  font-size: 20px;
}
.input-box-after {
  height: 56px !important;
  padding: 7px;
}
.noPadding {
  padding: 0 !important;
}
</style>
