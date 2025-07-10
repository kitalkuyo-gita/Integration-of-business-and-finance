import request from '@/utils/request'

// 用户登录
export function login(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

// 用户登出
export function logout() {
  return request({
    url: '/auth/logout',
    method: 'post'
  })
}

// 获取用户信息
export function getInfo() {
  return request({
    url: '/auth/profile',
    method: 'get'
  })
}

// 获取用户列表
export function getUserList(params) {
  return request({
    url: '/users',
    method: 'get',
    params
  })
}

// 创建用户
export function createUser(data) {
  return request({
    url: '/users',
    method: 'post',
    data
  })
}

// 更新用户
export function updateUser(id, data) {
  return request({
    url: `/users/${id}`,
    method: 'put',
    data
  })
}

// 删除用户
export function deleteUser(id) {
  return request({
    url: `/users/${id}`,
    method: 'delete'
  })
}

// 批量删除用户
export function deleteUsers(userIds) {
  return request({
    url: '/users/batch',
    method: 'delete',
    data: userIds
  })
}

// 获取用户详情
export function getUserDetail(id) {
  return request({
    url: `/users/${id}`,
    method: 'get'
  })
}

// 激活用户
export function activateUser(id) {
  return request({
    url: `/users/${id}/activate`,
    method: 'put'
  })
}

// 禁用用户
export function deactivateUser(id) {
  return request({
    url: `/users/${id}/deactivate`,
    method: 'put'
  })
}

// 批量激活用户
export function activateUsers(userIds) {
  return request({
    url: '/users/batch/activate',
    method: 'put',
    data: userIds
  })
}

// 批量禁用用户
export function deactivateUsers(userIds) {
  return request({
    url: '/users/batch/deactivate',
    method: 'put',
    data: userIds
  })
}

// 分配角色
export function assignRoles(userId, roleIds) {
  return request({
    url: `/users/${userId}/roles`,
    method: 'put',
    data: roleIds
  })
}

// 移除角色
export function removeRoles(userId, roleIds) {
  return request({
    url: `/users/${userId}/roles`,
    method: 'delete',
    data: roleIds
  })
}

// 修改密码
export function changePassword(userId, oldPassword, newPassword) {
  return request({
    url: `/users/${userId}/password`,
    method: 'put',
    params: {
      oldPassword,
      newPassword
    }
  })
}

// 重置密码
export function resetPassword(userId, newPassword) {
  return request({
    url: `/users/${userId}/password/reset`,
    method: 'put',
    params: {
      newPassword
    }
  })
} 