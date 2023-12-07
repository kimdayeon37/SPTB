<script setup lang="ts">
import { ref, watch } from 'vue'
import type { MethodArgumentData, ReadWriterData } from '../../types'

const emits = defineEmits(['addWriter', 'update:modelValue'])

const newReadWriter = ref<ReadWriterData>({
  type: 'Read NodeId Value',
  inputArguments: [],
  invalidmsgtype: false,
  invalidmsglength: false,
  invalidmsgchunk: false,
})
const typeOptions = ['Read NodeId Value', 'Write NodeId Value', 'Method Call', 'RawBuffer Send', 'Browse', 'Historical Read']

const argumentDataTypeOptions = ['Boolean', 'Int8', 'UInt8', 'Int16', 'UInt16', 'Int32', 'UInt32', 'Int64', 'UInt64', 'Float', 'Double', 'String']
const newInputArgument = ref<MethodArgumentData>({
  dataType: 'Boolean',
  size: '',
})

const addItemToInputArguments = () => {
  if (newReadWriter.value && newReadWriter.value.inputArguments) {
    newReadWriter.value.inputArguments.push(newInputArgument.value)
  }
  console.log(newReadWriter.value.inputArguments)
}

const removeItem = (index: number) => {
  if (newReadWriter.value && newReadWriter.value.inputArguments) {
    newReadWriter.value.inputArguments.splice(index, 1)
  }
}
watch(
  () => newReadWriter.value.type,
  () => {
    if (newReadWriter.value && newReadWriter.value.inputArguments) {
      newReadWriter.value.inputArguments.length = 0
    }
  }
)
</script>
<template>
  <q-dialog>
    <q-card class="q-pa-md dialog-box">
      <q-form
        @submit="
          () => {
            emits('addWriter', newReadWriter)
            console.log(newReadWriter)
            emits('update:modelValue', false)
            //addArgument(newReadWriter.inputArguments, newInputArgument);
          }
        "
      >
        <div class="row height">
          <div class="col-6 flex items-center">Name</div>
          <q-input ref="inputRef" outlined v-model="newReadWriter.name" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
        </div>
        <div class="row height">
          <div class="col-6 flex items-center">Type</div>
          <q-select outlined v-model="newReadWriter.type" dense class="col-6" :options="typeOptions" />
        </div>
        <!-- Read NodeId Value -->
        <template v-if="newReadWriter.type === typeOptions[0]">
          <div class="row height">
            <div class="col-6 flex items-center">NodeID</div>
            <q-input outlined v-model="newReadWriter.nodeId" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
          </div>
          <div class="row height">
            <div class="col-6 flex items-center">Invalid MSG Type</div>
            <q-toggle outlined v-model="newReadWriter.invalidmsgtype" dense class="col-6" :rules="[(val: any) => !!val || '* Required']" />
          </div>
          <div class="row height">
            <div class="col-6 flex items-center">Invalid MSG Length</div>
            <q-toggle outlined v-model="newReadWriter.invalidmsglength" dense class="col-6" :rules="[(val: any) => !!val || '* Required']" />
          </div>
          <div class="row height">
            <div class="col-6 flex items-center">Invalid MSG Chunk</div>
            <q-toggle outlined v-model="newReadWriter.invalidmsgchunk" dense class="col-6" :rules="[(val: any) => !!val || '* Required']" />
          </div>
        </template>

        <!-- Write NodeId Value -->
        <template v-if="newReadWriter.type === typeOptions[1]">
          <div class="row height">
            <div class="col-6 flex items-center">Node ID</div>
            <q-input outlined v-model="newReadWriter.nodeId" dense class="col-6" />
          </div>
          <div class="row height">
            <div class="col-6 flex items-center">Value</div>
            <div class="col-12 column">
              <q-item>
                <q-item-section v-if="newInputArgument.dataType === argumentDataTypeOptions[0]">
                  <q-input v-model="newInputArgument.size" dense square filled placeholder="true / false" class="col input-box" />
                </q-item-section>
                <q-item-section v-if="newInputArgument.dataType === argumentDataTypeOptions[1]">
                  <q-input
                    v-model="newInputArgument.size"
                    dense
                    square
                    filled
                    placeholder="-128 ~ 127"
                    class="col input-box"
                    :rules="[(val) => (-128 <= val && val <= 127) || 'Please check range']"
                  />
                </q-item-section>
                <q-item-section v-if="newInputArgument.dataType === argumentDataTypeOptions[2]">
                  <q-input
                    v-model="newInputArgument.size"
                    dense
                    square
                    filled
                    placeholder="0 ~ 255"
                    class="col input-box"
                    :rules="[(val) => (0 <= val && val <= 255) || 'Please check range']"
                  />
                </q-item-section>
                <q-item-section v-if="newInputArgument.dataType === argumentDataTypeOptions[3]">
                  <q-input
                    v-model="newInputArgument.size"
                    dense
                    square
                    filled
                    placeholder="-30,000 ~ 30,000"
                    class="col input-box"
                    :rules="[(val) => (-30000 <= val && val <= 30000) || 'Please check range']"
                  />
                </q-item-section>
                <q-item-section v-if="newInputArgument.dataType === argumentDataTypeOptions[4]">
                  <q-input
                    v-model="newInputArgument.size"
                    dense
                    square
                    filled
                    placeholder="0 ~ 30,000"
                    class="col input-box"
                    :rules="[(val) => (0 <= val && val <= 30000) || 'Please check range']"
                  />
                </q-item-section>
                <q-item-section v-if="newInputArgument.dataType === argumentDataTypeOptions[5]">
                  <q-input
                    v-model="newInputArgument.size"
                    dense
                    square
                    filled
                    placeholder="-30,000 ~ 30,000"
                    class="col input-box"
                    :rules="[(val) => (-30000 <= val && val <= 30000) || 'Please check range']"
                  />
                </q-item-section>
                <q-item-section v-if="newInputArgument.dataType === argumentDataTypeOptions[6]">
                  <q-input
                    v-model="newInputArgument.size"
                    dense
                    square
                    filled
                    placeholder="0 ~ 30,000"
                    class="col input-box"
                    :rules="[(val) => (0 <= val && val <= 30000) || 'Please check range']"
                  />
                </q-item-section>
                <q-item-section v-if="newInputArgument.dataType === argumentDataTypeOptions[7]">
                  <q-input
                    v-model="newInputArgument.size"
                    dense
                    square
                    filled
                    placeholder="-30,000 ~ 30,000"
                    class="col input-box"
                    :rules="[(val) => (-30000 <= val && val <= 30000) || 'Please check range']"
                  />
                </q-item-section>
                <q-item-section v-if="newInputArgument.dataType === argumentDataTypeOptions[8]">
                  <q-input
                    v-model="newInputArgument.size"
                    dense
                    square
                    filled
                    placeholder="0 ~ 30,000"
                    class="col input-box"
                    :rules="[(val) => (0 <= val && val <= 30000) || 'Please check range']"
                  />
                </q-item-section>
                <q-item-section v-if="newInputArgument.dataType === argumentDataTypeOptions[9]">
                  <q-input v-model="newInputArgument.size" dense square filled placeholder="±1.4E-45 ~ ±3.4E+38" class="col input-box" :rules="[(val) => !!val || '* Required']" />
                </q-item-section>
                <q-item-section v-if="newInputArgument.dataType === argumentDataTypeOptions[10]">
                  <q-input v-model="newInputArgument.size" dense square filled placeholder="±5.0E-324 ~ ±1.7E+308" class="col input-box" />
                </q-item-section>
                <q-item-section v-if="newInputArgument.dataType === argumentDataTypeOptions[11]">
                  <q-input v-model="newInputArgument.size" dense square filled placeholder="0 ~ 100" class="col input-box" />
                </q-item-section>
                <q-item-section> <q-select v-model="newInputArgument.dataType" dense square filled :options="argumentDataTypeOptions" class="col input-box" /></q-item-section>
                <q-item-section side> <q-btn flat color="main" size="md" padding="2px 12px 0px" class="q-mx-sm" @click="addItemToInputArguments"> 추가 </q-btn></q-item-section>
              </q-item>
              <div v-for="(item, index) in newReadWriter.inputArguments" :key="index">
                <q-item>
                  <q-item-section>
                    {{ item.size }}
                  </q-item-section>
                  <q-item-section>
                    {{ item.dataType }}
                  </q-item-section>
                  <q-item-section side>
                    <q-btn flat color="negative" size="md" padding="2px 12px 0px" class="q-mx-sm" @click="removeItem(index)"> 삭제 </q-btn>
                  </q-item-section>
                </q-item>
              </div>
            </div>
          </div>
        </template>

        <!-- Method Call -->
        <template v-if="newReadWriter.type === typeOptions[2]">
          <div class="row height">
            <div class="col-6 flex items-center">Input Arguments</div>
            <div class="col-12 column">
              <q-item>
                <q-item-section v-if="newInputArgument.dataType === argumentDataTypeOptions[0]">
                  <q-input v-model="newInputArgument.size" dense square filled placeholder="true / false" class="col input-box" />
                </q-item-section>
                <q-item-section v-if="newInputArgument.dataType === argumentDataTypeOptions[1]">
                  <q-input
                    v-model="newInputArgument.size"
                    dense
                    square
                    filled
                    placeholder="-128 ~ 127"
                    class="col input-box"
                    :rules="[(val) => (-128 <= val && val <= 127) || 'Please check range']"
                  />
                </q-item-section>
                <q-item-section v-if="newInputArgument.dataType === argumentDataTypeOptions[2]">
                  <q-input
                    v-model="newInputArgument.size"
                    dense
                    square
                    filled
                    placeholder="0 ~ 255"
                    class="col input-box"
                    :rules="[(val) => (0 <= val && val <= 255) || 'Please check range']"
                  />
                </q-item-section>
                <q-item-section v-if="newInputArgument.dataType === argumentDataTypeOptions[3]">
                  <q-input
                    v-model="newInputArgument.size"
                    dense
                    square
                    filled
                    placeholder="-30,000 ~ 30,000"
                    class="col input-box"
                    :rules="[(val) => (-30000 <= val && val <= 30000) || 'Please check range']"
                  />
                </q-item-section>
                <q-item-section v-if="newInputArgument.dataType === argumentDataTypeOptions[4]">
                  <q-input
                    v-model="newInputArgument.size"
                    dense
                    square
                    filled
                    placeholder="0 ~ 30,000"
                    class="col input-box"
                    :rules="[(val) => (0 <= val && val <= 30000) || 'Please check range']"
                  />
                </q-item-section>
                <q-item-section v-if="newInputArgument.dataType === argumentDataTypeOptions[5]">
                  <q-input
                    v-model="newInputArgument.size"
                    dense
                    square
                    filled
                    placeholder="-30,000 ~ 30,000"
                    class="col input-box"
                    :rules="[(val) => (-30000 <= val && val <= 30000) || 'Please check range']"
                  />
                </q-item-section>
                <q-item-section v-if="newInputArgument.dataType === argumentDataTypeOptions[6]">
                  <q-input
                    v-model="newInputArgument.size"
                    dense
                    square
                    filled
                    placeholder="0 ~ 30,000"
                    class="col input-box"
                    :rules="[(val) => (0 <= val && val <= 30000) || 'Please check range']"
                  />
                </q-item-section>
                <q-item-section v-if="newInputArgument.dataType === argumentDataTypeOptions[7]">
                  <q-input
                    v-model="newInputArgument.size"
                    dense
                    square
                    filled
                    placeholder="-30,000 ~ 30,000"
                    class="col input-box"
                    :rules="[(val) => (-30000 <= val && val <= 30000) || 'Please check range']"
                  />
                </q-item-section>
                <q-item-section v-if="newInputArgument.dataType === argumentDataTypeOptions[8]">
                  <q-input
                    v-model="newInputArgument.size"
                    dense
                    square
                    filled
                    placeholder="0 ~ 30,000"
                    class="col input-box"
                    :rules="[(val) => (0 <= val && val <= 30000) || 'Please check range']"
                  />
                </q-item-section>
                <q-item-section v-if="newInputArgument.dataType === argumentDataTypeOptions[9]">
                  <q-input v-model="newInputArgument.size" dense square filled placeholder="±1.4E-45 ~ ±3.4E+38" class="col input-box" />
                </q-item-section>
                <q-item-section v-if="newInputArgument.dataType === argumentDataTypeOptions[10]">
                  <q-input v-model="newInputArgument.size" dense square filled placeholder="±5.0E-324 ~ ±1.7E+308" class="col input-box" />
                </q-item-section>
                <q-item-section v-if="newInputArgument.dataType === argumentDataTypeOptions[11]">
                  <q-input v-model="newInputArgument.size" dense square filled placeholder="0 ~ 100" class="col input-box" />
                </q-item-section>
                <q-item-section> <q-select v-model="newInputArgument.dataType" dense square filled :options="argumentDataTypeOptions" class="col input-box" /></q-item-section>
                <q-item-section side> <q-btn flat color="main" size="md" padding="2px 12px 0px" class="q-mx-sm" @click="addItemToInputArguments"> 추가 </q-btn></q-item-section>
              </q-item>
              <div v-for="(item, index) in newReadWriter.inputArguments" :key="index">
                <q-item>
                  <q-item-section>
                    {{ item.size }}
                  </q-item-section>
                  <q-item-section>
                    {{ item.dataType }}
                  </q-item-section>
                  <q-item-section side>
                    <q-btn flat color="negative" size="md" padding="2px 12px 0px" class="q-mx-sm" @click="removeItem(index)"> 삭제 </q-btn>
                  </q-item-section>
                </q-item>
              </div>
            </div>
          </div>
        </template>

        <!-- RawBuffer Send -->
        <template v-if="newReadWriter.type === typeOptions[3]">
          <div class="row height">
            <div class="col-6 flex items-center">RawBuffer</div>
            <q-input outlined v-model="newReadWriter.rawBuffer" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
          </div>
        </template>

        <!-- Browse -->
        <template v-if="newReadWriter.type === typeOptions[4]">
          <div class="row height">
            <div class="col-6 flex items-center">Node ID</div>
            <q-input outlined v-model="newReadWriter.nodeId" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
          </div>
        </template>

        <!-- Historical Read -->
        <template v-if="newReadWriter.type === typeOptions[5]">
          <div class="row height">
            <div class="col-6 flex items-center">Node ID</div>
            <q-input outlined v-model="newReadWriter.nodeId" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
          </div>
        </template>

        <div class="row justify-evenly items-center">
          <q-btn label="추가" type="submit" color="main" padding="xs lg"></q-btn>
          <q-btn label="취소" flat padding="xs lg" color="red" v-close-popup></q-btn>
        </div>
      </q-form>
    </q-card>
  </q-dialog>
</template>
