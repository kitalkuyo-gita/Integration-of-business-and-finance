<template>
  <div class="quotation-requests">
    <div class="page-header">
      <h2>定制化报价请求</h2>
      <el-button type="primary" @click="handleAdd">新增请求</el-button>
    </div>

    <!-- 搜索条件 -->
    <el-card class="search-card">
      <el-form :model="searchForm" :inline="true" @submit.native.prevent>
        <el-form-item label="客户名称">
          <el-input v-model="searchForm.customerName" placeholder="请输入客户名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="产品编码">
          <el-input v-model="searchForm.productCode" placeholder="请输入产品编码" clearable></el-input>
        </el-form-item>
        <el-form-item label="报价状态">
          <el-select v-model="searchForm.quotationStatus" placeholder="请选择报价状态" clearable>
            <el-option label="待报价" value="PENDING"></el-option>
            <el-option label="已报价" value="QUOTED"></el-option>
            <el-option label="已确认" value="CONFIRMED"></el-option>
            <el-option label="已拒绝" value="REJECTED"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card">
      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column prop="requestNo" label="请求编号" min-width="120"></el-table-column>
        <el-table-column prop="customerName" label="客户名称" min-width="120"></el-table-column>
        <el-table-column prop="productCode" label="产品编码" min-width="120"></el-table-column>
        <el-table-column prop="productName" label="产品名称" min-width="120"></el-table-column>
        <el-table-column prop="quantity" label="数量" min-width="80"></el-table-column>
        <el-table-column prop="materialCost" label="材料成本" min-width="100">
          <template slot-scope="scope">
            ¥{{ scope.row.materialCost }}
          </template>
        </el-table-column>
        <el-table-column prop="processComplexity" label="工艺复杂度" min-width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.processComplexity === 'SIMPLE'" type="success">简单</el-tag>
            <el-tag v-else-if="scope.row.processComplexity === 'MEDIUM'" type="warning">中等</el-tag>
            <el-tag v-else-if="scope.row.processComplexity === 'COMPLEX'" type="danger">复杂</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="quotationStatus" label="报价状态" min-width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.quotationStatus === 'PENDING'" type="info">待报价</el-tag>
            <el-tag v-else-if="scope.row.quotationStatus === 'QUOTED'" type="primary">已报价</el-tag>
            <el-tag v-else-if="scope.row.quotationStatus === 'CONFIRMED'" type="success">已确认</el-tag>
            <el-tag v-else-if="scope.row.quotationStatus === 'REJECTED'" type="danger">已拒绝</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="quotedPrice" label="报价金额" min-width="100">
          <template slot-scope="scope">
            <span v-if="scope.row.quotedPrice">¥{{ scope.row.quotedPrice }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="requestTime" label="请求时间" min-width="150">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.requestTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="quotedTime" label="报价时间" min-width="150">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.quotedTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="requesterName" label="请求人" min-width="100"></el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" @click="handleDelete(scope.row)">删除</el-button>
            <el-button type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" @click="handleCalculate(scope.row)" v-if="scope.row.quotationStatus === 'PENDING'">计算报价</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.current"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
        class="pagination"
      ></el-pagination>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="700px">
      <el-form :model="form" :rules="rules" ref="form" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="客户名称" prop="customerName">
              <el-input v-model="form.customerName" placeholder="请输入客户名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="产品编码" prop="productCode">
              <el-input v-model="form.productCode" placeholder="请输入产品编码"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="产品名称" prop="productName">
              <el-input v-model="form.productName" placeholder="请输入产品名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数量" prop="quantity">
              <el-input-number v-model="form.quantity" :min="1" placeholder="请输入数量"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="材料成本" prop="materialCost">
              <el-input-number v-model="form.materialCost" :min="0" :precision="2" placeholder="请输入材料成本"></el-input-number>
              <span class="unit">元</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="工艺复杂度" prop="processComplexity">
              <el-select v-model="form.processComplexity" placeholder="请选择工艺复杂度">
                <el-option label="简单" value="SIMPLE"></el-option>
                <el-option label="中等" value="MEDIUM"></el-option>
                <el-option label="复杂" value="COMPLEX"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="技术要求" prop="technicalRequirements">
              <el-input v-model="form.technicalRequirements" type="textarea" placeholder="请输入技术要求"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="交货期" prop="deliveryPeriod">
              <el-input-number v-model="form.deliveryPeriod" :min="1" placeholder="请输入交货期"></el-input-number>
              <span class="unit">天</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="特殊要求" prop="specialRequirements">
          <el-input v-model="form.specialRequirements" type="textarea" placeholder="请输入特殊要求"></el-input>
        </el-form-item>
        <el-form-item label="备注信息" prop="remarks">
          <el-input v-model="form.remarks" type="textarea" placeholder="请输入备注信息"></el-input>
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
import { getQuotationRequestList, createQuotationRequest, updateQuotationRequest, deleteQuotationRequest, calculateQuotation } from '@/api/quotation'

export default {
  name: 'QuotationRequests',
  data() {
    return {
      loading: false,
      searchForm: {
        customerName: '',
        productCode: '',
        quotationStatus: ''
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '',
      form: {
        customerName: '',
        productCode: '',
        productName: '',
        quantity: 1,
        materialCost: 0,
        processComplexity: 'MEDIUM',
        technicalRequirements: '',
        deliveryPeriod: 30,
        specialRequirements: '',
        remarks: ''
      },
      rules: {
        customerName: [
          { required: true, message: '请输入客户名称', trigger: 'blur' }
        ],
        productCode: [
          { required: true, message: '请输入产品编码', trigger: 'blur' }
        ],
        productName: [
          { required: true, message: '请输入产品名称', trigger: 'blur' }
        ],
        quantity: [
          { required: true, message: '请输入数量', trigger: 'blur' }
        ],
        materialCost: [
          { required: true, message: '请输入材料成本', trigger: 'blur' }
        ],
        processComplexity: [
          { required: true, message: '请选择工艺复杂度', trigger: 'change' }
        ]
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
          ...this.searchForm
        }
        const response = await getQuotationRequestList(params)
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
        customerName: '',
        productCode: '',
        quotationStatus: ''
      }
      this.handleSearch()
    },

    // 新增
    handleAdd() {
      this.dialogTitle = '新增报价请求'
      this.form = {
        customerName: '',
        productCode: '',
        productName: '',
        quantity: 1,
        materialCost: 0,
        processComplexity: 'MEDIUM',
        technicalRequirements: '',
        deliveryPeriod: 30,
        specialRequirements: '',
        remarks: ''
      }
      this.dialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑报价请求'
      this.form = { ...row }
      this.dialogVisible = true
    },

    // 查看
    handleView(row) {
      this.dialogTitle = '查看报价请求'
      this.form = { ...row }
      this.dialogVisible = true
    },

    // 计算报价
    async handleCalculate(row) {
      try {
        const response = await calculateQuotation(row.id)
        if (response.code === 200) {
          this.$message.success('报价计算成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '报价计算失败')
        }
      } catch (error) {
        console.error('报价计算失败:', error)
        this.$message.error('报价计算失败')
      }
    },

    // 删除
    handleDelete(row) {
      this.$confirm('确定要删除该报价请求吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteQuotationRequest(row.id)
          if (response.code === 200) {
            this.$message.success('删除成功')
            this.loadData()
          } else {
            this.$message.error(response.message || '删除失败')
          }
        } catch (error) {
          console.error('删除失败:', error)
          this.$message.error('删除失败')
        }
      })
    },

    // 提交表单
    async handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          try {
            let response
            if (this.form.id) {
              response = await updateQuotationRequest(this.form.id, this.form)
            } else {
              response = await createQuotationRequest(this.form)
            }
            
            if (response.code === 200) {
              this.$message.success(this.form.id ? '更新成功' : '创建成功')
              this.dialogVisible = false
              this.loadData()
            } else {
              this.$message.error(response.message || '操作失败')
            }
          } catch (error) {
            console.error('操作失败:', error)
            this.$message.error('操作失败')
          }
        }
      })
    },

    // 分页大小改变
    handleSizeChange(size) {
      this.pagination.size = size
      this.loadData()
    },

    // 当前页改变
    handleCurrentChange(current) {
      this.pagination.current = current
      this.loadData()
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
.quotation-requests {
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

.search-card {
  margin-bottom: 20px;
}

.table-card {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

.unit {
  margin-left: 10px;
  color: #606266;
}

.dialog-footer {
  text-align: right;
}
</style> 