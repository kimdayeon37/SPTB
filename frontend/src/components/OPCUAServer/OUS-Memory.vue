<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import type { OUSMemoryNodeData } from '../../types'
import OUSMemoryAddDialog from './OUS-MemoryAddDialog.vue'
import OUSMemoryUpdateDialog from './OUS-MemoryUpdateDialog.vue'
import { $axios } from '@/axios/index'
import { useIdStore } from '../../store/idStore'
import { useOUSNetworkStore } from '../../store/OPCUAServer/OUS-NetworkStore'
import { useStateStore } from '../../store/stateStore'
import { useOUSMemoryTreeStore } from '../../store/OPCUAServer/OUS-TreeDataStore'

const stateStore = useStateStore()
const idStore = useIdStore()
const addDialogToggle = ref(false)
const updateDialogToggle = ref(false)
const selectedIndex = ref<string>()
const ousMemoryTreeDataStore = useOUSMemoryTreeStore()
const treeRef = ref()
const isExpanded = ref(false)
const ousMemoryTreeData = ref<OUSMemoryNodeData[]>([
  {
    id: '1',
    label: 'Object',
    children: [],
  },
])
const resetTreeData = () => {
  ousMemoryTreeData.value = [
    {
      id: '1',
      label: 'Object',
      children: [],
    },
  ]
}

const removeItem = (data: OUSMemoryNodeData[] | undefined, idToRemove: string) => {
  if (data)
    for (let i = data.length - 1; i >= 0; i--) {
      if (data[i].id === idToRemove) {
        data.splice(i, 1) // 해당 아이템 삭제
      } else if (data[i].children) {
        removeItem(data[i].children, idToRemove)
      }
    }
}
const tickedNodes = ref([])

const alertMsg = (msg: string) => {
  alert(msg)
}

const flattenAndProcessData = (nodes: OUSMemoryNodeData[], route: string, newArray: OUSMemoryNodeData[]) => {
  for (const node of nodes) {
    const { label, children } = node
    const nodeData = { ...node, route: route + label }
    newArray.push(nodeData)

    if (children) {
      flattenAndProcessData(children, route + label + '/', newArray)
    }
  }
}

const flattenData = (nodes: OUSMemoryNodeData[]): OUSMemoryNodeData[] => {
  const flattenedData: OUSMemoryNodeData[] = []
  flattenAndProcessData(nodes, '', flattenedData)

  //remove Object
  flattenedData.shift()

  //remove children
  flattenedData.forEach((node) => {
    delete node.children
  })
  return flattenedData
}

const startOpcuaServerSimulator = async () => {
  const idStore = useIdStore()
  const networkStore = useOUSNetworkStore()
  const array = flattenData(ousMemoryTreeData.value)

  if (!networkStore.checkData()) {
    alert('통신설정 해주세요.')
    return
  }
  console.log(JSON.stringify(array) + '\n을 전송 시도합니다.')

  try {
    await $axios()
      .post('/api/opcua/server', {
        id: idStore.clientId,
        ousNetworkData: {
          ...networkStore.networkData,
          nodeCount: array.length,
          port: networkStore.networkData?.port! * 1,
        },
        ousMemoryTreeData: [...array],
      })
      .then(() => {
        console.log('전송 성공')
        stateStore.state = true
      })
  } catch (e) {
    console.log('전송 실패 : ', e)
  }
}

const sendExitSignal = async () => {
  if (stateStore.state)
    $axios()
      .post('/api/exit', {
        id: idStore.clientId,
      })
      .then(() => {
        stateStore.state = false
      })
      .catch((err) => {
        console.log(err)
      })
}

watch(ousMemoryTreeData, () => {
  ousMemoryTreeDataStore.setMemoryTreeData(ousMemoryTreeData.value)
  console.log(ousMemoryTreeDataStore)
  if (selectedIndex.value != null) treeRef.value.setExpanded(selectedIndex.value, true)
})
onMounted(() => {
  const exTreeData = ousMemoryTreeDataStore.getMemoryTreeData()
  console.log(exTreeData)

  if (exTreeData.length !== 0) {
    ousMemoryTreeData.value = [...exTreeData]
  }
})
</script>
<template>
  <div>
    <div class="menu-bar-dense row items-center justify-end">
      <q-btn
        v-if="!stateStore.state"
        rounded
        unelevated
        color="positive"
        size="md"
        padding="0.1px 12px"
        class="q-mx-sm"
        @click="
          () => {
            startOpcuaServerSimulator()
          }
        "
      >
        실행
      </q-btn>
      <q-btn
        v-if="stateStore.state"
        rounded
        unelevated
        color="negative"
        size="md"
        padding="0.1px 12px"
        class="q-mx-sm"
        @click="
          () => {
            sendExitSignal()
          }
        "
      >
        중지
      </q-btn>
      <q-btn
        flat
        color="main"
        size="md"
        padding="2px 12px"
        class="q-mx-sm"
        @click="
          () => {
            console.log(selectedIndex)
            console.log(addDialogToggle)
            if (selectedIndex) {
              addDialogToggle = true
            } else {
              alertMsg('노드를 선택해주세요')
            }
          }
        "
      >
        추가
      </q-btn>
      <q-btn
        flat
        color="main"
        size="md"
        padding="2px 12px"
        class="q-mx-sm"
        @click="
          () => {
            if (selectedIndex) {
              if (selectedIndex === '1') {
                alertMsg('Object 노드는 수정이 불가합니다.')
                return
              }
              updateDialogToggle = true
            } else {
              alertMsg('노드를 선택해주세요')
            }
          }
        "
      >
        수정
      </q-btn>
      <q-btn
        v-if="!isExpanded"
        flat
        color="main"
        size="md"
        padding="2px 12px"
        class="q-mx-sm"
        @click="
          () => {
            treeRef.expandAll()
            isExpanded = true
          }
        "
      >
        펼치기
      </q-btn>
      <q-btn
        v-if="isExpanded"
        flat
        color="main"
        size="md"
        padding="2px 12px"
        class="q-mx-sm"
        @click="
          () => {
            treeRef.collapseAll()
            isExpanded = false
          }
        "
      >
        접기
      </q-btn>
      <q-separator vertical />
      <q-btn
        flat
        color="negative"
        size="md"
        padding="2px 12px"
        class="q-mx-sm"
        @click="
          () => {
            if (selectedIndex) removeItem(ousMemoryTreeData, selectedIndex)
          }
        "
      >
        삭제
      </q-btn>
      <q-btn
        flat
        color="negative"
        size="md"
        padding="2px 12px"
        class="q-mx-sm"
        @click="
          () => {
            ousMemoryTreeDataStore.deleteMemoryTreeData()
            resetTreeData()
          }
        "
      >
        전체 삭제
      </q-btn>
    </div>
    <q-tree
      class="col-12 col-sm-6"
      :nodes="ousMemoryTreeData"
      v-model:ticked="tickedNodes"
      ref="treeRef"
      node-key="id"
      label-key="label"
      tick-strategy="strict"
      default-expand-all
      @update:ticked="
        () => {
          if (tickedNodes.length === 2) tickedNodes.shift()
          selectedIndex = tickedNodes[0]
          console.log(selectedIndex)
        }
      "
    />
    <OUSMemoryAddDialog v-model:treeData="ousMemoryTreeData" v-model="addDialogToggle" :selectedIndex="selectedIndex" />
    <OUSMemoryUpdateDialog v-model:treeData="ousMemoryTreeData" v-model="updateDialogToggle" :selectedIndex="selectedIndex" />
  </div>
</template>
