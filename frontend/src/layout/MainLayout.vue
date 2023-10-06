<script setup lang="ts">
import { ref } from 'vue'
import SystemLog from '../components/System-Log.vue'

import { useToggleStore } from '../store/modules/settingtoggle'

const leftDrawerOpen = ref(false)
const toggleLeftDrawer = () => {
  leftDrawerOpen.value = !leftDrawerOpen.value
}

const toggleStore = useToggleStore()
const settingToggleHandler = () => {
  toggleStore.toggleSetting()
}
</script>
<template>
  <q-layout view="hHh lpR fFf">
    <q-header reveal bordered class="bg-main text-white">
      <q-toolbar>
        <q-btn dense flat round icon="menu" @click="toggleLeftDrawer" />

        <q-toolbar-title>
          <div class="q-ml-sm text-bold">Naonworks SPTB</div>
        </q-toolbar-title>
      </q-toolbar>
    </q-header>

    <q-drawer show-if-above v-model="leftDrawerOpen" side="left" bordered class="sidebar" :width="220">
      <q-list class="">
        <q-item to="/" clickable v-ripple>
          <q-item-section avatar>
            <q-icon name="home" />
          </q-item-section>
          <q-item-section> home </q-item-section>
        </q-item>
        <q-item to="/Log" v-ripple>
          <q-item-section avatar>
            <q-icon name="search" />
          </q-item-section>
          <q-item-section> Log Search </q-item-section>
        </q-item>
        <q-expansion-item expand-separator label="Modbus" default-opened :content-inset-level="0.5">
          <q-item to="/Modbus/MasterEthernet"><q-item-section @click="settingToggleHandler">Master Ethernet</q-item-section></q-item>
          <q-item to="/Modbus/SlaveEthernet"><q-item-section @click="settingToggleHandler">Slave Ethernet</q-item-section></q-item>
          <q-item to="/Modbus/MasterSerial"><q-item-section @click="settingToggleHandler">Master Serial</q-item-section></q-item>
          <q-item to="/Modbus/SlaveSerial"><q-item-section @click="settingToggleHandler">Slave Serial</q-item-section></q-item>
        </q-expansion-item>
        <q-expansion-item :content-inset-level="0.5" expand-separator label="OPC-UA" default-opened>
          <q-item to="/OPCUA/Client"><q-item-section>Client</q-item-section></q-item>
          <q-item to="/OPCUA/Server"><q-item-section>Server</q-item-section></q-item>
        </q-expansion-item>
      </q-list>
    </q-drawer>

    <q-page-container>
      <q-page>
        <router-view style="min-height: inherit" />
      </q-page>
    </q-page-container>

    <q-footer reveal bordered class="bg-grey-8 text-white">
      <q-toolbar>
        <div class="system-log-container row">
          <SystemLog />
        </div>
      </q-toolbar>
    </q-footer>
  </q-layout>
</template>
<style>
.sidebar {
  background: #f5f5f5;
  overflow: hidden;
}

.text-gray {
  color: gray !important;
}

.q-toolbar {
  padding: 0px !important;
}

.table-container {
  background: #fafafa;
  position: relative;
}

.table {
  position: absolute;
  height: 100%;
  width: 100%;
}

.table .q-table__top,
.table .q-table__bottom,
.table thead tr:first-child th

/* bg color is important for th; just specify one */ {
  background-color: #ffffff;
}

.table thead tr th {
  position: sticky;
  z-index: 1;
}

.table thead tr:first-child th {
  top: 0;
}

.system-log-container {
  height: 22vh;
  width: 100%;
}
</style>
virtual-scroll
