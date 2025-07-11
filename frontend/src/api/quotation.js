import request from '@/utils/request'

/**
 * 定制化订单报价引擎API
 * 提供动态报价、成本计算、风险评估等功能
 */

// 分页查询报价请求列表
export function getQuotationRequestList(params) {
  return request({
    url: '/api/quotation/requests',
    method: 'get',
    params
  })
}

// 根据ID查询报价请求详情
export function getQuotationRequestById(requestId) {
  return request({
    url: `/api/quotation/requests/${requestId}`,
    method: 'get'
  })
}

// 创建报价请求
export function createQuotationRequest(data) {
  return request({
    url: '/api/quotation/requests',
    method: 'post',
    data
  })
}

// 更新报价请求
export function updateQuotationRequest(requestId, data) {
  return request({
    url: `/api/quotation/requests/${requestId}`,
    method: 'put',
    data
  })
}

// 删除报价请求
export function deleteQuotationRequest(requestId) {
  return request({
    url: `/api/quotation/requests/${requestId}`,
    method: 'delete'
  })
}

// 执行报价计算
export function calculateQuotation(requestId) {
  return request({
    url: `/api/quotation/calculate/${requestId}`,
    method: 'post'
  })
}

// 获取报价结果列表
export function getQuotationResultList(params) {
  return request({
    url: '/api/quotation/results',
    method: 'get',
    params
  })
}

// 根据ID查询报价结果详情
export function getQuotationResultById(resultId) {
  return request({
    url: `/api/quotation/results/${resultId}`,
    method: 'get'
  })
}

// 获取成本分析数据
export function getCostAnalysis(requestId) {
  return request({
    url: `/api/quotation/cost-analysis/${requestId}`,
    method: 'get'
  })
}

// 获取风险评估数据
export function getRiskAssessment(requestId) {
  return request({
    url: `/api/quotation/risk-assessment/${requestId}`,
    method: 'get'
  })
}

// 快速报价（简化版）
export function quickQuote(params) {
  return request({
    url: '/api/quotation/quick-quote',
    method: 'post',
    params
  })
}

// 批量报价
export function batchQuote(requestIds) {
  return request({
    url: '/api/quotation/batch-quote',
    method: 'post',
    params: {
      requestIds
    }
  })
}

// 获取报价历史对比
export function getQuotationHistoryComparison(productCode, customerId) {
  return request({
    url: '/api/quotation/history-comparison',
    method: 'get',
    params: {
      productCode,
      customerId
    }
  })
} 