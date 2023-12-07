<script setup lang="ts">
import { ref } from 'vue'
import { useOUSNetworkStore } from '../../store/OPCUAServer/OUS-NetworkStore'
import { useToggleStore } from '../../store/modules/settingtoggle'
import type { OUSNetworkData } from '../../types'
const ousNetworkStore = useOUSNetworkStore()
const ousNetworkData = ref<OUSNetworkData>({ users: [] })
const toggleStore = useToggleStore()
const newUser = ref<{ id: string; pw: string }>({ id: '', pw: '' })
const addItem = () => {
  if (newUser.value.id !== '' && newUser.value.pw !== '') {
    ousNetworkData.value.users?.push({ ...newUser.value })
  }
  console.log(ousNetworkData.value.users)
}
const removeItem = (index: number) => {
  ousNetworkData.value.users?.splice(index, 1)
}

const readFileAsBinaryString = (file: Blob) => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()

    reader.onload = () => {
      resolve(btoa(reader.result as string))
    }

    reader.onerror = () => {
      reject(new Error('File read error.'))
    }

    reader.readAsBinaryString(file)
  })
}

const storeNetworkData = async () => {
  try {
    //base64로 변환
    const [certFileToBase64, keyFileToBase64] = await Promise.all([readFileAsBinaryString(ousNetworkData.value.certFile), readFileAsBinaryString(ousNetworkData.value.keyFile)])

    //변환된 데이터 저장
    ousNetworkStore.networkData = {
      ...ousNetworkData.value,

      certFile: certFileToBase64,
      keyFile: keyFileToBase64,
    }

    //store에 저장 후 dialog 닫기
    toggleStore.toggle()

    console.log(ousNetworkStore.networkData)
  } catch (error) {
    console.error(error)
  }
}
</script>
<template>
  <q-dialog v-model="toggleStore.networkDialogToggle">
    <q-card class="q-pa-md dialog-box">
      <q-form
        @submit="
          () => {
            storeNetworkData()
          }
        "
      >
        <div class="row height justify-center text-h6 text-weight-bold">Server 통신 설정</div>
        <div class="row height">
          <div class="col-6 flex items-center">IP</div>
          <q-input
            ref="inputRef"
            outlined
            v-model="ousNetworkData.ip"
            dense
            class="col-6"
            label="###.###.###.###"
            :rules="[
              (val) => !!val || '* Required',
              (val) => {
                const ipv4Pattern = /^(\d{1,3}\.){3}\d{1,3}$/ // IPv4 형식을 검증하는 정규 표현식
                return ipv4Pattern.test(val) // 정규 표현식과 매치되는지 확인
              },
            ]"
          />
        </div>
        <div class="row height">
          <div class="col-6 flex items-center">Port</div>
          <q-input
            ref="inputRef"
            outlined
            type="number"
            v-model="ousNetworkData.port"
            dense
            class="col-6"
            label="0 ~ 65535"
            :rules="[(val) => !!val || '* Required', (val) => (0 <= val && val <= 65535) || 'Please check range']"
          />
        </div>
        <div class="row height">
          <div class="col-6 flex items-center">CertFile</div>
          <q-file outlined v-model="ousNetworkData.certFile" dense class="col-6" :rules="[(val) => !!val || '* Required']">
            <template v-slot:prepend>
              <q-icon name="attach_file" />
            </template>
          </q-file>
        </div>
        <div class="row height">
          <div class="col-6 flex items-center">KeyFile</div>
          <q-file outlined v-model="ousNetworkData.keyFile" dense class="col-6" :rules="[(val) => !!val || '* Required']">
            <template v-slot:prepend>
              <q-icon name="attach_file" />
            </template>
          </q-file>
        </div>

        <div class="col-12 flex items-center justify-center">Users</div>
        <q-item>
          <q-item-section class="items-center"> ID </q-item-section>
          <q-item-section class="items-center"> PW </q-item-section>
          <q-item-section side> </q-item-section>
        </q-item>
        <div class="col-12 column">
          <q-item>
            <q-item-section>
              <q-input v-model="newUser.id" dense square filled placeholder="ID" class="col input-box" />
            </q-item-section>
            <q-item-section> <q-input v-model="newUser.pw" dense square filled placeholder="PW" class="col input-box" /></q-item-section>
            <q-item-section side> <q-btn flat color="main" size="md" padding="2px 12px 0px" class="q-mx-sm" @click="addItem"> 추가 </q-btn></q-item-section>
          </q-item>

          <div v-for="(item, index) in ousNetworkData.users" :key="index">
            <q-item>
              <q-item-section>
                {{ item.id }}
              </q-item-section>
              <q-item-section>
                {{ item.pw }}
              </q-item-section>
              <q-item-section side>
                <q-btn flat color="negative" size="md" padding="2px 12px 0px" class="q-mx-sm" @click="removeItem(index)"> 삭제 </q-btn>
              </q-item-section>
            </q-item>
          </div>
        </div>
        <div class="row justify-evenly items-center">
          <q-btn label="적용" type="submit" color="main" padding="xs lg"></q-btn>
          <q-btn label="취소" flat padding="xs lg" color="red" v-close-popup></q-btn>
        </div>
      </q-form>
    </q-card>
  </q-dialog>
</template>
