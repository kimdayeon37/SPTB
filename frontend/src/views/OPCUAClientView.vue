<script setup lang="ts">
import { onUnmounted, ref } from 'vue'
import OUCTopbar from '../components/OPCUAClient/OUC-Topbar.vue'
import OUCMemory from '../components/OPCUAClient/OUC-Memory.vue'
import OUCReadWrite from '../components/OPCUAClient/OUC-ReadWrite.vue'

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
    <OUCTopbar v-model:view-log-toggle="viewLogToggle" />
    <div v-show="!viewLogToggle" class="col row">
      <OUCMemory style="flex: 1" />
      <OUCReadWrite style="flex: 1" />
    </div>
    <div v-show="viewLogToggle" class="col row">
      <TransLog />
    </div>
  </div>
</template>
<style></style>
