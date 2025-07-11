import request from '@/utils/request'

/**
 * 合同管理API接口
 */

// 获取合同列表
export function getContractList(params) {
  return request({
    url: '/api/contracts',
    method: 'get',
    params
  })
}

// 获取合同详情
export function getContractById(id) {
  return request({
    url: `/api/contracts/${id}`,
    method: 'get'
  })
}

// 创建合同
export function createContract(data) {
  return request({
    url: '/api/contracts',
    method: 'post',
    data
  })
}

// 更新合同
export function updateContract(id, data) {
  return request({
    url: `/api/contracts/${id}`,
    method: 'put',
    data
  })
}

// 删除合同
export function deleteContract(id) {
  return request({
    url: `/api/contracts/${id}`,
    method: 'delete'
  })
} 