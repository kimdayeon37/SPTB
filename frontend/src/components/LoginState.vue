<template>
    <div class="column items-center">
        <div class="col">
            <h6><b>로그인 완료!</b></h6>
            <h6><b>{{ userStore.id }} 님</b></h6>
        </div>
    </div>
</template>
  
<script setup lang="ts">
import { useUserStore } from "@/store/userStore";

const userStore = useUserStore();

</script>
