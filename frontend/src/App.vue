<script setup lang="ts">
// import { onMounted } from "vue";
// import { useIdStore } from "./store/idStore";
// import axios from "axios";
// const idStore = useIdStore();
// let clientId: string;
// onMounted(async () => {
//   await axios.get("/api/modbus/getClientId").then((res) => {
//     clientId = res.data;

//     idStore.set(clientId);

//     console.log(idStore.get());
//   });
// });
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
