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

export type OUCNetworkData = {
  endpointurl: string
  securitymode?: 'None' | 'Sign' | 'SignAndEncrypt'
  securitypolicy?: 'None' | 'Basic256' | 'Basic128Rsa15' | 'Basic256Rha256'
  useridentify?: 'Anonymous' | 'UserName'
  username: string
  password?: string
  certFile?: any
  keyFile?: any
  applicationuri?: string
}
export type OUCMemoryData = {
  type?: string
  nodeId?: string
  interval?: number
  queueSize?: number
  discardOldest?: string
}

export type ReadWriterData = {
  name?: string
  type?: string
  nodeId?: string
  invalidmsgtype?: boolean
  invalidmsglength?: boolean
  invalidmsgchunk?: boolean
  inputArguments?: MethodArgumentData[]
  rawBuffer?: number
}
export type MethodArgumentData = {
  dataType: string
  size: string
}

export type OUSNetworkData = {
  ip?: string
  port?: number
  certFile?: any
  keyFile?: any
  users?: { id: string; pw: string }[]
}
export type ArgumentData = {
  name: string
  dataType: string
}

export type OUSMemoryNodeData = {
  id?: string
  label?: string
  category?: string
  type?: string
  accessRight?: string
  inputArguments?: ArgumentData[]
  outputArguments?: ArgumentData[]
  children?: OUSMemoryNodeData[]
}
