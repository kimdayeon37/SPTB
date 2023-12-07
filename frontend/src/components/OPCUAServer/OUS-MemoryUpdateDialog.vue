<script setup lang="ts">
import { ref, watch } from 'vue'
import type { OUSMemoryNodeData, ArgumentData } from '../../types'

const newNode = ref<OUSMemoryNodeData>({
  category: 'Variable',
  type: 'NULL',
  accessRight: 'Read & Write',
  inputArguments: [],
  outputArguments: [],
})
const categoryOptions = ['Variable', 'Folder', 'Method']
const typeOptions = [
  'NULL',
  'Boolean',
  'SByte',
  'Byte',
  'Int16',
  'UInt16',
  'Int32',
  'UInt32',
  'StatusCode',
  'Int64',
  'UInt64',
  'DateTime',
  'Float',
  'Double',
  'String',
  'ByteString',
  'XmlElement',
]
const accessRightsOptions = ['Read & Write', 'ReadOnly']
const props = defineProps(['selectedIndex', 'treeData'])
const emits = defineEmits(['update:treeData', 'update:modelValue'])
// const emits = defineEmits<{
//     "update:treeData":[data:OUSMemoryNodeData[]];
//     "update:index":[index:number];
// }>()
const argumentDataTypeOptions = [
  'UA_NULL',
  'UA_Boolean',
  'UA_SByte',
  'UA_Byte',
  'UA_Int16',
  'UA_UInt16',
  'UA_Int32',
  'UA_UInt32',
  'UA_Int64',
  'UA_UInt64',
  'UA_StatusCode',
  'UA_DateTime',
  'UA_Float',
  'UA_Double',
  'UA_String',
  'UA_ByteString',
  'UA_XmlElement',
]
const newInputArgument = ref<{ name: string; dataType: string }>({
  name: '',
  dataType: 'UA_NULL',
})

const newOutputArgument = ref<{ name: string; dataType: string }>({
  name: '',
  dataType: 'UA_NULL',
})

const addArgument = (argArray: ArgumentData[] | undefined, newArg: ArgumentData) => {
  if (argArray)
    if (newArg.name !== '' && newArg.dataType !== '') {
      argArray.push({ ...newArg })
    }
}

const removeArgument = (argArray: ArgumentData[] | undefined, index: number) => {
  argArray?.splice(index, 1)
}

const updateItem = (data: OUSMemoryNodeData[] | undefined, idToUpdate: string, updatedData: OUSMemoryNodeData) => {
  if (data)
    data.forEach((item) => {
      if (item.id === idToUpdate) {
        // 찾은 항목을 업데이트
        item.label = updatedData.label
        item.category = updatedData.category
        item.type = updatedData.type
        item.accessRight = updatedData.accessRight
        if (updatedData.inputArguments) item.inputArguments = [...updatedData.inputArguments]
        if (updatedData.outputArguments) item.outputArguments = [...updatedData.outputArguments]
      } else if (item.children) {
        updateItem(item.children, idToUpdate, updatedData)
      }
    })
}

