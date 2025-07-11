import request from '@/utils/request'

/**
 * 财务管理API接口
 */

// 获取凭证列表
export function getVoucherList(params) {
  return request({
    url: '/api/finance/vouchers',
    method: 'get',
    params
  })
}

// 获取凭证详情
export function getVoucherById(id) {
  return request({
    url: `/api/finance/vouchers/${id}`,
    method: 'get'
  })
}

// 创建凭证
export function createVoucher(data) {
  return request({
    url: '/api/finance/vouchers',
    method: 'post',
    data
  })
}

// 更新凭证
export function updateVoucher(id, data) {
  return request({
    url: `/api/finance/vouchers/${id}`,
    method: 'put',
    data
  })
}

// 删除凭证
export function deleteVoucher(id) {
  return request({
    url: `/api/finance/vouchers/${id}`,
    method: 'delete'
  })
}

// 审核凭证
export function auditVoucher(id, auditUserId) {
  return request({
    url: `/api/finance/vouchers/${id}/audit`,
    method: 'post',
    params: { auditUserId }
  })
}

// 获取财务报表
export function getFinanceReport(params) {
  return request({
    url: '/finance/reports',
    method: 'get',
    params
  })
}

// 获取资产负债表
export function getBalanceSheet(params) {
  return request({
    url: '/finance/reports/balance-sheet',
    method: 'get',
    params
  })
}

// 获取利润表
export function getIncomeStatement(params) {
  return request({
    url: '/finance/reports/income-statement',
    method: 'get',
    params
  })
}

// 获取现金流量表
export function getCashFlowStatement(params) {
  return request({
    url: '/finance/reports/cash-flow',
    method: 'get',
    params
  })
} 