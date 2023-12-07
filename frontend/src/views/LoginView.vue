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
import { useQuasar } from 'quasar'
import { ref } from 'vue'
import { $axios } from '@/axios/index';
import { useRouter } from 'vue-router';
import { useUserStore } from "../store/userStore";
import { jwtDecode } from 'jwt-decode'
import LoginState from '@/components/LoginState.vue';
import { useIdStore } from '@/store/idStore'

const $q = useQuasar()
const router = useRouter();
const userStore = useUserStore();

const id = ref<string>('');
const pw = ref<string>('');

const logoutUser = () => {
    userStore.clearUsername();
    router.push({ path: '/Login' });
}

const isUserLogin = () => {
    return userStore.isLogin;
};

interface IPayload {
            iat: number
            exp: number
        }
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
        router.push({ path: '/Home' });

        try {
            const idStore = useIdStore()
            console.log(idStore.clientId)
            if (idStore.clientId == ''){
            const response = await $axios().get('/api/getClientId')
            const clientId = response.data
            idStore.clientId = clientId
            console.log('Client ID set in idStore:', clientId)
            }
        } catch (error) {
            console.error('Error fetching client ID:', error)
        }

        const decoded = jwtDecode<IPayload>(data.token);
        if (decoded) {
            const expirationTime = decoded.exp;

            if (expirationTime) {
                const expirationDate = new Date(expirationTime * 1000); 
                const notificationTime = new Date(expirationTime * 1000 - (10 * 1000)); 
                const currentTime = new Date();

                console.log('Token expiration time:', expirationDate);
                console.log('Notification time:', notificationTime);
                console.log('Current time:', currentTime);
                const secondsUntilExpiration = Math.floor((expirationDate.getTime() - notificationTime.getTime()) / 1000);
                const showDialog = () => {
                    
                    console.log('Seconds until expiration:', secondsUntilExpiration);
                    $q.dialog({
                        title: `로그인이 ${secondsUntilExpiration}초 후 만료됩니다. 연장하시겠습니까?`,
                        message: 'CANCEL을 누르면 로그아웃 됩니다.',
                        cancel: true,
                        persistent: true
                    }).onOk(async () => {
                        let headers = {
                            Authorization: 'Bearer ' + userStore.token,
                            'Content-Type': 'application/json',
                        }
                        isUserLogin();
                        console.log(userStore.token)
                        const axiosResponse = await $axios().post("/api/regenToken", { id: userStore.id }, { headers });
                        const regenToken = axiosResponse.data.token;
                        console.log(regenToken)
                        userStore.setToken(regenToken);
                        setTimeout(showDialog, expirationDate.getTime() - currentTime.getTime());
                    }).onCancel(() => {
                        logoutUser();
                    }).onDismiss(() => {
                    })
                }
                setTimeout(showDialog, expirationDate.getTime() - currentTime.getTime());
            } else {
                console.error('Token does not contain an expiration time (exp).');
            }
        } else {
            console.error('Failed to decode the token.');
        }

        // if (decoded) {
        //     const expiredMs = decoded.exp * 1000

        //     if (expiredMs) {
        //         //const expiredDt = new Date(expiredMs);
        //         // const currentDt = new Date();
        //         // const currentMs = currentDt.valueOf()
        //         const currentMs = Date.now();
        //         console.log(`expiredMs : ${expiredMs}`)
        //         //console.log(`currentDt : ${currentDt}`)
        //         console.log(`currentMs : ${currentMs}`)
        //         const remainingTime = expiredMs - currentMs;
        //         console.log(`remainingTime : ${remainingTime}`);

        //         setTimeout(() => {
        //             if (remainingTime <= 14 * 60 * 1000) {
        //                 console.log('로그인 만료 10초전')
        //                 console.log(remainingTime)
        //                 // const response = await axios.post("/api/regenToken", { id: userData.id, headers });
        //                 // const regenToken = response.data.regenToken;
        //                 // console.log(regenToken)
        //                 // userStore.setToken(regenToken);
        //                 // localStorage.setItem("access_token", regenToken);

        //                 $q.dialog({
        //                     title: `로그인이 10초 후 만료됩니다. 연장하시겠습니까?`,
        //                     message: 'CANCEL을 누르면 로그아웃 됩니다.',
        //                     cancel: true,
        //                     persistent: true
        //                 }).onOk(() => {
        //                     isUserLogin();
        //                 }).onCancel(() => {
        //                     logoutUser();
        //                 }).onDismiss(() => {
        //                 })
        //             }
        //         });
        //     } else {
        //         console.error('Token does not contain an expiration time (exp).');
        //     }
        // } else {
        //     console.error('Failed to decode the token.');
        // }
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
