<script setup lang="ts">
const props = defineProps<{
  active: boolean
  searchIp: string
  filtedIpList: string[]
}>()
const emits = defineEmits(['deleteIp'])
</script>
<template>
  <q-list separator>
    <q-item v-for="(ip, index) in filtedIpList" :key="index" clickable :active="props.active">
      <q-item-section avatar>
        <q-icon name="public" />
      </q-item-section>
      <q-item-section>{{ ip }}</q-item-section>
      <q-item-section side>
        <q-btn flat color="negative" @click="() => emits('deleteIp', ip)"> 삭제 </q-btn>
      </q-item-section>
    </q-item>
    <div v-if="props.filtedIpList.length === 0" class="row justify-center"><strong class="q-py-md">해당하는 IP는 없습니다.</strong></div>
  </q-list>
</template>
