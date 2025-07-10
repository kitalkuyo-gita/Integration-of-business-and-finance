import request from '@/utils/request'

// 获取会计科目列表
export function getAccountList(params) {
  return request({
    url: '/finance/accounts',
    method: 'get',
    params
  })
}

// 创建会计科目
export function createAccount(data) {
  return request({
    url: '/finance/accounts',
    method: 'post',
    data
  })
}

// 更新会计科目
export function updateAccount(id, data) {
  return request({
    url: `/finance/accounts/${id}`,
    method: 'put',
    data
  })
}

// 删除会计科目
export function deleteAccount(id) {
  return request({
    url: `/finance/accounts/${id}`,
    method: 'delete'
  })
}

// 获取凭证列表
export function getVoucherList(params) {
  return request({
    url: '/finance/vouchers',
    method: 'get',
    params
  })
}

// 创建凭证
export function createVoucher(data) {
  return request({
    url: '/finance/vouchers',
    method: 'post',
    data
  })
}

// 更新凭证
export function updateVoucher(id, data) {
  return request({
    url: `/finance/vouchers/${id}`,
    method: 'put',
    data
  })
}

// 删除凭证
export function deleteVoucher(id) {
  return request({
    url: `/finance/vouchers/${id}`,
    method: 'delete'
  })
}

// 审核凭证
export function auditVoucher(id, data) {
  return request({
    url: `/finance/vouchers/${id}/audit`,
    method: 'post',
    data
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