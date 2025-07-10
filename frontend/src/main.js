import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './styles/index.scss'

// 引入全局工具
import './utils/auth'
import './utils/request'

Vue.config.productionTip = false

// 使用Element UI
Vue.use(ElementUI)

// 全局过滤器
Vue.filter('formatDate', function (value) {
  if (!value) return ''
  const date = new Date(value)
  return date.toLocaleDateString()
})

// 全局混入
Vue.mixin({
  methods: {
    // 格式化金额
    formatMoney(value) {
      if (!value) return '0.00'
      return parseFloat(value).toFixed(2)
    },
    // 格式化百分比
    formatPercent(value) {
      if (!value) return '0%'
      return parseFloat(value).toFixed(2) + '%'
    }
  }
})

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app') 