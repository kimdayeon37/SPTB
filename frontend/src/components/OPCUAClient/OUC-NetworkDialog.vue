<script setup lang="ts">
import { ref } from 'vue'
import { useOUCNetworkStore } from '../../store/OPCUAClient/OUC-NetworkStore'
import { useToggleStore } from '../../store/modules/settingtoggle'
import { type OUCNetworkData } from '../../types'
const oucNetworkStore = useOUCNetworkStore()
const toggleStore = useToggleStore()
const oucNetworkData = ref<OUCNetworkData>({
  endpointurl: 'opc.tcp://127.0.0.1:4880',
  securitymode: 'None',
  securitypolicy: 'None',
  useridentify: 'Anonymous',
  username: '',
  applicationuri: 'urn:open62541.server.application',
})

const securitymodeOptions = ['None', 'Sign', 'SignAndEncrypt']
const securitypolicyOptions = ['None', 'Basic256', 'Basic128Rsa15', 'Basic256Rha256']
const useridentifyOptions = ['Anonymous', 'UserName']

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
    const [certFileToBase64, keyFileToBase64] = await Promise.all([readFileAsBinaryString(oucNetworkData.value.certFile), readFileAsBinaryString(oucNetworkData.value.keyFile)])

    //변환된 데이터 저장
    oucNetworkStore.networkData = {
      ...oucNetworkData.value,

      certFile: certFileToBase64,
      keyFile: keyFileToBase64,
    }

    //store에 저장 후 dialog 닫기
    toggleStore.toggle()

    console.log(oucNetworkStore.networkData)
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
        <div class="row height justify-center text-h6 text-weight-bold">Client 통신 설정</div>
        <div class="row height">
          <div class="col-6 height flex items-center">Endpoint URL</div>
          <q-input outlined v-model="oucNetworkData.endpointurl" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
        </div>
        <div class="row height">
          <div class="col-6 height flex items-center">Security Mode</div>
          <q-select outlined v-model="oucNetworkData.securitymode" :options="securitymodeOptions" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
        </div>
        <div class="row height">
          <div class="col-6 height flex items-center">Security Policy</div>
          <q-select outlined v-model="oucNetworkData.securitypolicy" :options="securitypolicyOptions" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
        </div>
        <div class="row height">
          <div class="col-6 height flex items-center">User Identify</div>
          <q-select outlined v-model="oucNetworkData.useridentify" :options="useridentifyOptions" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
        </div>
        <div class="row height">
          <div class="col-6 height flex items-center">User Name</div>
          <q-input outlined v-model="oucNetworkData.username" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
        </div>
        <div class="row height">
          <div class="col-6 height flex items-center">Password</div>
          <q-input outlined v-model="oucNetworkData.password" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
        </div>
        <div class="row height">
          <div class="col-6 height flex items-center">CertFile</div>
          <q-file outlined v-model="oucNetworkData.certFile" label="Pick files" filled counter dense class="col-6" :rules="[(val) => !!val || '* Required']">
            <template v-slot:prepend>
              <q-icon name="attach_file" />
            </template>
          </q-file>
        </div>

        <div class="row height">
          <div class="col-6 height flex items-center">KeyFile</div>
          <q-file outlined v-model="oucNetworkData.keyFile" label="Pick files" filled counter dense class="col-6" :rules="[(val) => !!val || '* Required']">
            <template v-slot:prepend>
              <q-icon name="attach_file" />
            </template>
          </q-file>
        </div>

        <div class="row height">
          <div class="col-6 height flex items-center">applicationUrl</div>
          <q-input outlined v-model="oucNetworkData.applicationuri" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
        </div>
        <div class="row justify-evenly items-center">
          <q-btn label="적용" type="submit" color="main" padding="xs lg"></q-btn>
          <q-btn label="취소" flat padding="xs lg" color="red" v-close-popup></q-btn>
        </div>
      </q-form>
    </q-card>
  </q-dialog>
</template>
