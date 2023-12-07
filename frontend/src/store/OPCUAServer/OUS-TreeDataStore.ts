import { useLocalStorage } from '@vueuse/core'
import { defineStore } from 'pinia'
import type { OUSMemoryNodeData } from './../../types'

export const useOUSMemoryTreeStore = defineStore('store', () => {
  const memoryTreeData = useLocalStorage('memoryTreeData', '')
  const getMemoryTreeData = (): OUSMemoryNodeData[] => {
    if (memoryTreeData.value == '') return []
    else return JSON.parse(memoryTreeData.value)
  }
  const setMemoryTreeData = (treeData: OUSMemoryNodeData[]) => {
    memoryTreeData.value = JSON.stringify(treeData)
  }

  const deleteMemoryTreeData = () => {
    memoryTreeData.value = ''
  }
  return {
    memoryTreeData,
    getMemoryTreeData,
    setMemoryTreeData,
    deleteMemoryTreeData,
  }
})
