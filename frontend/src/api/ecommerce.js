import request from '@/utils/request'

/**
 * 电商订单管理API
 * 提供电商订单的增删改查、状态更新、数据同步等功能
 */

// 分页查询订单列表
export function getOrderList(params) {
  return request({
    url: '/api/ecommerce/orders',
    method: 'get',
    params
  })
}

// 根据ID查询订单详情
export function getOrderById(orderId) {
  return request({
    url: `/api/ecommerce/orders/${orderId}`,
    method: 'get'
  })
}

// 创建订单
export function createOrder(data) {
  return request({
    url: '/api/ecommerce/orders',
    method: 'post',
    data
  })
}

// 更新订单信息
export function updateOrder(orderId, data) {
  return request({
    url: `/api/ecommerce/orders/${orderId}`,
    method: 'put',
    data
  })
}

// 删除订单
export function deleteOrder(orderId) {
  return request({
    url: `/api/ecommerce/orders/${orderId}`,
    method: 'delete'
  })
}

// 更新订单状态
export function updateOrderStatus(orderId, orderStatus, updatedBy) {
  return request({
    url: `/api/ecommerce/orders/${orderId}/status`,
    method: 'put',
    params: {
      orderStatus,
      updatedBy
    }
  })
}

// 更新支付状态
export function updatePaymentStatus(orderId, paymentStatus, paymentTime, updatedBy) {
  return request({
    url: `/api/ecommerce/orders/${orderId}/payment`,
    method: 'put',
    params: {
      paymentStatus,
      paymentTime,
      updatedBy
    }
  })
}

// 同步外部平台订单
export function syncExternalOrder(platformType, orderData) {
  return request({
    url: `/api/ecommerce/orders/sync/${platformType}`,
    method: 'post',
    data: orderData
  })
}

// 获取订单统计数据
export function getOrderStatistics(params) {
  return request({
    url: '/api/ecommerce/orders/statistics',
    method: 'get',
    params
  })
}

// 获取待发货订单列表
export function getPendingShippingOrders() {
  return request({
    url: '/api/ecommerce/orders/pending-shipping',
    method: 'get'
  })
}

// 获取即将过期商品订单
export function getExpiringProductOrders(days = 30) {
  return request({
    url: '/api/ecommerce/orders/expiring',
    method: 'get',
    params: { days }
  })
}

// 批量更新订单状态
export function batchUpdateOrderStatus(orderIds, orderStatus, updatedBy) {
  return request({
    url: '/api/ecommerce/orders/batch-status',
    method: 'put',
    params: {
      orderIds,
      orderStatus,
      updatedBy
    }
  })
} 