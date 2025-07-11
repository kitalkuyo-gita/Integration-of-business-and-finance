import request from '@/utils/request'

/**
 * 客户管理API接口
 */

// 获取客户列表
export function getCustomerList(params) {
  return request({
    url: '/api/customers',
    method: 'get',
    params
  })
}

// 获取客户详情
export function getCustomerById(id) {
  return request({
    url: `/api/customers/${id}`,
    method: 'get'
  })
}

// 创建客户
export function createCustomer(data) {
  return request({
    url: '/api/customers',
    method: 'post',
    data
  })
}

// 更新客户
export function updateCustomer(id, data) {
  return request({
    url: `/api/customers/${id}`,
    method: 'put',
    data
  })
}

// 删除客户
export function deleteCustomer(id) {
  return request({
    url: `/api/customers/${id}`,
    method: 'delete'
  })
} 