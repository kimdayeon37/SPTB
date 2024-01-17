<script setup lang="ts">
import { ref } from 'vue'
import type { SmtpConfig, SmtpData } from '../../types.ts'
import { $axios } from '@/axios'
import { useQuasar } from 'quasar'
const $q = useQuasar()
const newSmtpData = ref<SmtpData>({})
const smtpConfig = ref<SmtpConfig>({ host: 'smtp.naver.com', port: 465 })
const sendSmtp = async () => {
  try {
    const result = $axios().post('/api/smtp', { config: smtpConfig.value, msg: newSmtpData.value })
    $q.notify({
      color: 'green-4',
      textColor: 'white',
      icon: 'cloud_done',
      message: 'smtp 메일 전송 완료',
    })
  } catch (err) {
    $q.notify({
      color: 'red-5',
      textColor: 'white',
      icon: 'warning',
      message: 'smtp 메일 전송 실패',
    })
    console.log(err)
  }
}
</script>
<template>
  <div class="row q-mx-xl border-x q-pt-md">
    <div class="col-3 row justify-center items-center text"><div>host</div></div>
    <div class="col-9 q-pb-sm">
      <q-input class="col-9 text" filled dense v-model="smtpConfig.host" />
    </div>

    <div class="col-3 row justify-center items-center text"><div>port</div></div>
    <div class="col-9 q-pb-sm">
      <q-input class="col-9 text" type="number" filled dense v-model="smtpConfig.port" />
    </div>

    <div class="col-3 row justify-center items-center text"><div>username</div></div>
    <div class="col-9 q-pb-sm">
      <q-input class="col-9 text" filled dense v-model="smtpConfig.username" />
    </div>

    <div class="col-3 row justify-center items-center text"><div>password</div></div>
    <div class="col-9 q-pb-sm">
      <q-input class="col-9 text" filled type="password" dense v-model="smtpConfig.password" />
    </div>

    <div class="col-3 row justify-center items-center text">받는 이</div>
    <div class="col-9 q-pb-sm">
      <q-input class="col-9 text" filled dense v-model="newSmtpData.to" />
    </div>

    <div class="col-3 row justify-center items-center text">제목</div>
    <div class="col-9 q-pb-sm">
      <q-input class="col-9 text" filled dense v-model="newSmtpData.subject" />
    </div>

    <div class="col-3 row justify-center items-center text">내용</div>
    <div class="col-9 q-pb-sm">
      <q-input class="col-9 text" type="textarea" filled dense v-model="newSmtpData.text" />
    </div>

    <div class="col-12 row justify-center">
      <q-btn
        @click="
          () => {
            sendSmtp()
          }
        "
        >전송</q-btn
      >
    </div>
  </div>
</template>
<style scoped>
.text {
  font-size: 16px;
}
</style>
