import request from '@/utils/request'

/**
 * 质量追溯API
 * 提供质量追溯、缺陷分析、成本关联等功能
 */

// 分页查询质量追溯记录列表
export function getQualityTraceList(params) {
  return request({
    url: '/api/quality/traces',
    method: 'get',
    params
  })
}

// 根据ID查询质量追溯记录详情
export function getQualityTraceById(traceId) {
  return request({
    url: `/api/quality/traces/${traceId}`,
    method: 'get'
  })
}

// 根据追溯码查询质量追溯记录
export function getQualityTraceByCode(traceCode) {
  return request({
    url: `/api/quality/traces/by-code/${traceCode}`,
    method: 'get'
  })
}

// 创建质量追溯记录
export function createQualityTrace(data) {
  return request({
    url: '/api/quality/traces',
    method: 'post',
    data
  })
}

// 更新质量追溯记录
export function updateQualityTrace(traceId, data) {
  return request({
    url: `/api/quality/traces/${traceId}`,
    method: 'put',
    data
  })
}

// 删除质量追溯记录
export function deleteQualityTrace(traceId) {
  return request({
    url: `/api/quality/traces/${traceId}`,
    method: 'delete'
  })
}

// 获取缺陷分析数据
export function getDefectAnalysis(params) {
  return request({
    url: '/api/quality/defect-analysis',
    method: 'get',
    params
  })
}

// 获取质量成本分析
export function getQualityCostAnalysis(params) {
  return request({
    url: '/api/quality/cost-analysis',
    method: 'get',
    params
  })
}

// 获取产品全生命周期追溯
export function getProductLifecycle(traceCode) {
  return request({
    url: `/api/quality/lifecycle/${traceCode}`,
    method: 'get'
  })
}

// 生成追溯码
export function generateTraceCode(productCode, batchNo) {
  return request({
    url: '/api/quality/generate-trace-code',
    method: 'post',
    params: {
      productCode,
      batchNo
    }
  })
}

// 批量处理缺陷产品
export function batchTreatDefects(traceIds, treatmentMethod, treatedBy) {
  return request({
    url: '/api/quality/batch-treat-defects',
    method: 'post',
    params: {
      traceIds,
      treatmentMethod,
      treatedBy
    }
  })
} 