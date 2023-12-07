<template>
  <div class="column items-center">
    <div class="q-pa-md" style="max-width: 400px">
      <div class="row height justify-center text-h6 text-weight-bold">회원가입</div>
      <q-form @submit="onSubmit" class="q-gutter-md" ref="form">
        <!-- <q-input filled v-model="signupData.name" label="이름 *" lazy-rules
                :rules="[val => val && val.length > 0 || '이름을 입력해주세요']" />
            <q-input filled v-model="signupData.email" label="이메일 *" lazy-rules :rules="[val => val && val.length > 0 || '이메일 형식으로 입력해주세요',
            (val) => {
                const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                return emailRegex.test(val);
            }]" /> -->
        <q-input filled v-model="id" label="아이디 *" lazy-rules :rules="[(val) => (val && val.length > 0) || '아이디를 입력해주세요']" />

        <q-input filled v-model="pw" label="비밀번호 *" lazy-rules :rules="[(val: string | any[]) => (val && val.length > 0) || '비밀번호를 입력해주세요']" />
        <div class="column">
          <q-btn label="Signup" type="submit" color="secondary" />
        </div>
      </q-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

const id = ref<string>('')
const pw = ref<string>('')

const onSubmit = async () => {
  try {
    const userData = {
      id: id.value,
      pw: pw.value,
    }

    const { data } = await axios.post('/api/signup', userData)
    console.log(data.id)
    alert('회원가입이 완료되었습니다. 로그인 화면으로 돌아갑니다')
    router.push({ path: '/Login' })
    id.value = ''
    pw.value = ''
  } catch (error) {
    console.log(error)
  }
}
</script>
