<template>
  <div class="order-list">
    <div class="page-header">
      <h2>电商订单管理</h2>
      <div class="header-actions">
        <el-button type="primary" @click="handleSyncOrders">同步订单</el-button>
        <el-button type="success" @click="handleBatchUpdate">批量更新</el-button>
      </div>
    </div>

    <!-- 搜索条件 -->
    <el-card class="search-card">
      <el-form :model="searchForm" :inline="true" @submit.native.prevent>
        <el-form-item label="平台类型">
          <el-select v-model="searchForm.platformType" placeholder="请选择平台" clearable>
            <el-option label="淘宝" value="TAOBAO"></el-option>
            <el-option label="京东" value="JD"></el-option>
            <el-option label="拼多多" value="PINDUODUO"></el-option>
            <el-option label="抖音" value="DOUYIN"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="渠道类型">
          <el-select v-model="searchForm.channelType" placeholder="请选择渠道" clearable>
            <el-option label="线上" value="ONLINE"></el-option>
            <el-option label="直播" value="LIVE"></el-option>
            <el-option label="批发" value="WHOLESALE"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select v-model="searchForm.orderStatus" placeholder="请选择状态" clearable>
            <el-option label="待付款" value="PENDING"></el-option>
            <el-option label="已付款" value="PAID"></el-option>
            <el-option label="已发货" value="SHIPPED"></el-option>
            <el-option label="已送达" value="DELIVERED"></el-option>
            <el-option label="已完成" value="COMPLETED"></el-option>
            <el-option label="已取消" value="CANCELLED"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="客户姓名">
          <el-input v-model="searchForm.customerName" placeholder="请输入客户姓名"></el-input>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="searchForm.timeRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            format="yyyy-MM-dd HH:mm:ss"
            value-format="yyyy-MM-dd HH:mm:ss">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card">
      <div class="table-header">
        <div class="table-title">订单列表</div>
        <div class="table-actions">
          <el-button type="primary" @click="handleAdd">新增订单</el-button>
          <el-button @click="handleExport">导出数据</el-button>
        </div>
      </div>

      <el-table
        :data="tableData"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        style="width: 100%">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="orderNo" label="订单编号" width="180"></el-table-column>
        <el-table-column prop="externalOrderNo" label="外部订单号" width="180"></el-table-column>
        <el-table-column prop="platformType" label="平台类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="getPlatformTypeTag(scope.row.platformType)">
              {{ getPlatformTypeText(scope.row.platformType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="channelType" label="渠道类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="getChannelTypeTag(scope.row.channelType)">
              {{ getChannelTypeText(scope.row.channelType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="customerName" label="客户姓名" width="120"></el-table-column>
        <el-table-column prop="totalAmount" label="订单金额" width="120">
          <template slot-scope="scope">
            ¥{{ scope.row.totalAmount }}
          </template>
        </el-table-column>
        <el-table-column prop="paidAmount" label="实付金额" width="120">
          <template slot-scope="scope">
            ¥{{ scope.row.paidAmount }}
          </template>
        </el-table-column>
        <el-table-column prop="orderStatus" label="订单状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getOrderStatusTag(scope.row.orderStatus)">
              {{ getOrderStatusText(scope.row.orderStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="paymentStatus" label="支付状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getPaymentStatusTag(scope.row.paymentStatus)">
              {{ getPaymentStatusText(scope.row.paymentStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" @click="handleUpdateStatus(scope.row)">更新状态</el-button>
            <el-button type="text" style="color: #F56C6C" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.current"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total">
        </el-pagination>
      </div>
    </el-card>

    <!-- 更新状态对话框 -->
    <el-dialog title="更新订单状态" :visible.sync="statusDialogVisible" width="500px">
      <el-form :model="statusForm" label-width="100px">
        <el-form-item label="订单状态">
          <el-select v-model="statusForm.orderStatus" placeholder="请选择订单状态">
            <el-option label="待付款" value="PENDING"></el-option>
            <el-option label="已付款" value="PAID"></el-option>
            <el-option label="已发货" value="SHIPPED"></el-option>
            <el-option label="已送达" value="DELIVERED"></el-option>
            <el-option label="已完成" value="COMPLETED"></el-option>
            <el-option label="已取消" value="CANCELLED"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="statusDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmUpdateStatus">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getOrderList, updateOrderStatus, deleteOrder, syncExternalOrder } from '@/api/ecommerce'
import { formatDateTime } from '@/utils/date'

export default {
  name: 'OrderList',
  data() {
    return {
      loading: false,
      searchForm: {
        platformType: '',
        channelType: '',
        orderStatus: '',
        customerName: '',
        timeRange: []
      },
      tableData: [],
      selectedRows: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      statusDialogVisible: false,
      statusForm: {
        orderId: null,
        orderStatus: ''
      }
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const params = {
          current: this.pagination.current,
          size: this.pagination.size,
          ...this.searchForm,
          startTime: this.searchForm.timeRange[0],
          endTime: this.searchForm.timeRange[1]
        }
        
        const response = await getOrderList(params)
        if (response.code === 200) {
          this.tableData = response.data.records
          this.pagination.total = response.data.total
        } else {
          this.$message.error(response.message || '获取数据失败')
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },

    // 搜索
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },

    // 重置
    handleReset() {
      this.searchForm = {
        platformType: '',
        channelType: '',
        orderStatus: '',
        customerName: '',
        timeRange: []
      }
      this.handleSearch()
    },

    // 新增
    handleAdd() {
      this.$router.push('/ecommerce/order-detail/new')
    },

    // 查看
    handleView(row) {
      this.$router.push(`/ecommerce/order-detail/${row.id}`)
    },

    // 编辑
    handleEdit(row) {
      this.$router.push(`/ecommerce/order-detail/${row.id}?edit=true`)
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除这个订单吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await deleteOrder(row.id)
        if (response.code === 200) {
          this.$message.success('删除成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败:', error)
          this.$message.error('删除失败')
        }
      }
    },

    // 更新状态
    handleUpdateStatus(row) {
      this.statusForm.orderId = row.id
      this.statusForm.orderStatus = row.orderStatus
      this.statusDialogVisible = true
    },

    // 确认更新状态
    async confirmUpdateStatus() {
      try {
        const response = await updateOrderStatus(
          this.statusForm.orderId,
          this.statusForm.orderStatus,
          this.$store.state.user.id
        )
        if (response.code === 200) {
          this.$message.success('状态更新成功')
          this.statusDialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.message || '状态更新失败')
        }
      } catch (error) {
        console.error('状态更新失败:', error)
        this.$message.error('状态更新失败')
      }
    },

    // 同步订单
    async handleSyncOrders() {
      try {
        await this.$confirm('确定要同步外部平台订单吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        // 这里可以添加同步逻辑
        this.$message.success('同步任务已启动')
      } catch (error) {
        if (error !== 'cancel') {
          console.error('同步失败:', error)
          this.$message.error('同步失败')
        }
      }
    },

    // 批量更新
    handleBatchUpdate() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要更新的订单')
        return
      }
      // 实现批量更新逻辑
    },

    // 导出数据
    handleExport() {
      // 实现导出逻辑
      this.$message.success('导出功能开发中')
    },

    // 选择变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    // 分页大小变化
    handleSizeChange(size) {
      this.pagination.size = size
      this.loadData()
    },

    // 当前页变化
    handleCurrentChange(current) {
      this.pagination.current = current
      this.loadData()
    },

    // 格式化日期时间
    formatDateTime,

    // 获取平台类型标签
    getPlatformTypeTag(type) {
      const tags = {
        'TAOBAO': 'success',
        'JD': 'primary',
        'PINDUODUO': 'warning',
        'DOUYIN': 'danger'
      }
      return tags[type] || 'info'
    },

    getPlatformTypeText(type) {
      const texts = {
        'TAOBAO': '淘宝',
        'JD': '京东',
        'PINDUODUO': '拼多多',
        'DOUYIN': '抖音'
      }
      return texts[type] || type
    },

    getChannelTypeTag(type) {
      const tags = {
        'ONLINE': 'success',
        'LIVE': 'warning',
        'WHOLESALE': 'info'
      }
      return tags[type] || 'info'
    },

    getChannelTypeText(type) {
      const texts = {
        'ONLINE': '线上',
        'LIVE': '直播',
        'WHOLESALE': '批发'
      }
      return texts[type] || type
    },

    getOrderStatusTag(status) {
      const tags = {
        'PENDING': 'warning',
        'PAID': 'success',
        'SHIPPED': 'primary',
        'DELIVERED': 'success',
        'COMPLETED': 'success',
        'CANCELLED': 'danger'
      }
      return tags[status] || 'info'
    },

    getOrderStatusText(status) {
      const texts = {
        'PENDING': '待付款',
        'PAID': '已付款',
        'SHIPPED': '已发货',
        'DELIVERED': '已送达',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      }
      return texts[status] || status
    },

    getPaymentStatusTag(status) {
      const tags = {
        'UNPAID': 'danger',
        'PAID': 'success',
        'REFUNDED': 'warning'
      }
      return tags[status] || 'info'
    },

    getPaymentStatusText(status) {
      const texts = {
        'UNPAID': '未支付',
        'PAID': '已支付',
        'REFUNDED': '已退款'
      }
      return texts[status] || status
    }
  }
}
</script>

<style scoped>
.order-list {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  color: #303133;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.search-card {
  margin-bottom: 20px;
}

.table-card {
  margin-bottom: 20px;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.table-title {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}

.table-actions {
  display: flex;
  gap: 10px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.dialog-footer {
  text-align: right;
}
</style> 