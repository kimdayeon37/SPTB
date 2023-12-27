<script setup lang="ts">
import { ref } from 'vue'
const active = ref(false)
const searchIp = ref('')
</script>
<template>
  <div>
    <div class="title flex items-center q-pl-md">
      <div>Setting > <strong>IP Blocking</strong></div>
    </div>

    <div class="col-12 row q-ma-sm">
      <div class="col-2 row items-center justify-center">
        <q-toggle color="main" v-model="active" />
        <q-icon :color="active ? 'positive' : 'negative'" :name="active ? 'verified_user' : 'remove_moderator'" size="sm"></q-icon>
      </div>

      <q-input outlined class="col-9" color="main" v-model="searchIp" dense>
        <template v-slot:prepend>
          <q-icon name="search" />
        </template>
        <template v-slot:append>
          <q-btn flat label="검색" />
        </template>
      </q-input>

      <q-btn flat color="main" class="col-1"> 추가 </q-btn>
    </div>

    <q-list separator>
      <q-item clickable v-ripple :active="active">
        <q-item-section avatar>
          <q-icon name="public" />
        </q-item-section>
        <q-item-section>Active</q-item-section>
        <q-item-section side><q-btn flat color="negative"> 삭제 </q-btn></q-item-section>
      </q-item>
      <q-item clickable v-ripple :active="active">
        <q-item-section avatar>
          <q-icon name="signal_wifi_off" />
        </q-item-section>
        <q-item-section>Active</q-item-section>
        <q-item-section side>Side</q-item-section>
      </q-item>
    </q-list>
  </div>
</template>
