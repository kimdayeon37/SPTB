<script setup lang="ts">
import { useToggleStore } from '../../store/modules/settingtoggle'
import OUSNetworkDialog from './OUS-NetworkDialog.vue'

const props = defineProps<{
  viewLogToggle: boolean
}>()
const emits = defineEmits<{
  'update:viewLogToggle': [bool: boolean]
}>()

const toggleStore = useToggleStore()
toggleStore.networkDialogToggle = false
toggleStore.toggle()
</script>
<template>
  <div class="topbar-container">
    <div class="column">
      <div class="title flex items-center q-pl-md">
        <div>OPC-UA > <strong>Server</strong></div>
      </div>
      <div class="menu-bar row items-center">
        <q-btn :outline="!toggleStore.networkDialogToggle" rounded size="md" padding="2px 12px" color="main" class="setting-btn q-mx-sm" @click="toggleStore.toggle()">
          통신 설정
        </q-btn>
        <q-btn :outline="props.viewLogToggle" rounded size="md" padding="2px 12px" color="main" class="setting-btn q-mx-sm" @click="emits('update:viewLogToggle', false)">
          메세지
        </q-btn>
        <q-btn :outline="!props.viewLogToggle" rounded size="md" padding="2px 12px" color="main" class="setting-btn q-mx-sm" @click="emits('update:viewLogToggle', true)">
          로그
        </q-btn>
      </div>
      <OUSNetworkDialog />
    </div>
  </div>
</template>
<style scoped>
.title {
  height: 40px;
  border-bottom: solid 1px;
  border-color: #bcbcbc;
  background: #f3f4f5;
}
</style>
