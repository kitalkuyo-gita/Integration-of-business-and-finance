<template>
  <div class="voucher-list">
    <!-- 搜索区域 -->
    <el-card class="search-card">
      <el-form :model="searchForm" :inline="true" @submit.native.prevent>
        <el-form-item label="凭证号">
          <el-input
            v-model="searchForm.voucherNo"
            placeholder="请输入凭证号"
            clearable
            @keyup.enter.native="handleSearch"
          />
        </el-form-item>
        <el-form-item label="凭证类型">
          <el-select v-model="searchForm.voucherType" placeholder="请选择凭证类型" clearable>
            <el-option label="收款" :value="1" />
            <el-option label="付款" :value="2" />
            <el-option label="转账" :value="3" />
            <el-option label="其他" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="草稿" :value="1" />
            <el-option label="已审核" :value="2" />
            <el-option label="已过账" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="日期范围">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮区域 -->
    <el-card class="action-card">
      <el-button type="primary" @click="handleAdd">新增凭证</el-button>
    </el-card>

    <!-- 表格区域 -->
    <el-card>
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column prop="voucherNo" label="凭证号" width="120" />
        <el-table-column prop="voucherDate" label="凭证日期" width="100" />
        <el-table-column prop="summary" label="摘要" min-width="200" />
        <el-table-column prop="totalAmount" label="总金额" width="120">
          <template slot-scope="scope">
            {{ formatAmount(scope.row.totalAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="voucherTypeName" label="凭证类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="getVoucherTypeTag(scope.row.voucherType)">
              {{ scope.row.voucherTypeName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="statusName" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ scope.row.statusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button 
              v-if="scope.row.status === 1"
              type="text" 
              @click="handleAudit(scope.row)"
            >审核</el-button>
            <el-button type="text" style="color: #f56c6c" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        :current-page="pagination.current"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.size"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- 凭证表单对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      @close="handleDialogClose"
    >
      <el-form
        ref="voucherForm"
        :model="voucherForm"
        :rules="voucherRules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="凭证号" prop="voucherNo">
              <el-input v-model="voucherForm.voucherNo" placeholder="请输入凭证号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="凭证日期" prop="voucherDate">
              <el-date-picker
                v-model="voucherForm.voucherDate"
                type="date"
                placeholder="选择凭证日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="凭证类型" prop="voucherType">
              <el-select v-model="voucherForm.voucherType" placeholder="请选择凭证类型">
                <el-option label="收款" :value="1" />
                <el-option label="付款" :value="2" />
                <el-option label="转账" :value="3" />
                <el-option label="其他" :value="4" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="总金额" prop="totalAmount">
              <el-input-number
                v-model="voucherForm.totalAmount"
                :precision="2"
                :step="1000"
                :min="0"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="摘要" prop="summary">
          <el-input v-model="voucherForm.summary" type="textarea" placeholder="请输入摘要" />
        </el-form-item>
        
        <!-- 凭证明细 -->
        <el-form-item label="凭证明细">
          <div class="detail-table">
            <el-table :data="voucherForm.details" border>
              <el-table-column label="科目" width="200">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.accountId" placeholder="选择科目" style="width: 100%">
                    <el-option
                      v-for="account in accountOptions"
                      :key="account.id"
                      :label="account.accountName"
                      :value="account.id"
                    />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column label="方向" width="100">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.direction" placeholder="选择方向">
                    <el-option label="借" :value="1" />
                    <el-option label="贷" :value="2" />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column label="金额" width="150">
                <template slot-scope="scope">
                  <el-input-number
                    v-model="scope.row.amount"
                    :precision="2"
                    :step="1000"
                    :min="0"
                    style="width: 100%"
                  />
                </template>
              </el-table-column>
              <el-table-column label="摘要" min-width="200">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.summary" placeholder="请输入摘要" />
                </template>
              </el-table-column>
              <el-table-column label="操作" width="100">
                <template slot-scope="scope">
                  <el-button type="text" @click="removeDetail(scope.$index)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-button type="text" @click="addDetail">添加明细</el-button>
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getVoucherList, createVoucher, updateVoucher, deleteVoucher, auditVoucher } from '@/api/finance'

export default {
  name: 'VoucherList',
  data() {
    return {
      loading: false,
      searchForm: {
        voucherNo: '',
        voucherType: null,
        status: null
      },
      dateRange: [],
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '',
      voucherForm: {
        voucherNo: '',
        voucherDate: '',
        summary: '',
        totalAmount: 0,
        voucherType: 1,
        details: []
      },
      voucherRules: {
        voucherNo: [
          { required: true, message: '请输入凭证号', trigger: 'blur' }
        ],
        voucherDate: [
          { required: true, message: '请选择凭证日期', trigger: 'change' }
        ],
        summary: [
          { required: true, message: '请输入摘要', trigger: 'blur' }
        ],
        totalAmount: [
          { required: true, message: '请输入总金额', trigger: 'blur' }
        ],
        voucherType: [
          { required: true, message: '请选择凭证类型', trigger: 'change' }
        ]
      },
      accountOptions: [
        { id: 1, accountName: '现金' },
        { id: 2, accountName: '银行存款' },
        { id: 3, accountName: '应收账款' },
        { id: 4, accountName: '应付账款' },
        { id: 5, accountName: '主营业务收入' },
        { id: 6, accountName: '主营业务成本' }
      ],
      isEdit: false,
      currentId: null
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    // 获取数据
    async fetchData() {
      this.loading = true
      try {
        const params = {
          current: this.pagination.current,
          size: this.pagination.size,
          ...this.searchForm
        }
        
        if (this.dateRange && this.dateRange.length === 2) {
          params.startDate = this.dateRange[0]
          params.endDate = this.dateRange[1]
        }
        
        const response = await getVoucherList(params)
        if (response.code === 200) {
          this.tableData = response.data.records
          this.pagination.total = response.data.total
        }
      } catch (error) {
        this.$message.error('获取凭证列表失败')
        console.error(error)
      } finally {
        this.loading = false
      }
    },

    // 搜索
    handleSearch() {
      this.pagination.current = 1
      this.fetchData()
    },

    // 重置
    handleReset() {
      this.searchForm = {
        voucherNo: '',
        voucherType: null,
        status: null
      }
      this.dateRange = []
      this.handleSearch()
    },

    // 新增
    handleAdd() {
      this.isEdit = false
      this.dialogTitle = '新增凭证'
      this.dialogVisible = true
      this.resetForm()
    },

    // 编辑
    handleEdit(row) {
      this.isEdit = true
      this.currentId = row.id
      this.dialogTitle = '编辑凭证'
      this.dialogVisible = true
      this.voucherForm = { ...row }
    },

    // 查看
    handleView(row) {
      this.isEdit = true
      this.dialogTitle = '查看凭证'
      this.dialogVisible = true
      this.voucherForm = { ...row }
    },

    // 删除
    handleDelete(row) {
      this.$confirm('确定要删除该凭证吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteVoucher(row.id)
          if (response.code === 200) {
            this.$message.success('删除成功')
            this.fetchData()
          } else {
            this.$message.error(response.message || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除失败')
          console.error(error)
        }
      })
    },

    // 审核
    handleAudit(row) {
      this.$confirm('确定要审核该凭证吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await auditVoucher(row.id, 1) // 假设当前用户ID为1
          if (response.code === 200) {
            this.$message.success('审核成功')
            this.fetchData()
          } else {
            this.$message.error(response.message || '审核失败')
          }
        } catch (error) {
          this.$message.error('审核失败')
          console.error(error)
        }
      })
    },

    // 提交表单
    async handleSubmit() {
      this.$refs.voucherForm.validate(async (valid) => {
        if (valid) {
          try {
            let response
            if (this.isEdit) {
              response = await updateVoucher(this.currentId, this.voucherForm)
            } else {
              response = await createVoucher(this.voucherForm)
            }
            
            if (response.code === 200) {
              this.$message.success(this.isEdit ? '更新成功' : '创建成功')
              this.dialogVisible = false
              this.fetchData()
            } else {
              this.$message.error(response.message || '操作失败')
            }
          } catch (error) {
            this.$message.error('操作失败')
            console.error(error)
          }
        }
      })
    },

    // 添加明细
    addDetail() {
      this.voucherForm.details.push({
        accountId: null,
        direction: 1,
        amount: 0,
        summary: ''
      })
    },

    // 删除明细
    removeDetail(index) {
      this.voucherForm.details.splice(index, 1)
    },

    // 重置表单
    resetForm() {
      this.voucherForm = {
        voucherNo: '',
        voucherDate: '',
        summary: '',
        totalAmount: 0,
        voucherType: 1,
        details: []
      }
      this.addDetail() // 默认添加一条明细
      this.$nextTick(() => {
        this.$refs.voucherForm && this.$refs.voucherForm.clearValidate()
      })
    },

    // 对话框关闭
    handleDialogClose() {
      this.resetForm()
    },

    // 分页大小改变
    handleSizeChange(size) {
      this.pagination.size = size
      this.fetchData()
    },

    // 当前页改变
    handleCurrentChange(current) {
      this.pagination.current = current
      this.fetchData()
    },

    // 格式化金额
    formatAmount(amount) {
      if (!amount) return '0.00'
      return new Intl.NumberFormat('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      }).format(amount)
    },

    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return ''
      return new Date(dateTime).toLocaleString()
    },

    // 获取凭证类型标签
    getVoucherTypeTag(type) {
      switch (type) {
        case 1: return 'success'
        case 2: return 'danger'
        case 3: return 'warning'
        case 4: return 'info'
        default: return 'info'
      }
    },

    // 获取状态类型
    getStatusType(status) {
      switch (status) {
        case 1: return 'info'
        case 2: return 'warning'
        case 3: return 'success'
        default: return 'info'
      }
    }
  }
}
</script>

<style scoped>
.voucher-list {
  padding: 20px;
}

.search-card {
  margin-bottom: 20px;
}

.action-card {
  margin-bottom: 20px;
}

.dialog-footer {
  text-align: right;
}

.detail-table {
  margin-top: 10px;
}
</style> 