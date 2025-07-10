import Cookies from 'js-cookie'

const TokenKey = 'IOBAF-Token'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

// 用户信息相关
const UserKey = 'IOBAF-User'

export function getUserInfo() {
  const userStr = localStorage.getItem(UserKey)
  return userStr ? JSON.parse(userStr) : null
}

export function setUserInfo(user) {
  return localStorage.setItem(UserKey, JSON.stringify(user))
}

export function removeUserInfo() {
  return localStorage.removeItem(UserKey)
}

// 权限相关
export function hasPermission(permission) {
  const user = getUserInfo()
  if (!user || !user.permissions) {
    return false
  }
  return user.permissions.includes(permission)
}

export function hasRole(role) {
  const user = getUserInfo()
  if (!user || !user.roles) {
    return false
  }
  return user.roles.includes(role)
} 