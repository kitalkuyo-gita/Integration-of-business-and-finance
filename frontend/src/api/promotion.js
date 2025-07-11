import request from '@/utils/request'

/**
 * 促销费用分摊引擎API
 * 提供促销规则管理、费用分摊计算、ROI分析等功能
 */

// 分页查询促销规则列表
export function getPromotionRuleList(params) {
  return request({
    url: '/api/promotion/rules',
    method: 'get',
    params
  })
}

// 根据ID查询促销规则详情
export function getPromotionRuleById(ruleId) {
  return request({
    url: `/api/promotion/rules/${ruleId}`,
    method: 'get'
  })
}

// 创建促销规则
export function createPromotionRule(data) {
  return request({
    url: '/api/promotion/rules',
    method: 'post',
    data
  })
}

// 更新促销规则
export function updatePromotionRule(ruleId, data) {
  return request({
    url: `/api/promotion/rules/${ruleId}`,
    method: 'put',
    data
  })
}

// 删除促销规则
export function deletePromotionRule(ruleId) {
  return request({
    url: `/api/promotion/rules/${ruleId}`,
    method: 'delete'
  })
}

// 执行费用分摊计算
export function calculateAllocation(promotionId, startTime, endTime) {
  return request({
    url: '/api/promotion/allocation/calculate',
    method: 'post',
    params: {
      promotionId,
      startTime,
      endTime
    }
  })
}

// 查询分摊记录列表
export function getAllocationList(params) {
  return request({
    url: '/api/promotion/allocation',
    method: 'get',
    params
  })
}

// 获取促销ROI分析数据
export function getRoiAnalysis(params) {
  return request({
    url: '/api/promotion/roi-analysis',
    method: 'get',
    params
  })
}

// 获取渠道对比分析数据
export function getChannelComparison(params) {
  return request({
    url: '/api/promotion/channel-comparison',
    method: 'get',
    params
  })
}

// 批量执行费用分摊
export function batchCalculateAllocation(promotionIds, startTime, endTime) {
  return request({
    url: '/api/promotion/allocation/batch',
    method: 'post',
    params: {
      promotionIds,
      startTime,
      endTime
    }
  })
} 