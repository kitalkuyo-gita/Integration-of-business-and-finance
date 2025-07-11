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
  // 第三阶段：美妆快消品行业功能
  {
    path: '/ecommerce',
    component: () => import('@/layout/index.vue'),
    redirect: '/ecommerce/orders',
    meta: { title: '电商订单管理', icon: 'shopping' },
    children: [
      {
        path: 'orders',
        name: 'OrderList',
        component: () => import('@/views/ecommerce/OrderList.vue'),
        meta: { title: '订单列表' }
      },
      {
        path: 'order-detail/:id',
        name: 'OrderDetail',
        component: () => import('@/views/ecommerce/OrderDetail.vue'),
        meta: { title: '订单详情', hidden: true }
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
    redirect: '/promotion/rules',
    meta: { title: '促销费用分摊', icon: 'chart' },
    children: [
      {
        path: 'rules',
        name: 'PromotionRules',
        component: () => import('@/views/promotion/PromotionRules.vue'),
        meta: { title: '分摊规则' }
      },
      {
        path: 'allocation',
        name: 'PromotionAllocation',
        component: () => import('@/views/promotion/PromotionAllocation.vue'),
        meta: { title: '分摊记录' }
      },
      {
        path: 'roi-analysis',
        name: 'RoiAnalysis',
        component: () => import('@/views/promotion/RoiAnalysis.vue'),
        meta: { title: 'ROI分析' }
      }
    ]
  },
  // 第三阶段：精密金属零部件行业功能
  {
    path: '/mes',
    component: () => import('@/layout/index.vue'),
    redirect: '/mes/process-parameters',
    meta: { title: 'MES系统集成', icon: 'factory' },
    children: [
      {
        path: 'process-parameters',
        name: 'ProcessParameters',
        component: () => import('@/views/mes/ProcessParameters.vue'),
        meta: { title: '工艺参数' }
      },
      {
        path: 'equipment-data',
        name: 'EquipmentData',
        component: () => import('@/views/mes/EquipmentData.vue'),
        meta: { title: '设备数据' }
      },
      {
        path: 'cost-calculation',
        name: 'CostCalculation',
        component: () => import('@/views/mes/CostCalculation.vue'),
        meta: { title: '成本计算' }
      }
    ]
  },
  {
    path: '/quality',
    component: () => import('@/layout/index.vue'),
    redirect: '/quality/traces',
    meta: { title: '质量追溯', icon: 'search' },
    children: [
      {
        path: 'traces',
        name: 'QualityTraces',
        component: () => import('@/views/quality/QualityTraces.vue'),
        meta: { title: '追溯记录' }
      },
      {
        path: 'defect-analysis',
        name: 'DefectAnalysis',
        component: () => import('@/views/quality/DefectAnalysis.vue'),
        meta: { title: '缺陷分析' }
      },
      {
        path: 'lifecycle/:traceCode',
        name: 'ProductLifecycle',
        component: () => import('@/views/quality/ProductLifecycle.vue'),
        meta: { title: '生命周期', hidden: true }
      }
    ]
  },
  {
    path: '/quotation',
    component: () => import('@/layout/index.vue'),
    redirect: '/quotation/requests',
    meta: { title: '定制化报价', icon: 'calculator' },
    children: [
      {
        path: 'requests',
        name: 'QuotationRequests',
        component: () => import('@/views/quotation/QuotationRequests.vue'),
        meta: { title: '报价请求' }
      },
      {
        path: 'results',
        name: 'QuotationResults',
        component: () => import('@/views/quotation/QuotationResults.vue'),
        meta: { title: '报价结果' }
      },
      {
        path: 'quick-quote',
        name: 'QuickQuote',
        component: () => import('@/views/quotation/QuickQuote.vue'),
        meta: { title: '快速报价' }
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