<template>
  <div class="contract-list">
    <!-- 搜索区域 -->
    <el-card class="search-card">
      <el-form :model="searchForm" :inline="true" @submit.native.prevent>
        <el-form-item label="合同名称">
          <el-input
            v-model="searchForm.contractName"
            placeholder="请输入合同名称"
            clearable
            @keyup.enter.native="handleSearch"
          />
        </el-form-item>
        <el-form-item label="合同类型">
          <el-select v-model="searchForm.contractType" placeholder="请选择合同类型" clearable>
            <el-option label="销售合同" :value="1" />
            <el-option label="采购合同" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="草稿" :value="1" />
            <el-option label="审批中" :value="2" />
            <el-option label="已生效" :value="3" />
            <el-option label="已完成" :value="4" />
            <el-option label="已终止" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮区域 -->
    <el-card class="action-card">
      <el-button type="primary" @click="handleAdd">新增合同</el-button>
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
        <el-table-column prop="contractNo" label="合同编号" width="120" />
        <el-table-column prop="contractName" label="合同名称" min-width="200" />
        <el-table-column prop="customerName" label="客户名称" min-width="150" />
        <el-table-column prop="contractAmount" label="合同金额" width="120">
          <template slot-scope="scope">
            {{ formatAmount(scope.row.contractAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="contractTypeName" label="合同类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.contractType === 1 ? 'success' : 'warning'">
              {{ scope.row.contractTypeName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startDate" label="开始日期" width="100" />
        <el-table-column prop="endDate" label="结束日期" width="100" />
        <el-table-column prop="statusName" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag
              :type="getStatusType(scope.row.status)"
            >
              {{ scope.row.statusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" @click="handleEdit(scope.row)">编辑</el-button>
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

    <!-- 合同表单对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="700px"
      @close="handleDialogClose"
    >
      <el-form
        ref="contractForm"
        :model="contractForm"
        :rules="contractRules"
        label-width="100px"
      >
        <el-form-item label="合同编号" prop="contractNo">
          <el-input v-model="contractForm.contractNo" placeholder="请输入合同编号" />
        </el-form-item>
        <el-form-item label="合同名称" prop="contractName">
          <el-input v-model="contractForm.contractName" placeholder="请输入合同名称" />
        </el-form-item>
        <el-form-item label="客户" prop="customerId">
          <el-select v-model="contractForm.customerId" placeholder="请选择客户" filterable>
            <el-option
              v-for="customer in customerOptions"
              :key="customer.id"
              :label="customer.customerName"
              :value="customer.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="合同金额" prop="contractAmount">
          <el-input-number
            v-model="contractForm.contractAmount"
            :precision="2"
            :step="1000"
            :min="0"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="合同类型" prop="contractType">
          <el-select v-model="contractForm.contractType" placeholder="请选择合同类型">
            <el-option label="销售合同" :value="1" />
            <el-option label="采购合同" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始日期" prop="startDate">
          <el-date-picker
            v-model="contractForm.startDate"
            type="date"
            placeholder="选择开始日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="结束日期" prop="endDate">
          <el-date-picker
            v-model="contractForm.endDate"
            type="date"
            placeholder="选择结束日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="contractForm.status" placeholder="请选择状态">
            <el-option label="草稿" :value="1" />
            <el-option label="审批中" :value="2" />
            <el-option label="已生效" :value="3" />
            <el-option label="已完成" :value="4" />
            <el-option label="已终止" :value="5" />
          </el-select>
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
import { getContractList, createContract, updateContract, deleteContract } from '@/api/contract'
import { getCustomerList } from '@/api/customer'

export default {
  name: 'ContractList',
  data() {
    return {
      loading: false,
      searchForm: {
        contractName: '',
        contractType: null,
        status: null
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '',
      contractForm: {
        contractNo: '',
        contractName: '',
        customerId: null,
        contractAmount: 0,
        contractType: 1,
        startDate: '',
        endDate: '',
        status: 1
      },
      contractRules: {
        contractNo: [
          { required: true, message: '请输入合同编号', trigger: 'blur' }
        ],
        contractName: [
          { required: true, message: '请输入合同名称', trigger: 'blur' }
        ],
        customerId: [
          { required: true, message: '请选择客户', trigger: 'change' }
        ],
        contractAmount: [
          { required: true, message: '请输入合同金额', trigger: 'blur' }
        ],
        contractType: [
          { required: true, message: '请选择合同类型', trigger: 'change' }
        ]
      },
      customerOptions: [],
      isEdit: false,
      currentId: null
    }
  },
  created() {
    this.fetchData()
    this.fetchCustomerOptions()
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
        const response = await getContractList(params)
        if (response.code === 200) {
          this.tableData = response.data.records
          this.pagination.total = response.data.total
        }
      } catch (error) {
        this.$message.error('获取合同列表失败')
        console.error(error)
      } finally {
        this.loading = false
      }
    },

    // 获取客户选项
    async fetchCustomerOptions() {
      try {
        const response = await getCustomerList({ size: 1000 })
        if (response.code === 200) {
          this.customerOptions = response.data.records
        }
      } catch (error) {
        console.error('获取客户选项失败:', error)
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
        contractName: '',
        contractType: null,
        status: null
      }
      this.handleSearch()
    },

    // 新增
    handleAdd() {
      this.isEdit = false
      this.dialogTitle = '新增合同'
      this.dialogVisible = true
      this.resetForm()
    },

    // 编辑
    handleEdit(row) {
      this.isEdit = true
      this.currentId = row.id
      this.dialogTitle = '编辑合同'
      this.dialogVisible = true
      this.contractForm = { ...row }
    },

    // 查看
    handleView(row) {
      this.isEdit = true
      this.dialogTitle = '查看合同'
      this.dialogVisible = true
      this.contractForm = { ...row }
    },

    // 删除
    handleDelete(row) {
      this.$confirm('确定要删除该合同吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteContract(row.id)
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

    // 提交表单
    async handleSubmit() {
      this.$refs.contractForm.validate(async (valid) => {
        if (valid) {
          try {
            let response
            if (this.isEdit) {
              response = await updateContract(this.currentId, this.contractForm)
            } else {
              response = await createContract(this.contractForm)
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

    // 重置表单
    resetForm() {
      this.contractForm = {
        contractNo: '',
        contractName: '',
        customerId: null,
        contractAmount: 0,
        contractType: 1,
        startDate: '',
        endDate: '',
        status: 1
      }
      this.$nextTick(() => {
        this.$refs.contractForm && this.$refs.contractForm.clearValidate()
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

    // 获取状态类型
    getStatusType(status) {
      switch (status) {
        case 1: return 'info'
        case 2: return 'warning'
        case 3: return 'success'
        case 4: return 'success'
        case 5: return 'danger'
        default: return 'info'
      }
    }
  }
}
</script>

<style scoped>
.contract-list {
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
</style> 