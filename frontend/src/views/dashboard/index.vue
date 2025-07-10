<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <!-- 统计卡片 -->
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon user-icon">
              <i class="el-icon-user"></i>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ stats.userCount }}</div>
              <div class="stat-label">用户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon customer-icon">
              <i class="el-icon-user-solid"></i>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ stats.customerCount }}</div>
              <div class="stat-label">客户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon contract-icon">
              <i class="el-icon-document"></i>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ stats.contractCount }}</div>
              <div class="stat-label">合同总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon finance-icon">
              <i class="el-icon-money"></i>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ formatMoney(stats.totalAmount) }}</div>
              <div class="stat-label">合同总金额</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <!-- 图表区域 -->
      <el-col :span="12">
        <el-card>
          <div slot="header">
            <span>月度合同金额趋势</span>
          </div>
          <div ref="monthlyChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card>
          <div slot="header">
            <span>客户分布</span>
          </div>
          <div ref="customerChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <!-- 最近活动 -->
      <el-col :span="12">
        <el-card>
          <div slot="header">
            <span>最近活动</span>
          </div>
          <el-timeline>
            <el-timeline-item
              v-for="(activity, index) in recentActivities"
              :key="index"
              :timestamp="activity.time"
              :type="activity.type"
            >
              {{ activity.content }}
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
      
      <!-- 待办事项 -->
      <el-col :span="12">
        <el-card>
          <div slot="header">
            <span>待办事项</span>
          </div>
          <el-table :data="todoList" size="small">
            <el-table-column prop="title" label="标题" />
            <el-table-column prop="priority" label="优先级" width="80">
              <template slot-scope="scope">
                <el-tag :type="getPriorityType(scope.row.priority)" size="small">
                  {{ getPriorityText(scope.row.priority) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="deadline" label="截止时间" width="120" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'Dashboard',
  data() {
    return {
      // 统计数据
      stats: {
        userCount: 125,
        customerCount: 342,
        contractCount: 89,
        totalAmount: 12500000
      },
      // 最近活动
      recentActivities: [
        {
          content: '新增客户：北京科技有限公司',
          time: '2024-01-15 14:30',
          type: 'primary'
        },
        {
          content: '签订合同：软件开发项目',
          time: '2024-01-15 10:20',
          type: 'success'
        },
        {
          content: '用户登录：张三',
          time: '2024-01-15 09:15',
          type: 'info'
        },
        {
          content: '系统维护：数据库备份完成',
          time: '2024-01-15 02:00',
          type: 'warning'
        }
      ],
      // 待办事项
      todoList: [
        {
          title: '审核合同：软件开发项目',
          priority: 'high',
          deadline: '2024-01-16'
        },
        {
          title: '客户回访：北京科技有限公司',
          priority: 'medium',
          deadline: '2024-01-17'
        },
        {
          title: '财务报表生成',
          priority: 'low',
          deadline: '2024-01-18'
        }
      ],
      // 图表实例
      monthlyChart: null,
      customerChart: null
    }
  },
  mounted() {
    this.initCharts()
  },
  beforeDestroy() {
    if (this.monthlyChart) {
      this.monthlyChart.dispose()
    }
    if (this.customerChart) {
      this.customerChart.dispose()
    }
  },
  methods: {
    // 初始化图表
    initCharts() {
      this.initMonthlyChart()
      this.initCustomerChart()
    },

    // 初始化月度趋势图
    initMonthlyChart() {
      this.monthlyChart = echarts.init(this.$refs.monthlyChart)
      
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: [820, 932, 901, 934, 1290, 1330, 1320, 1450, 1200, 1100, 1400, 1600],
          type: 'line',
          smooth: true,
          areaStyle: {
            opacity: 0.3
          }
        }]
      }
      
      this.monthlyChart.setOption(option)
    },

    // 初始化客户分布图
    initCustomerChart() {
      this.customerChart = echarts.init(this.$refs.customerChart)
      
      const option = {
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '客户分布',
            type: 'pie',
            radius: '50%',
            data: [
              { value: 1048, name: '制造业' },
              { value: 735, name: '服务业' },
              { value: 580, name: '科技行业' },
              { value: 484, name: '金融业' },
              { value: 300, name: '其他' }
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      }
      
      this.customerChart.setOption(option)
    },

    // 格式化金额
    formatMoney(amount) {
      return (amount / 10000).toFixed(1) + '万'
    },

    // 获取优先级类型
    getPriorityType(priority) {
      const typeMap = {
        high: 'danger',
        medium: 'warning',
        low: 'info'
      }
      return typeMap[priority] || 'info'
    },

    // 获取优先级文本
    getPriorityText(priority) {
      const textMap = {
        high: '高',
        medium: '中',
        low: '低'
      }
      return textMap[priority] || '未知'
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  padding: 20px;
  
  .stat-card {
    .stat-content {
      display: flex;
      align-items: center;
      
      .stat-icon {
        width: 60px;
        height: 60px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 15px;
        
        i {
          font-size: 24px;
          color: #fff;
        }
        
        &.user-icon {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        
        &.customer-icon {
          background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }
        
        &.contract-icon {
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }
        
        &.finance-icon {
          background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
        }
      }
      
      .stat-info {
        flex: 1;
        
        .stat-number {
          font-size: 24px;
          font-weight: bold;
          color: #303133;
          line-height: 1;
        }
        
        .stat-label {
          font-size: 14px;
          color: #909399;
          margin-top: 5px;
        }
      }
    }
  }
}
</style> 