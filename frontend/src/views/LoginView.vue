<template>
    <div class="column items-center">
        <div class="col q-pa-lg">
            <q-img src="/src/assets/login.png" style="width: 300px; height: 50px;" />
            <LoginState v-if="userStore.isLogin()" />
        </div>
        
        <div class="col" v-if="!userStore.isLogin()" style="max-width: 400px; margin-top: -100px;">
            <h6><b>로그인</b></h6>
            <q-form @submit="onSubmit" class="q-gutter-md">
                <q-input filled v-model="id" label="아이디 *" lazy-rules
                    :rules="[(val: string | any[]) => val && val.length > 0 || '아이디를 입력해주세요']" />
                <!-- <q-input filled v-model="pw" label="비밀번호 *" lazy-rules :rules="[
                    (val) => val && val.length > 5 || '비밀번호는 5자 이상 입력해주세요',
                    (val) => /(?=.*\d)/.test(val) || '숫자를 포함해야 합니다',
                    (val) => /([!@$%])/.test(val) || '특수문자를 포함해야 합니다',
                ]" /> -->
                <q-input filled v-model="pw" type="password" label="비밀번호 *" lazy-rules
                    :rules="[(val: string | any[]) => val && val.length > 0 || '비밀번호를 입력해주세요']" />
                <div>
                    <q-btn label="login" type="submit" color="primary" class="q-ml-sm" />
                    <q-btn label="signup" to="/Signup" color="secondary" class="q-ml-lg" />
                </div>
            </q-form>
        </div>
    
    </div>
</template>
  
<script setup lang="ts">
import { $axios } from '@/axios/index';
import LoginState from '@/components/LoginState.vue';
import { useQuasar } from 'quasar';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from "../store/userStore";
import {setClientId} from '@/utils/api_auth'

const $q = useQuasar()
const router = useRouter();
const userStore = useUserStore();

const id = ref<string>('');
const pw = ref<string>('');

const onSubmit = async () => {
    try {
        const userData = {
            id: id.value,
            pw: pw.value,
        };

        const { data } = await $axios().post("/api/login", userData);
        userStore.setUsername(userData.id);
        userStore.setToken(data.token);

        console.log(data.token);

        $q.notify({
            color: 'green-4',
            textColor: 'white',
            icon: 'cloud_done',
            message: '로그인 성공'
        })
        await setClientId()
        await router.push({ path: '/Home' })
    }
    catch (error) {
        console.log(error);
        $q.notify({
            color: 'red-5',
            textColor: 'white',
            icon: 'warning',
            message: '아이디와 비밀번호가 일치하지 않습니다'
        })
    } finally {
        id.value = '';
        pw.value = '';
    }
};
</script>
