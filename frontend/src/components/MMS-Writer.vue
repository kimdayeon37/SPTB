<script setup lang="ts">
import axios from 'axios'
import type { QTableProps } from 'quasar'
import { ref, type PropType } from 'vue'

type NetworkData = {
  protocol?: string
  ip?: string
  port?: number
  transactionDelay?: number
  timeout?: number
}

const props = defineProps({
  networkData: {
    type: Object as PropType<NetworkData>,
    required: true,
  },
  viewLog: { type: Function, required: true },
})
const startSimulator = async () => {
  if (selectedWriters.value.length === 0) {
    alert('실행할 writer들을 선택해주세요.')
    return
  }
  if (!(props.networkData.ip && props.networkData.port && props.networkData.timeout && props.networkData.transactionDelay)) {
    alert('통신 설정 값을 기입해주세요.')
    return
  }
  await axios
    .post('http://localhost:4000/writer', {
      networkData: {
        ...props.networkData,
        msgCount: selectedWriters.value.length,
      },
      msgData: selectedWriters.value,
    })
    .then(() => {
      props.viewLog(true)
    })
    .catch((err) => {
      console.log(err)
    })
}
// writer
type WriterType = {
  time: number
  name?: string
  slaveId?: number
  type?: string
  writeAddress?: number
  readAddress?: number
  value?: boolean | number
  values?: (boolean | number)[]
  invalidFunction?: boolean
  invalidLength?: boolean
  invalidChecksum?: boolean
  byteSwap?: boolean
  wordSwap?: boolean
  andMask?: boolean[]
  orMask?: boolean[]
  hexValue?: string
}

const inputedWriterName = ref<string>()
const inputedWriterType = ref<string>('Write Single Coil')
const typeOptions = [
  'Write Single Coil',
  'Write Single Register',
  'Write Multiple Coils',
  'Write Multiple Registers',
  'Write Mask Registers',
  'Read/Write Multiple Registers',
  'Send Custom Hex String',
]
const inputedWriterSlaveId = ref<number>()
const inputedWriterWriteAddress = ref<number>()
const inputedWriterReadAddress = ref<number>()
const inputedWriterBooleanValue = ref<boolean>()
const inputedWriterNumberValue = ref<number>()
const inputedWriterValues = ref<(boolean | number)[]>([]) // Write Multiple Coils, Write Multiple Registers
const inputedWriterByteSwap = ref<boolean>() // Write Single Register
const inputedWriterWordSwap = ref<boolean>() // Write Single Register
const inputedWriterInvalidFucntion = ref<boolean>(false)
const inputedWriterInvalidLength = ref<boolean>(false)
const inputedWriterInvalidChecksum = ref<boolean>(false)
const inputedWriterANDMask = ref<boolean[]>()
const inputedWriterORMask = ref<boolean[]>()
const inputedWriterHexValue = ref<string>()

const inputValueForAdd = ref<number>()
const addWriterToggle = ref<boolean>(false)
const addWriterToggleHandler = () => {
  addWriterToggle.value = !addWriterToggle.value
}

const writers = ref<any[]>([])
const selectedWriters = ref<WriterType[]>([])
const columns: QTableProps['columns'] = [
  {
    name: 'time',
    label: 'Time',
    field: '',
    sortable: true,
    align: 'left',
    headerStyle: 'width: 10px',
  },
  {
    name: 'name',
    required: true,
    label: 'Name',
    align: 'left',
    field: 'name',
  },
]
const addWriter = () => {
  console.log('addWriter')
  writers.value.push({
    time: Date.now(),
    name: inputedWriterName.value,
    slaveId: inputedWriterSlaveId.value,
    type: inputedWriterType.value,
    writeAddress: inputedWriterWriteAddress.value,
    readAddress: inputedWriterReadAddress.value,
    value: inputedWriterType.value === typeOptions[0] ? inputedWriterBooleanValue.value : inputedWriterNumberValue.value,
    values: inputedWriterValues.value,
    invalidFunction: inputedWriterInvalidFucntion.value,
    invalidLength: inputedWriterInvalidLength.value,
    invalidChecksum: inputedWriterInvalidChecksum.value,
    byteSwap: inputedWriterByteSwap.value,
    wordSwap: inputedWriterWordSwap.value,
    andMask: inputedWriterANDMask.value,
    orMask: inputedWriterORMask.value,
    hexValue: inputedWriterHexValue.value,
  })
  addWriterToggleHandler()
}

