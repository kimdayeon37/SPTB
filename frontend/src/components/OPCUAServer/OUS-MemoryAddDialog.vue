<script setup lang="ts">
import { ref } from 'vue'
import type { ArgumentData, OUSMemoryNodeData } from '../../types'

const newNode = ref<OUSMemoryNodeData>({
  label: '',
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
const newInputArgument = ref<ArgumentData>({
  name: '',
  dataType: 'UA_NULL',
})

const newOutputArgument = ref<ArgumentData>({
  name: '',
  dataType: 'UA_NULL',
})

const addArgument = (argArray: ArgumentData[] | undefined, newArg: ArgumentData) => {
  if (argArray)
    if (newArg.name !== '' && newArg.dataType !== '') {
      argArray.push({ ...newArg })
    }
  console.log(newNode.value.inputArguments)
}

const removeArgument = (argArray: ArgumentData[] | undefined, index: number) => {
  argArray?.splice(index, 1)
}

const addChildItem = (data: OUSMemoryNodeData[] | undefined, parentId: string, newItem: OUSMemoryNodeData) => {
  if (data)
    data.forEach((item) => {
      if (item.id === parentId) {
        if (!item.children) {
          item.children = []
        }
        if (
          item.children.findIndex((e) => {
            return e.label == newItem.label
          }) == -1
        ) {
          item.children.push(newItem)
        } else {
          alert('같은 경로에 동일한 이름의 노드가 존재합니다.')
        }
      } else if (item.children) {
        addChildItem(item.children, parentId, newItem)
      }
    })
}
const resetNewNode = () => {
  newNode.value = {
    ...{
      label: '',
      category: 'Variable',
      type: 'NULL',
      accessRight: 'Read & Write',
      inputArguments: [],
      outputArguments: [],
    },
  }
  newInputArgument.value = {
    name: '',
    dataType: 'UA_NULL',
  }
  newOutputArgument.value = {
    name: '',
    dataType: 'UA_NULL',
  }
}
</script>
<template>
  <q-dialog>
    <q-card class="q-pa-md dialog-box">
      <q-form
        @submit="
          () => {
            const newTreeData = [...props.treeData]
            addChildItem(newTreeData, props.selectedIndex, {
              ...newNode,
              inputArguments: newNode.inputArguments ? [...newNode.inputArguments] : [],
              outputArguments: newNode.outputArguments ? [...newNode.outputArguments] : [],
              id: Date.now().toString(),
            })
            emits('update:treeData', newTreeData)
            emits('update:modelValue', false)
            resetNewNode()
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