const findItem = (data: OUSMemoryNodeData[] | undefined, idToFind: string): OUSMemoryNodeData | null => {
  if (data)
    for (const item of data) {
      if (item.id === idToFind) {
        return item // 해당 ID를 가진 항목 반환
      } else if (item.children) {
        const found = findItem(item.children, idToFind)
        if (found) {
          return found // 하위 항목에서 발견된 경우 반환
        } else {
          console.log('findItem fail : No Matched ID')
          return null
        }
      }
    }
  else {
    console.log('findItem fail : No Memory Tree Data')
  }
  return null // 해당 ID를 가진 항목을 찾지 못한 경우 null 반환
}
watch(
  () => props.selectedIndex,
  () => {
    const exNode = findItem(props.treeData, props.selectedIndex)
    if (exNode) {
      newNode.value.accessRight = exNode?.accessRight
      newNode.value.category = exNode?.category
      newNode.value.label = exNode?.label
      newNode.value.type = exNode?.type
      console.log(exNode.inputArguments)
      console.log(exNode.outputArguments)
      if (exNode.inputArguments) newNode.value.inputArguments = [...exNode.inputArguments]
      if (exNode.outputArguments) newNode.value.outputArguments = [...exNode.outputArguments]
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
            const newTreeData = [...props.treeData]
            updateItem(newTreeData, props.selectedIndex, {
              ...newNode,
            })
            emits('update:treeData', newTreeData)
            emits('update:modelValue', false)
          }
        "
      >
        <div class="row height justify-center text-h6 text-weight-bold">노드 편집</div>

        <div class="row height">
          <div class="col-6 flex items-center">Name</div>
          <q-input ref="inputRef" outlined v-model="newNode.label" dense placeholder="Name" class="col-6" :rules="[(val) => !!val || '* Required']" />
        </div>

        <div class="row height">
          <div class="col-6 flex items-center">Category</div>
          <q-select outlined v-model="newNode.category" dense class="col-6" :options="categoryOptions" />
        </div>

        <div class="row height">
          <div class="col-6 flex items-center">Data Type</div>
          <q-select outlined v-model="newNode.type" dense class="col-6" :options="typeOptions" />
        </div>

        <div class="row height">
          <div class="col-6 flex items-center">Access Right</div>
          <q-select outlined v-model="newNode.accessRight" dense class="col-6" :options="accessRightsOptions" />
        </div>

        <div class="col-12 flex items-center justify-center">Input Arguments</div>
        <q-item>
          <q-item-section class="items-center"> Name </q-item-section>
          <q-item-section class="items-center"> Data Type </q-item-section>
          <q-item-section side> </q-item-section>
        </q-item>
        <div class="col-12 column">
          <q-item>
            <q-item-section>
              <q-input v-model="newInputArgument.name" dense square filled placeholder="Name" class="col input-box" />
            </q-item-section>
            <q-item-section> <q-select v-model="newInputArgument.dataType" dense square filled :options="argumentDataTypeOptions" class="col input-box" /></q-item-section>
            <q-item-section side>
              <q-btn flat color="main" size="md" padding="2px 12px 0px" class="q-mx-sm" @click="addArgument(newNode.inputArguments, newInputArgument)">
                추가
              </q-btn></q-item-section
            >
          </q-item>

          <div v-for="(item, index) in newNode.inputArguments" :key="index">
            <q-item>
              <q-item-section>
                {{ item.name }}
              </q-item-section>
              <q-item-section>
                {{ item.dataType }}
              </q-item-section>
              <q-item-section side>
                <q-btn flat color="negative" size="md" padding="2px 12px 0px" class="q-mx-sm" @click="removeArgument(newNode.inputArguments, index)"> 삭제 </q-btn>
              </q-item-section>
            </q-item>
          </div>
        </div>

        <div class="col-12 flex items-center justify-center q-mt-md">Output Arguments</div>
        <q-item>
          <q-item-section class="items-center"> Name </q-item-section>
          <q-item-section class="items-center"> Data Type </q-item-section>
          <q-item-section side> </q-item-section>
        </q-item>
        <div class="col-12 column">
          <q-item>
            <q-item-section>
              <q-input v-model="newOutputArgument.name" dense square filled placeholder="Name" class="col input-box" />
            </q-item-section>
            <q-item-section> <q-select v-model="newOutputArgument.dataType" dense square filled :options="argumentDataTypeOptions" class="col input-box" /></q-item-section>
            <q-item-section side>
              <q-btn flat color="main" size="md" padding="2px 12px 0px" class="q-mx-sm" @click="addArgument(newNode.outputArguments, newOutputArgument)">
                추가
              </q-btn></q-item-section
            >
          </q-item>

          <div v-for="(item, index) in newNode.outputArguments" :key="index">
            <q-item>
              <q-item-section>
                {{ item.name }}
              </q-item-section>
              <q-item-section>
                {{ item.dataType }}
              </q-item-section>
              <q-item-section side>
                <q-btn flat color="negative" size="md" padding="2px 12px 0px" class="q-mx-sm" @click="removeArgument(newNode.outputArguments, index)"> 삭제 </q-btn>
              </q-item-section>
            </q-item>
          </div>
        </div>
        <div class="row justify-evenly items-center">
          <q-btn label="적용" type="submit" color="main" padding="xs lg"></q-btn>
          <q-btn
            label="취소"
            flat
            padding="xs lg"
            color="red"
            @click="
              () => {
                emits('update:modelValue', false)
              }
            "
          ></q-btn>
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
