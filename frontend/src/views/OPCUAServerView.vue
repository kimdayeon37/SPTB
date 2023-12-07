<script setup lang="ts">
import { onUnmounted, ref } from 'vue'
import OUSTopbar from '../components/OPCUAServer/OUS-Topbar.vue'
import OUSMemory from '../components/OPCUAServer/OUS-Memory.vue'
import TransLog from '../components/Trans-Log.vue'
import { useStateStore } from '../store/stateStore'
import { $axios } from '@/axios/index'
import { useIdStore } from '../store/idStore'

const viewLogToggle = ref<boolean>(false)
const stateStore = useStateStore()
const idStore = useIdStore()

onUnmounted(() => {
  if (stateStore.state)
    $axios()
      .post('/api/exit', {
        id: idStore.clientId,
      })
      .catch((err) => {
        console.log(err)
      })
})
</script>
<template>
  <div class="column">
    <OUSTopbar v-model:view-log-toggle="viewLogToggle" />

    <OUSMemory v-show="!viewLogToggle" />

    <TransLog v-show="viewLogToggle" />
  </div>
</template>
<style lang="scss"></style>
