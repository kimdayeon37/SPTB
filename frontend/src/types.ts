export type NetworkMasterData = {
  protocol: string
  ip?: string
  port?: number
  transactionDelay?: number
  timeout?: number
}
export type MasterSerialNetworkData = {
  protocol?: 'RTU' | 'ASCII'
  comPort?: number
  baudrate?: number
  dataBit?: number
  stopBit?: number
  parity?: 'None' | 'Odd' | 'Even'
  transactionDelay?: number
  timeout?: number
}
export type ReaderData = {
  time?: number
  name?: string
  slaveId?: number
  area: string
  readAddress?: number
  quantity?: number
  scanTime?: number
  byteSwap?: boolean
  wordSwap?: boolean
}

export type WriterData = {
  time?: number
  name?: string
  slaveId?: number
  type?: string
  writeAddress?: number
  readAddress?: number
  readQuantity?: number
  value?: boolean | number
  values: (boolean | number)[]
  invalidFunction?: boolean
  invalidLength?: boolean
  byteSwap?: boolean
  wordSwap?: boolean
  andMask?: string
  orMask?: string
  hexValue?: string
}
