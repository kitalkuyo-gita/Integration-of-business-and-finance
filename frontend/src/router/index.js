import Vue from 'vue'
import VueRouter from 'vue-router'
import { getToken } from '@/utils/auth'

Vue.use(VueRouter)

// 路由配置
const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录', hidden: true }
  },
  {
    path: '/',
    component: () => import('@/layout/index.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '仪表板', icon: 'dashboard' }
      }
    ]
  },
  {
    path: '/user',
    component: () => import('@/layout/index.vue'),
    redirect: '/user/list',
    meta: { title: '用户管理', icon: 'user' },
    children: [
      {
        path: 'list',
        name: 'UserList',
        component: () => import('@/views/user/UserList.vue'),
        meta: { title: '用户列表' }
      }
    ]
  },
  {
    path: '/customer',
    component: () => import('@/layout/index.vue'),
    redirect: '/customer/list',
    meta: { title: '客户管理', icon: 'user' },
    children: [
      {
        path: 'list',
        name: 'CustomerList',
        component: () => import('@/views/customer/CustomerList.vue'),
        meta: { title: '客户列表' }
      }
    ]
  },
  {
    path: '/contract',
    component: () => import('@/layout/index.vue'),
    redirect: '/contract/list',
    meta: { title: '合同管理', icon: 'document' },
    children: [
      {
        path: 'list',
        name: 'ContractList',
        component: () => import('@/views/contract/ContractList.vue'),
        meta: { title: '合同列表' }
      }
    ]
  },
  {
    path: '/finance',
    component: () => import('@/layout/index.vue'),
    redirect: '/finance/voucher',
    meta: { title: '财务管理', icon: 'money' },
    children: [
      {
        path: 'voucher',
        name: 'VoucherList',
        component: () => import('@/views/finance/VoucherList.vue'),
        meta: { title: '凭证管理' }
      }
    ]
  },
  // 第三阶段：美妆快消品行业功能
  {
    path: '/ecommerce',
    component: () => import('@/layout/index.vue'),
    redirect: '/ecommerce/order',
    meta: { title: '电商管理', icon: 'shopping' },
    children: [
      {
        path: 'order',
        name: 'OrderList',
        component: () => import('@/views/ecommerce/OrderList.vue'),
        meta: { title: '订单管理' }
      },
      {
        path: 'statistics',
        name: 'OrderStatistics',
        component: () => import('@/views/ecommerce/OrderStatistics.vue'),
        meta: { title: '订单统计' }
      },
      {
        path: 'expiring',
        name: 'ExpiringProducts',
        component: () => import('@/views/ecommerce/ExpiringProducts.vue'),
        meta: { title: '临期预警' }
      }
    ]
  },
  {
    path: '/promotion',
    component: () => import('@/layout/index.vue'),
    redirect: '/promotion/rule',
    meta: { title: '促销管理', icon: 'gift' },
    children: [
      {
        path: 'rule',
        name: 'PromotionRule',
        component: () => import('@/views/promotion/PromotionRule.vue'),
        meta: { title: '促销规则' }
      },
      {
        path: 'allocation',
        name: 'PromotionAllocation',
        component: () => import('@/views/promotion/PromotionAllocation.vue'),
        meta: { title: '费用分摊' }
      },
      {
        path: 'roi',
        name: 'PromotionRoi',
        component: () => import('@/views/promotion/PromotionRoi.vue'),
        meta: { title: 'ROI分析' }
      }
    ]
  },
  // 第三阶段：精密金属零部件行业功能
  {
    path: '/mes',
    component: () => import('@/layout/index.vue'),
    redirect: '/mes/process',
    meta: { title: 'MES管理', icon: 'factory' },
    children: [
      {
        path: 'process',
        name: 'ProcessParameter',
        component: () => import('@/views/mes/ProcessParameter.vue'),
        meta: { title: '工艺参数' }
      },
      {
        path: 'equipment',
        name: 'EquipmentData',
        component: () => import('@/views/mes/EquipmentData.vue'),
        meta: { title: '设备数据' }
      },
      {
        path: 'cost',
        name: 'ProcessCost',
        component: () => import('@/views/mes/ProcessCost.vue'),
        meta: { title: '成本计算' }
      }
    ]
  },
  {
    path: '/quality',
    component: () => import('@/layout/index.vue'),
    redirect: '/quality/trace',
    meta: { title: '质量管理', icon: 'check-circle' },
    children: [
      {
        path: 'trace',
        name: 'QualityTrace',
        component: () => import('@/views/quality/QualityTrace.vue'),
        meta: { title: '质量追溯' }
      },
      {
        path: 'defect',
        name: 'DefectAnalysis',
        component: () => import('@/views/quality/DefectAnalysis.vue'),
        meta: { title: '缺陷分析' }
      },
      {
        path: 'cost',
        name: 'QualityCost',
        component: () => import('@/views/quality/QualityCost.vue'),
        meta: { title: '质量成本' }
      }
    ]
  },
  {
    path: '/quotation',
    component: () => import('@/layout/index.vue'),
    redirect: '/quotation/request',
    meta: { title: '报价管理', icon: 'calculator' },
    children: [
      {
        path: 'request',
        name: 'QuotationRequest',
        component: () => import('@/views/quotation/QuotationRequest.vue'),
        meta: { title: '报价请求' }
      },
      {
        path: 'result',
        name: 'QuotationResult',
        component: () => import('@/views/quotation/QuotationResult.vue'),
        meta: { title: '报价结果' }
      },
      {
        path: 'analysis',
        name: 'QuotationAnalysis',
        component: () => import('@/views/quotation/QuotationAnalysis.vue'),
        meta: { title: '报价分析' }
      }
    ]
  },
  {
    path: '/system',
    component: () => import('@/layout/index.vue'),
    redirect: '/system/user',
    meta: { title: '系统管理', icon: 'setting' },
    children: [
      {
        path: 'user',
        name: 'UserList',
        component: () => import('@/views/system/user.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'role',
        name: 'RoleList',
        component: () => import('@/views/system/role.vue'),
        meta: { title: '角色管理' }
      }
    ]
  },
  {
    path: '/404',
    component: () => import('@/views/error/404.vue'),
    hidden: true
  },
  {
    path: '*',
    redirect: '/404',
    hidden: true
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title ? to.meta.title + ' - IOBAF系统' : 'IOBAF系统'
  
  const hasToken = getToken()
  
  if (hasToken) {
    if (to.path === '/login') {
      // 如果已登录，重定向到首页
      next({ path: '/' })
    } else {
      next()
    }
  } else {
    // 没有token
    if (to.path === '/login') {
      // 如果是登录页面，直接进入
      next()
    } else {
      // 其他没有访问权限的页面将被重定向到登录页面
      next(`/login?redirect=${to.path}`)
    }
  }
})

export default router 