const deleteWriters = () => {
  console.log(selectedWriters.value)
  console.log(writers.value)

  const newWriters = writers.value.filter(
    (item) =>
      !selectedWriters.value.some((target) => {
        console.log(target.time + ' : ' + item.time)

        return target.time == item.time
      })
  )
  writers.value = newWriters
  console.log(writers.value)
}

// 아이템 추가
const addItem = () => {
  const value = inputValueForAdd.value
  if (value) {
    // multiple coil
    if (inputedWriterType.value === typeOptions[2]) {
      inputedWriterValues.value.push(value > 0 ? true : false)
    }
    // multiple register
    if (inputedWriterType.value === typeOptions[3]) {
      if (value < 0 || value > 65535) {
        alert('0 ~ 65535 범위입니다.')
        return
      }
      inputedWriterValues.value.push(value)
    }
  }
}

// 아이템 삭제
const removeItem = (index: number) => {
  inputedWriterValues.value.splice(index, 1)
}
</script>
<template>
  <div class="col-12 col-md-5 column border-left">
    <div class="title q-pl-md flex items-center">
      <strong class="text-subtitle1">Write</strong>
    </div>
    <div class="menu-bar-dense row items-center justify-end">
      <q-btn rounded unelevated color="positive" size="md" padding="0.1px 12px" class="q-mx-sm" @click="startSimulator"> 실행 </q-btn>
      <q-btn flat color="main" size="md" padding="2px 12px" class="q-mx-sm" @click="addWriterToggleHandler"> 추가 </q-btn>
      <q-separator vertical />
      <q-btn flat color="negative" size="md" padding="2px 12px" class="q-mx-sm" @click="deleteWriters"> 삭제 </q-btn>
    </div>
    <div class="col table-container">
      <q-table
        flat
        square
        :rows="writers"
        :columns="columns"
        row-key="time"
        dense
        class="table"
        :rows-per-page-options="[0]"
        hide-no-data
        hide-pagination
        selection="multiple"
        v-model:selected="selectedWriters"
      />
    </div>
    <q-dialog v-model="addWriterToggle">
      <q-card class="q-pa-md dialog-box">
        <q-form @submit="addWriter">
          <div class="row height">
            <div class="col-6 flex items-center">Name</div>
            <q-input ref="inputRef" outlined v-model="inputedWriterName" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
          </div>
          <div class="row height">
            <div class="col-6 flex items-center">Type</div>
            <q-select outlined v-model="inputedWriterType" dense class="col-6" :options="typeOptions" />
          </div>
          <!-- Write Singe Coil -->
          <template v-if="inputedWriterType === typeOptions[0]">
            <div class="row height">
              <div class="col-6 flex items-center">Slave ID</div>
              <q-input outlined v-model="inputedWriterSlaveId" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
            </div>
            <div class="row height">
              <div class="col-6 flex items-center">Address</div>
              <q-input outlined v-model="inputedWriterWriteAddress" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
            </div>
            <div class="row height">
              <div class="col-6 flex items-center">Value (Boolean)</div>
              <q-toggle outlined v-model="inputedWriterBooleanValue" dense class="col-6" :rules="[(val: any) => !!val || '* Required']" />
            </div>
            <div class="row height">
              <div class="col-6 flex items-center">Invalid Function</div>
              <q-toggle outlined v-model="inputedWriterInvalidFucntion" dense class="col-6" :rules="[(val: any) => !!val || '* Required']" />
            </div>
          </template>

          <!-- Write Singe Register -->
          <template v-if="inputedWriterType === typeOptions[1]">
            <div class="row height">
              <div class="col-6 flex items-center">Slave ID</div>
              <q-input outlined v-model="inputedWriterSlaveId" dense class="col-6" />
            </div>
            <div class="row height">
              <div class="col-6 flex items-center">Address</div>
              <q-input outlined v-model="inputedWriterWriteAddress" dense class="col-6" />
            </div>
            <div class="row height">
              <div class="col-6 flex items-center">Value (UInt16)</div>
              <q-input outlined v-model="inputedWriterNumberValue" dense class="col-6" />
            </div>
            <div class="row height">
              <div class="col-6 flex items-center">Byte Swap</div>
              <q-toggle outlined v-model="inputedWriterByteSwap" dense class="col-6" />
            </div>
            <div class="row height">
              <div class="col-6 flex items-center">Invalid Function</div>
              <q-toggle outlined v-model="inputedWriterInvalidFucntion" dense class="col-6" />
            </div>
          </template>

          <!-- Write Multiple Coils -->
          <template v-if="inputedWriterType === typeOptions[2]">
            <div class="row height">
              <div class="col-6 flex items-center">Slave ID</div>
              <q-input outlined v-model="inputedWriterSlaveId" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
            </div>
            <div class="row height">
              <div class="col-6 flex items-center">Address</div>
              <q-input outlined v-model="inputedWriterWriteAddress" dense class="col-6" />
            </div>
            <div class="row height">
              <div class="col-6 flex items-center">Values</div>
              <div class="col-6 column">
                <div class="menu-bar-dense row justify-end input-box">
                  <q-input v-model="inputValueForAdd" dense square filled placeholder="0 or 1" mask="#" class="col input-box" style="height: 100%" />
                  <q-btn flat color="main" size="md" padding="2px 12px 0px" class="q-mx-sm" @click="addItem"> 추가 </q-btn>
                </div>
                <div v-for="(item, index) in inputedWriterValues" :key="index">
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
              <q-toggle outlined v-model="inputedWriterInvalidFucntion" dense class="col-6" :rules="[(val: any) => !!val || '* Required']" />
            </div>
            <div class="row height">
              <div class="col-6 flex items-center">Invalid Length</div>
              <q-toggle outlined v-model="inputedWriterInvalidLength" dense class="col-6" :rules="[(val: any) => !!val || '* Required']" />
            </div>
          </template>

          <!-- Write Multiple Registers -->
          <template v-if="inputedWriterType === typeOptions[3]">
            <div class="row height">
              <div class="col-6 flex items-center">Slave ID</div>
              <q-input outlined v-model="inputedWriterSlaveId" dense class="col-6" :rules="[(val) => !!val || '* Required']" />
            </div>
            <div class="row height">
              <div class="col-6 flex items-center">Address</div>
              <q-input outlined v-model="inputedWriterWriteAddress" dense class="col-6" />
            </div>
            <div class="row height">
              <div class="col-6 flex items-center">Values</div>
              <div class="col-6 column">
                <div class="menu-bar-dense row justify-end input-box">
                  <q-input v-model="inputValueForAdd" dense square filled placeholder="0 ~ 65535" class="col input-box" style="height: 100%" />
                  <q-btn flat color="main" size="md" padding="2px 12px 0px" class="q-mx-sm" @click="addItem"> 추가 </q-btn>
                </div>
                <div v-for="(item, index) in inputedWriterValues" :key="index">
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
              <q-toggle outlined v-model="inputedWriterByteSwap" dense class="col-6" />
            </div>
            <div class="row height">
              <div class="col-6 flex items-center">Word Swap</div>
              <q-toggle outlined v-model="inputedWriterWordSwap" dense class="col-6" />
            </div>
            <div class="row height">
              <div class="col-6 flex items-center">Invalid Function</div>
              <q-toggle outlined v-model="inputedWriterInvalidFucntion" dense class="col-6" :rules="[(val: any) => !!val || '* Required']" />
            </div>
            <div class="row height">
              <div class="col-6 flex items-center">Invalid Length</div>
              <q-toggle outlined v-model="inputedWriterInvalidLength" dense class="col-6" :rules="[(val: any) => !!val || '* Required']" />
            </div>
          </template>

          <!-- Write Mask Register -->
          <template v-if="inputedWriterType === typeOptions[4]"> </template>

          <!-- Write Read/Write Multiple Registers -->
          <template v-if="inputedWriterType === typeOptions[5]"> </template>

          <!-- Send Custom Hex String -->
          <template v-if="inputedWriterType === typeOptions[6]"> </template>
          <div class="row height">
            <div class="col-6 flex items-center">Invalid Checksum</div>
            <q-toggle outlined v-model="inputedWriterInvalidChecksum" dense class="col-6" :rules="[(val: any) => !!val || '* Required']" />
          </div>
          <div class="row justify-evenly items-center">
            <q-btn label="추가" type="submit" color="main" padding="xs lg"></q-btn>
            <q-btn label="취소" flat padding="xs lg" color="red" v-close-popup></q-btn>
          </div>
        </q-form>
      </q-card>
    </q-dialog>
  </div>
</template>
<style lang="scss">
.border-left {
  border-left: solid 1px;
  border-color: #bcbcbc;
}
.input-box {
  .q-field--dense .q-field__control {
    height: 100% !important;
  }
  .q-field__bottom {
    height: 0px;
    overflow: hidden;
    margin: 0px;
    padding: 0px;
    display: none !important;
  }
}
</style>
