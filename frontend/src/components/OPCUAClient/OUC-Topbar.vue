<script setup lang="ts">
import { useToggleStore } from '../../store/modules/settingtoggle'
import OUCNetworkDialog from './OUC-NetworkDialog.vue'

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
        <q-breadcrumbs class="text-primary">
          <template v-slot:separator>
            <q-icon size="1.5em" name="chevron_right" color="primary" />
          </template>
          <q-breadcrumbs-el label="OPC-UA" />
          <q-breadcrumbs-el label="Client" />
        </q-breadcrumbs>
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
      <OUCNetworkDialog />
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
