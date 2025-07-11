import request from '@/utils/request'

/**
 * MES系统集成API
 * 提供工艺参数管理、设备数据采集、成本计算等功能
 */

// 分页查询工艺参数列表
export function getProcessParameterList(params) {
  return request({
    url: '/api/mes/process-parameters',
    method: 'get',
    params
  })
}

// 根据ID查询工艺参数详情
export function getProcessParameterById(parameterId) {
  return request({
    url: `/api/mes/process-parameters/${parameterId}`,
    method: 'get'
  })
}

// 创建工艺参数
export function createProcessParameter(data) {
  return request({
    url: '/api/mes/process-parameters',
    method: 'post',
    data
  })
}

// 更新工艺参数
export function updateProcessParameter(parameterId, data) {
  return request({
    url: `/api/mes/process-parameters/${parameterId}`,
    method: 'put',
    data
  })
}

// 删除工艺参数
export function deleteProcessParameter(parameterId) {
  return request({
    url: `/api/mes/process-parameters/${parameterId}`,
    method: 'delete'
  })
}

// 分页查询设备数据列表
export function getEquipmentDataList(params) {
  return request({
    url: '/api/mes/equipment-data',
    method: 'get',
    params
  })
}

// 根据ID查询设备数据详情
export function getEquipmentDataById(dataId) {
  return request({
    url: `/api/mes/equipment-data/${dataId}`,
    method: 'get'
  })
}

// 创建设备数据
export function createEquipmentData(data) {
  return request({
    url: '/api/mes/equipment-data',
    method: 'post',
    data
  })
}

// 更新设备数据
export function updateEquipmentData(dataId, data) {
  return request({
    url: `/api/mes/equipment-data/${dataId}`,
    method: 'put',
    data
  })
}

// 删除设备数据
export function deleteEquipmentData(dataId) {
  return request({
    url: `/api/mes/equipment-data/${dataId}`,
    method: 'delete'
  })
}

// 计算工艺成本
export function calculateProcessCost(workOrderNo, productCode) {
  return request({
    url: '/api/mes/cost-calculation',
    method: 'post',
    params: {
      workOrderNo,
      productCode
    }
  })
}

// 获取设备能耗分析
export function getEnergyAnalysis(equipmentCode, startTime, endTime) {
  return request({
    url: '/api/mes/energy-analysis',
    method: 'get',
    params: {
      equipmentCode,
      startTime,
      endTime
    }
  })
}

// 获取设备运行效率分析
export function getEfficiencyAnalysis(equipmentCode, startTime, endTime) {
  return request({
    url: '/api/mes/efficiency-analysis',
    method: 'get',
    params: {
      equipmentCode,
      startTime,
      endTime
    }
  })
}

// 同步MES系统数据
export function syncMesData(dataType, data) {
  return request({
    url: `/api/mes/sync/${dataType}`,
    method: 'post',
    data
  })
} 