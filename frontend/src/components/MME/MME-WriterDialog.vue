<script setup lang="ts">
import { ref, watch } from 'vue'
import type { WriterData } from '../../types'
const emits = defineEmits<{
  addWriter: [reader: WriterData]
  addWriterToggleHandler: [bool?: boolean]
}>()

const inputedWriterBooleanValue = ref<boolean>()
const inputedWriterNumberValue = ref<number>()

const newWriter = ref<WriterData>({
  type: 'Write Single Coil',
  values: [],
  invalidFunction: false,
  invalidLength: false,
  byteSwap: false,
  wordSwap: false,
})
const typeOptions = [
  'Write Single Coil',
  'Write Single Register',
  'Write Multiple Coils',
  'Write Multiple Registers',
  'Write Mask Registers',
  'Read/Write Multiple Registers',
  'Send Custom Hex String',
]
const inputValueForAdd = ref<number>()
// 아이템 추가
const addItem = () => {
  const value = inputValueForAdd.value
  if (value && newWriter.value) {
    // multiple coil
    if (newWriter.value.type === typeOptions[2]) {
      newWriter.value.values.push(!!value)
    }
    // multiple register
    if (newWriter.value.type === typeOptions[3] || newWriter.value.type === typeOptions[5]) {
      if (value < 0 || value > 65535) {
        alert('0 ~ 65535 범위입니다.')
        return
      }
      newWriter.value.values.push(value)
    }
  }
}

