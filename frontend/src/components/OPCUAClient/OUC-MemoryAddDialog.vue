<script setup lang="ts">
import { ref } from 'vue'
import type { OUCMemoryData } from '../../types'

const newNode = ref<OUCMemoryData>({
  type: 'Subscription',
  discardOldest: 'True',
})
const discardOldestOptions = ['True', 'False']

const emits = defineEmits(['addWriter', 'update:modelValue'])
</script>
<template>
  <q-dialog>
    <q-card class="q-pa-md dialog-box">
      <q-form
        @submit="
          () => {
            emits('addWriter', newNode)
            console.log(newNode)
            emits('update:modelValue', false)
          }
        "
      >
        <div class="row height justify-center text-h6 text-weight-bold">Subscription 추가</div>

        <div class="row height">
          <div class="col-6 height flex items-center">Node Id</div>
          <q-input outlined v-model="newNode.nodeId" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
        </div>
        <div class="row height">
          <div class="col-6 height flex items-center">Sampling Interval</div>
          <q-input
            outlined
            type="number"
            v-model="newNode.interval"
            dense
            class="col-6"
            label="1 ~ 1000"
            :rules="[(val) => !!val || '* Required', (val) => (1 <= val && val <= 1000) || 'Please check range']"
          />
        </div>
        <div class="row height">
          <div class="col-6 height flex items-center">Queue Size</div>
          <q-input
            outlined
            type="number"
            v-model="newNode.queueSize"
            dense
            class="col-6"
            label="1 ~ 1000"
            :rules="[(val) => !!val || '* Required', (val) => (1 <= val && val <= 1000) || 'Please check range']"
          />
        </div>
        <div class="row height">
          <div class="col-6 height flex items-center">Discard Oldest</div>
          <q-select outlined v-model="newNode.discardOldest" dense class="col-6" :options="discardOldestOptions" :rules="[(val) => !!val || '* Required']" />
        </div>

        <div class="row justify-evenly items-center">
          <q-btn label="적용" type="submit" color="main" padding="xs lg"></q-btn>
          <q-btn label="취소" flat padding="xs lg" color="red" v-close-popup></q-btn>
        </div>
      </q-form>
    </q-card>
  </q-dialog>
</template>
<style scoped>
.q-item {
  padding: 0 !important;
  min-height: 30px !important;
}
</style>
