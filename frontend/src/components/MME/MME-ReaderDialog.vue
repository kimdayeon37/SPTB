<script setup lang="ts">
import { ref } from 'vue'
import type { ReaderData } from '../../types'

const emits = defineEmits<{
  addReader: [reader: ReaderData]
  addReaderToggleHandler: [bool?: boolean]
}>()
const newReader = ref<ReaderData>({
  area: 'Coil',
  byteSwap: false,
  wordSwap: false,
})
const areaOptions = ['Coil', 'DiscreteInput', 'InputRegister', 'HoldingRegister']
</script>
<template>
  <q-dialog>
    <q-card class="q-pa-md dialog-box">
      <q-form
        @submit="
          () => {
            emits('addReader', newReader)
            emits('addReaderToggleHandler', false)
          }
        "
      >
        <div class="row height">
          <div class="col-6 flex items-center">Name</div>
          <q-input outlined v-model="newReader.name" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
        </div>
        <div class="row height">
          <div class="col-6 height flex items-center">Slave ID</div>
          <q-input
            outlined
            v-model="newReader.slaveId"
            dense
            class="col-6"
            label="1 ~ 3"
            :rules="[(val) => !!val || '* Required', (val) => (1 <= val && val <= 3) || 'Please check range']"
          />
        </div>
        <div class="row height">
          <div class="col-6 height flex items-center">Area</div>
          <q-select outlined v-model="newReader.area" mask="#####" dense :options="areaOptions" class="col-6" :rules="[(val) => !!val || '* Required']" />
        </div>
        <div class="row height">
          <div class="col-6 height flex items-center">Address</div>
          <q-input
            outlined
            class="col-6"
            v-model="newReader.readAddress"
            dense
            label="0 ~ 49999"
            :rules="[(val) => !!val || '* Required', (val) => (0 <= val && val <= 4999) || 'Please check range']"
          />
        </div>
        <div class="row height">
          <div class="col-6 height flex items-center">Quantity</div>
          <q-input
            outlined
            v-model="newReader.quantity"
            dense
            class="col-6"
            label="1 ~ 9999"
            :rules="[(val) => !!val || '* Required', (val) => (1 <= val && val <= 9999) || 'Please check range']"
          />
        </div>
        <div class="row height">
          <div class="col-6 height flex items-center">Scan Time(ms)</div>
          <q-input
            outlined
            v-model="newReader.scanTime"
            dense
            class="col-6"
            label="1 ~ 1000"
            :rules="[(val) => !!val || '* Required', (val) => (1 <= val && val <= 1000) || 'Please check range']"
          />
        </div>
        <div class="row height">
          <div class="col-6 height flex items-center">Byte Swap</div>
          <q-toggle color="main" v-model="newReader.byteSwap" />
        </div>
        <div class="row height">
          <div class="col-6 height flex items-center">Word Swap</div>
          <q-toggle color="main" v-model="newReader.wordSwap" />
        </div>
        <div class="row justify-evenly items-center">
          <q-btn label="추가" type="submit" color="main" padding="xs lg"></q-btn>
          <q-btn label="취소" flat padding="xs lg" color="red" v-close-popup></q-btn>
        </div>
      </q-form>
    </q-card>
  </q-dialog>
</template>