// 아이템 삭제
const removeItem = (index: number) => {
  newWriter.value.values.splice(index, 1)
}
watch(
  () => newWriter.value.type,
  () => {
    newWriter.value.values.length = 0
  }
)
</script>
<template>
  <q-dialog>
    <q-card class="q-pa-md dialog-box">
      <q-form
        @submit="
          () => {
            if (newWriter.type === typeOptions[0] || newWriter.type === typeOptions[1]) {
              if (newWriter.type === typeOptions[0]) newWriter.values.push(inputedWriterBooleanValue!)
              else newWriter.values.push(inputedWriterNumberValue!)
            }
            emits('addWriter', newWriter)
            console.log(newWriter)

            emits('addWriterToggleHandler', false)
          }
        "
      >
        <div class="row height">
          <div class="col-6 flex items-center">Name</div>
          <q-input ref="inputRef" outlined v-model="newWriter.name" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
        </div>
        <div class="row height">
          <div class="col-6 flex items-center">Type</div>
          <q-select outlined v-model="newWriter.type" dense class="col-6" :options="typeOptions" />
        </div>
        <!-- Write Singe Coil -->
        <template v-if="newWriter.type === typeOptions[0]">
          <div class="row height">
            <div class="col-6 flex items-center">Slave ID</div>
            <q-input outlined v-model="newWriter.slaveId" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
          </div>
          <div class="row height">
            <div class="col-6 flex items-center">Address</div>
            <q-input outlined v-model="newWriter.writeAddress" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
          </div>
          <div class="row height">
            <div class="col-6 flex items-center">Value (Boolean)</div>
            <q-toggle outlined v-model="inputedWriterBooleanValue" dense class="col-6" :rules="[(val: any) => !!val || '* Required']" />
          </div>
          <div class="row height">
            <div class="col-6 flex items-center">Invalid Function</div>
            <q-toggle outlined v-model="newWriter.invalidFunction" dense class="col-6" :rules="[(val: any) => !!val || '* Required']" />
          </div>
        </template>

        <!-- Write Singe Register -->
        <template v-if="newWriter.type === typeOptions[1]">
          <div class="row height">
            <div class="col-6 flex items-center">Slave ID</div>
            <q-input outlined v-model="newWriter.slaveId" dense class="col-6" />
          </div>
          <div class="row height">
            <div class="col-6 flex items-center">Address</div>
            <q-input outlined v-model="newWriter.writeAddress" dense class="col-6" />
          </div>
          <div class="row height">
            <div class="col-6 flex items-center">Value (UInt16)</div>
            <q-input outlined v-model="inputedWriterNumberValue" dense class="col-6" />
          </div>
          <div class="row height">
            <div class="col-6 flex items-center">Byte Swap</div>
            <q-toggle outlined v-model="newWriter.byteSwap" dense class="col-6" />
          </div>
          <div class="row height">
            <div class="col-6 flex items-center">Invalid Function</div>
            <q-toggle outlined v-model="newWriter.invalidFunction" dense class="col-6" />
          </div>
        </template>

        <!-- Write Multiple Coils -->
        <template v-if="newWriter.type === typeOptions[2]">
          <div class="row height">
            <div class="col-6 flex items-center">Slave ID</div>
            <q-input outlined v-model="newWriter.slaveId" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
          </div>
          <div class="row height">
            <div class="col-6 flex items-center">Address</div>
            <q-input outlined v-model="newWriter.writeAddress" dense class="col-6" />
          </div>
          <div class="row height">
            <div class="col-6 flex items-center">Values</div>
            <div class="col-6 column">
              <div class="menu-bar-dense row justify-end input-box">
                <q-input v-model="inputValueForAdd" dense square filled placeholder="0 or 1" mask="#" class="col input-box" style="height: 100%" />
                <q-btn flat color="main" size="md" padding="2px 12px 0px" class="q-mx-sm" @click="addItem"> 추가 </q-btn>
              </div>
              <div v-for="(item, index) in newWriter.values" :key="index">
                <q-item>
                  <q-item-section>
                    {{ index }}
                  </q-item-section>
                  <q-item-section>
                    {{ item }}
                  </q-item-section>
                  <q-item-section side>
                    <q-btn flat color="negative" size="md" padding="2px 12px 0px" class="q-mx-sm" @click="removeItem(index)"> 삭제 </q-btn>
                  </q-item-section>
                </q-item>
              </div>
            </div>
          </div>
          <div class="row height">
            <div class="col-6 flex items-center">Invalid Function</div>
            <q-toggle outlined v-model="newWriter.invalidFunction" dense class="col-6" :rules="[(val: any) => !!val || '* Required']" />
          </div>
          <div class="row height">
            <div class="col-6 flex items-center">Invalid Length</div>
            <q-toggle outlined v-model="newWriter.invalidLength" dense class="col-6" :rules="[(val: any) => !!val || '* Required']" />
          </div>
        </template>

        <!-- Write Multiple Registers -->
        <template v-if="newWriter.type === typeOptions[3]">
          <div class="row height">
            <div class="col-6 flex items-center">Slave ID</div>
            <q-input outlined v-model="newWriter.slaveId" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
          </div>
          <div class="row height">
            <div class="col-6 flex items-center">Address</div>
            <q-input outlined v-model="newWriter.writeAddress" dense class="col-6" />
          </div>
          <div class="row height">
            <div class="col-6 flex items-center">Values</div>
            <div class="col-6 column">
              <div class="menu-bar-dense row justify-end input-box">
                <q-input v-model="inputValueForAdd" dense square filled placeholder="0 ~ 65535" class="col input-box" style="height: 100%" />
                <q-btn flat color="main" size="md" padding="2px 12px 0px" class="q-mx-sm" @click="addItem"> 추가 </q-btn>
              </div>
              <div v-for="(item, index) in newWriter.values" :key="index">
                <q-item>
                  <q-item-section>
                    {{ index }}
                  </q-item-section>
                  <q-item-section>
                    {{ item }}
                  </q-item-section>
                  <q-item-section side>
                    <q-btn flat color="negative" size="md" padding="2px 12px 0px" class="q-mx-sm" @click="removeItem(index)"> 삭제 </q-btn>
                  </q-item-section>
                </q-item>
              </div>
            </div>
          </div>
          <div class="row height">
            <div class="col-6 flex items-center">Byte Swap</div>
            <q-toggle outlined v-model="newWriter.byteSwap" dense class="col-6" />
          </div>
          <div class="row height">
            <div class="col-6 flex items-center">Word Swap</div>
            <q-toggle outlined v-model="newWriter.wordSwap" dense class="col-6" />
          </div>
          <div class="row height">
            <div class="col-6 flex items-center">Invalid Function</div>
            <q-toggle outlined v-model="newWriter.invalidFunction" dense class="col-6" :rules="[(val: any) => !!val || '* Required']" />
          </div>
          <div class="row height">
            <div class="col-6 flex items-center">Invalid Length</div>
            <q-toggle outlined v-model="newWriter.invalidLength" dense class="col-6" :rules="[(val: any) => !!val || '* Required']" />
          </div>
        </template>

        <!-- Write Mask Register -->
        <template v-if="newWriter.type === typeOptions[4]">
          <div class="row height">
            <div class="col-6 flex items-center">Slave ID</div>
            <q-input outlined v-model="newWriter.slaveId" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
          </div>
          <div class="row height">
            <div class="col-6 flex items-center">Address</div>
            <q-input outlined v-model="newWriter.writeAddress" dense class="col-6" />
          </div>
          <div class="row height">
            <div class="col-6 height flex items-center">AND Mask</div>
            <q-input
              outlined
              v-model="newWriter.andMask"
              label="0 or 1 16 bit"
              dense
              mask="#### #### #### ####"
              unmasked-value
              class="col-6"
              :rules="[
                (val) => !!val || '* Required',
                (val) => {
                  const zeroOrOne = /^[01]{16}$/
                  return zeroOrOne.test(val) // 정규 표현식과 매치되는지 확인
                },
              ]"
            />
          </div>
          <div class="row height">
            <div class="col-6 height flex items-center">OR Mask</div>
            <q-input
              outlined
              v-model="newWriter.orMask"
              label="0 or 1 16 bit"
              dense
              mask="#### #### #### ####"
              unmasked-value
              class="col-6"
              :rules="[
                (val) => !!val || '* Required',
                (val) => {
                  const zeroOrOne = /^[01]{16}$/
                  return zeroOrOne.test(val) // 정규 표현식과 매치되는지 확인
                },
              ]"
            />
          </div>
          <div class="row height">
            <div class="col-6 flex items-center">Invalid Function</div>
            <q-toggle outlined v-model="newWriter.invalidFunction" dense class="col-6" :rules="[(val: any) => !!val || '* Required']" />
          </div>
        </template>

        <!-- Write Read/Write Multiple Registers -->
        <template v-if="newWriter.type === typeOptions[5]">
          <div class="row height">
            <div class="col-6 flex items-center">Slave ID</div>
            <q-input outlined v-model="newWriter.slaveId" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
          </div>
          <div class="row height">
            <div class="col-6 flex items-center">Read Address</div>
            <q-input outlined v-model="newWriter.readAddress" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
          </div>
          <div class="row height">
            <div class="col-6 flex items-center">Read Quantity</div>
            <q-input outlined v-model="newWriter.readQuantity" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
          </div>
          <div class="row height">
            <div class="col-6 flex items-center">Write Address</div>
            <q-input outlined v-model="newWriter.writeAddress" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
          </div>
          <div class="row height">
            <div class="col-6 flex items-center">Values</div>
            <div class="col-6 column">
              <div class="menu-bar-dense row justify-end input-box">
                <q-input v-model="inputValueForAdd" dense square filled placeholder="0 ~ 65535" class="col input-box" style="height: 100%" />
                <q-btn flat color="main" size="md" padding="2px 12px 0px" class="q-mx-sm" @click="addItem"> 추가 </q-btn>
              </div>
              <div v-for="(item, index) in newWriter.values" :key="index">
                <q-item>
                  <q-item-section>
                    {{ index }}
                  </q-item-section>
                  <q-item-section>
                    {{ item }}
                  </q-item-section>
                  <q-item-section side>
                    <q-btn flat color="negative" size="md" padding="2px 12px 0px" class="q-mx-sm" @click="removeItem(index)"> 삭제 </q-btn>
                  </q-item-section>
                </q-item>
              </div>
            </div>
          </div>
          <div class="row height">
            <div class="col-6 flex items-center">Invalid Function</div>
            <q-toggle outlined v-model="newWriter.invalidFunction" dense class="col-6" :rules="[(val: any) => !!val || '* Required']" />
          </div>
          <div class="row height">
            <div class="col-6 flex items-center">Invalid Length</div>
            <q-toggle outlined v-model="newWriter.invalidLength" dense class="col-6" :rules="[(val: any) => !!val || '* Required']" />
          </div>
        </template>

        <!-- Send Custom Hex String -->
        <template v-if="newWriter.type === typeOptions[6]">
          <div class="row height">
            <div class="col-6 flex items-center">Hex Value</div>
            <q-input
              outlined
              v-model="newWriter.hexValue"
              dense
              class="col-6"
              :rules="[
                (val) => !!val || '* Required',
                (val) => {
                  const hexPattern = /^[0-9A-Fa-f]+$/
                  return hexPattern.test(val) // 정규 표현식과 매치되는지 확인
                },
              ]"
            />
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
