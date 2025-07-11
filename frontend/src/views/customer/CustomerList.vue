<template>
  <div class="customer-list">
    <!-- 搜索区域 -->
    <el-card class="search-card">
      <el-form :model="searchForm" :inline="true" @submit.native.prevent>
        <el-form-item label="客户名称">
          <el-input
            v-model="searchForm.customerName"
            placeholder="请输入客户名称"
            clearable
            @keyup.enter.native="handleSearch"
          />
        </el-form-item>
        <el-form-item label="客户等级">
          <el-select v-model="searchForm.customerLevel" placeholder="请选择客户等级" clearable>
            <el-option label="普通" :value="1" />
            <el-option label="重要" :value="2" />
            <el-option label="VIP" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
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
      <el-button type="primary" @click="handleAdd">新增客户</el-button>
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
        <el-table-column prop="customerName" label="客户名称" min-width="150" />
        <el-table-column prop="customerCode" label="客户编码" width="120" />
        <el-table-column prop="contactPerson" label="联系人" width="100" />
        <el-table-column prop="contactPhone" label="联系电话" width="120" />
        <el-table-column prop="contactEmail" label="联系邮箱" min-width="150" />
        <el-table-column prop="industry" label="所属行业" width="100" />
        <el-table-column prop="customerLevelName" label="客户等级" width="80">
          <template slot-scope="scope">
            <el-tag
              :type="scope.row.customerLevel === 3 ? 'danger' : scope.row.customerLevel === 2 ? 'warning' : 'info'"
            >
              {{ scope.row.customerLevelName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="statusName" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
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

    <!-- 客户表单对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form
        ref="customerForm"
        :model="customerForm"
        :rules="customerRules"
        label-width="100px"
      >
        <el-form-item label="客户名称" prop="customerName">
          <el-input v-model="customerForm.customerName" placeholder="请输入客户名称" />
        </el-form-item>
        <el-form-item label="客户编码" prop="customerCode">
          <el-input v-model="customerForm.customerCode" placeholder="请输入客户编码" />
        </el-form-item>
        <el-form-item label="联系人" prop="contactPerson">
          <el-input v-model="customerForm.contactPerson" placeholder="请输入联系人" />
        </el-form-item>
        <el-form-item label="联系电话" prop="contactPhone">
          <el-input v-model="customerForm.contactPhone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="联系邮箱" prop="contactEmail">
          <el-input v-model="customerForm.contactEmail" placeholder="请输入联系邮箱" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="customerForm.address" type="textarea" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="所属行业" prop="industry">
          <el-input v-model="customerForm.industry" placeholder="请输入所属行业" />
        </el-form-item>
        <el-form-item label="客户等级" prop="customerLevel">
          <el-select v-model="customerForm.customerLevel" placeholder="请选择客户等级">
            <el-option label="普通" :value="1" />
            <el-option label="重要" :value="2" />
            <el-option label="VIP" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="customerForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
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
import { getCustomerList, createCustomer, updateCustomer, deleteCustomer } from '@/api/customer'

export default {
  name: 'CustomerList',
  data() {
    return {
      loading: false,
      searchForm: {
        customerName: '',
        customerLevel: null,
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
      customerForm: {
        customerName: '',
        customerCode: '',
        contactPerson: '',
        contactPhone: '',
        contactEmail: '',
        address: '',
        industry: '',
        customerLevel: 1,
        status: 1
      },
      customerRules: {
        customerName: [
          { required: true, message: '请输入客户名称', trigger: 'blur' }
        ],
        contactPerson: [
          { required: true, message: '请输入联系人', trigger: 'blur' }
        ],
        contactPhone: [
          { required: true, message: '请输入联系电话', trigger: 'blur' }
        ]
      },
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
        const response = await getCustomerList(params)
        if (response.code === 200) {
          this.tableData = response.data.records
          this.pagination.total = response.data.total
        }
      } catch (error) {
        this.$message.error('获取客户列表失败')
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
        customerName: '',
        customerLevel: null,
        status: null
      }
      this.handleSearch()
    },

    // 新增
    handleAdd() {
      this.isEdit = false
      this.dialogTitle = '新增客户'
      this.dialogVisible = true
      this.resetForm()
    },

    // 编辑
    handleEdit(row) {
      this.isEdit = true
      this.currentId = row.id
      this.dialogTitle = '编辑客户'
      this.dialogVisible = true
      this.customerForm = { ...row }
    },

    // 查看
    handleView(row) {
      this.isEdit = true
      this.dialogTitle = '查看客户'
      this.dialogVisible = true
      this.customerForm = { ...row }
    },

    // 删除
    handleDelete(row) {
      this.$confirm('确定要删除该客户吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteCustomer(row.id)
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
      this.$refs.customerForm.validate(async (valid) => {
        if (valid) {
          try {
            let response
            if (this.isEdit) {
              response = await updateCustomer(this.currentId, this.customerForm)
            } else {
              response = await createCustomer(this.customerForm)
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
      this.customerForm = {
        customerName: '',
        customerCode: '',
        contactPerson: '',
        contactPhone: '',
        contactEmail: '',
        address: '',
        industry: '',
        customerLevel: 1,
        status: 1
      }
      this.$nextTick(() => {
        this.$refs.customerForm && this.$refs.customerForm.clearValidate()
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

    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return ''
      return new Date(dateTime).toLocaleString()
    }
  }
}
</script>

<style scoped>
.customer-list {
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