<script setup lang="ts">
import { useQuasar } from 'quasar'
import { onMounted } from 'vue'
import { jwtDecode } from 'jwt-decode'
import { useUserStore } from "@/store/userStore";
import { $axios } from '@/axios/index';
import { useRouter } from 'vue-router';

const userStore = useUserStore();
const $q = useQuasar()
const router = useRouter();

const isUserLogin = () => {
    return userStore.isLogin;
};
const logoutUser = () => {
    userStore.clearUsername();
    router.push({ path: '/Login' });
}
interface IPayload {
            iat: number
            exp: number
        }
        

onMounted(()=> {
    console.log(userStore.token)
    if (userStore.token) {
    const decoded = jwtDecode<IPayload>(userStore.token);
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
    }
})
</script>
<template>
  <RouterView />
</template>
<style>
.text-main {
  color: #283b59 !important;
}
.bg-main {
  background: #283b59 !important;
}
</style>
