<template>
  <div class="promotion-rules">
    <div class="page-header">
      <h2>促销费用分摊规则</h2>
      <el-button type="primary" @click="handleAdd">新增规则</el-button>
    </div>

    <!-- 搜索条件 -->
    <el-card class="search-card">
      <el-form :model="searchForm" :inline="true" @submit.native.prevent>
        <el-form-item label="规则类型">
          <el-select v-model="searchForm.ruleType" placeholder="请选择规则类型" clearable>
            <el-option label="按销售额" value="SALES_AMOUNT"></el-option>
            <el-option label="按销售量" value="SALES_QUANTITY"></el-option>
            <el-option label="按市场份额" value="MARKET_SHARE"></el-option>
            <el-option label="自定义" value="CUSTOM"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="渠道类型">
          <el-select v-model="searchForm.channelType" placeholder="请选择渠道类型" clearable>
            <el-option label="线上" value="ONLINE"></el-option>
            <el-option label="直播" value="LIVE"></el-option>
            <el-option label="批发" value="WHOLESALE"></el-option>
            <el-option label="线下" value="OFFLINE"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="规则状态">
          <el-select v-model="searchForm.ruleStatus" placeholder="请选择规则状态" clearable>
            <el-option label="生效" value="ACTIVE"></el-option>
            <el-option label="失效" value="INACTIVE"></el-option>
            <el-option label="暂停" value="PAUSED"></el-option>
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
        <el-table-column prop="ruleName" label="规则名称" min-width="120"></el-table-column>
        <el-table-column prop="ruleCode" label="规则编码" min-width="120"></el-table-column>
        <el-table-column prop="ruleType" label="规则类型" min-width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.ruleType === 'SALES_AMOUNT'" type="primary">按销售额</el-tag>
            <el-tag v-else-if="scope.row.ruleType === 'SALES_QUANTITY'" type="success">按销售量</el-tag>
            <el-tag v-else-if="scope.row.ruleType === 'MARKET_SHARE'" type="warning">按市场份额</el-tag>
            <el-tag v-else-if="scope.row.ruleType === 'CUSTOM'" type="info">自定义</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="channelType" label="渠道类型" min-width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.channelType === 'ONLINE'" type="primary">线上</el-tag>
            <el-tag v-else-if="scope.row.channelType === 'LIVE'" type="success">直播</el-tag>
            <el-tag v-else-if="scope.row.channelType === 'WHOLESALE'" type="warning">批发</el-tag>
            <el-tag v-else-if="scope.row.channelType === 'OFFLINE'" type="info">线下</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="allocationRatio" label="分摊比例" min-width="100">
          <template slot-scope="scope">
            {{ scope.row.allocationRatio }}%
          </template>
        </el-table-column>
        <el-table-column prop="ruleStatus" label="规则状态" min-width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.ruleStatus === 'ACTIVE'" type="success">生效</el-tag>
            <el-tag v-else-if="scope.row.ruleStatus === 'INACTIVE'" type="danger">失效</el-tag>
            <el-tag v-else-if="scope.row.ruleStatus === 'PAUSED'" type="warning">暂停</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" min-width="80"></el-table-column>
        <el-table-column prop="effectiveStartTime" label="生效开始时间" min-width="150">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.effectiveStartTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="effectiveEndTime" label="生效结束时间" min-width="150">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.effectiveEndTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" @click="handleDelete(scope.row)">删除</el-button>
            <el-button type="text" @click="handleView(scope.row)">查看</el-button>
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
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" :rules="rules" ref="form" label-width="120px">
        <el-form-item label="规则名称" prop="ruleName">
          <el-input v-model="form.ruleName" placeholder="请输入规则名称"></el-input>
        </el-form-item>
        <el-form-item label="规则编码" prop="ruleCode">
          <el-input v-model="form.ruleCode" placeholder="请输入规则编码"></el-input>
        </el-form-item>
        <el-form-item label="规则类型" prop="ruleType">
          <el-select v-model="form.ruleType" placeholder="请选择规则类型">
            <el-option label="按销售额" value="SALES_AMOUNT"></el-option>
            <el-option label="按销售量" value="SALES_QUANTITY"></el-option>
            <el-option label="按市场份额" value="MARKET_SHARE"></el-option>
            <el-option label="自定义" value="CUSTOM"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="渠道类型" prop="channelType">
          <el-select v-model="form.channelType" placeholder="请选择渠道类型">
            <el-option label="线上" value="ONLINE"></el-option>
            <el-option label="直播" value="LIVE"></el-option>
            <el-option label="批发" value="WHOLESALE"></el-option>
            <el-option label="线下" value="OFFLINE"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="产品类别" prop="productCategory">
          <el-input v-model="form.productCategory" placeholder="请输入产品类别"></el-input>
        </el-form-item>
        <el-form-item label="分摊比例" prop="allocationRatio">
          <el-input-number v-model="form.allocationRatio" :min="0" :max="100" :precision="2" placeholder="请输入分摊比例"></el-input-number>
          <span class="unit">%</span>
        </el-form-item>
        <el-form-item label="分摊基数" prop="allocationBase">
          <el-select v-model="form.allocationBase" placeholder="请选择分摊基数">
            <el-option label="金额" value="AMOUNT"></el-option>
            <el-option label="数量" value="QUANTITY"></el-option>
            <el-option label="自定义" value="CUSTOM"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="自定义公式" prop="customFormula" v-if="form.ruleType === 'CUSTOM'">
          <el-input v-model="form.customFormula" type="textarea" placeholder="请输入自定义分摊公式"></el-input>
        </el-form-item>
        <el-form-item label="生效开始时间" prop="effectiveStartTime">
          <el-date-picker
            v-model="form.effectiveStartTime"
            type="datetime"
            placeholder="选择生效开始时间"
            format="yyyy-MM-dd HH:mm:ss"
            value-format="yyyy-MM-dd HH:mm:ss"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="生效结束时间" prop="effectiveEndTime">
          <el-date-picker
            v-model="form.effectiveEndTime"
            type="datetime"
            placeholder="选择生效结束时间"
            format="yyyy-MM-dd HH:mm:ss"
            value-format="yyyy-MM-dd HH:mm:ss"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="规则状态" prop="ruleStatus">
          <el-select v-model="form.ruleStatus" placeholder="请选择规则状态">
            <el-option label="生效" value="ACTIVE"></el-option>
            <el-option label="失效" value="INACTIVE"></el-option>
            <el-option label="暂停" value="PAUSED"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-input-number v-model="form.priority" :min="1" :max="999" placeholder="请输入优先级"></el-input-number>
        </el-form-item>
        <el-form-item label="规则描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入规则描述"></el-input>
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
import { getPromotionRuleList, createPromotionRule, updatePromotionRule, deletePromotionRule } from '@/api/promotion'

export default {
  name: 'PromotionRules',
  data() {
    return {
      loading: false,
      searchForm: {
        ruleType: '',
        channelType: '',
        ruleStatus: ''
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
        ruleName: '',
        ruleCode: '',
        ruleType: '',
        channelType: '',
        productCategory: '',
        allocationRatio: 0,
        allocationBase: '',
        customFormula: '',
        effectiveStartTime: '',
        effectiveEndTime: '',
        ruleStatus: 'ACTIVE',
        priority: 1,
        description: ''
      },
      rules: {
        ruleName: [
          { required: true, message: '请输入规则名称', trigger: 'blur' }
        ],
        ruleCode: [
          { required: true, message: '请输入规则编码', trigger: 'blur' }
        ],
        ruleType: [
          { required: true, message: '请选择规则类型', trigger: 'change' }
        ],
        channelType: [
          { required: true, message: '请选择渠道类型', trigger: 'change' }
        ],
        allocationRatio: [
          { required: true, message: '请输入分摊比例', trigger: 'blur' }
        ],
        allocationBase: [
          { required: true, message: '请选择分摊基数', trigger: 'change' }
        ],
        effectiveStartTime: [
          { required: true, message: '请选择生效开始时间', trigger: 'change' }
        ],
        effectiveEndTime: [
          { required: true, message: '请选择生效结束时间', trigger: 'change' }
        ],
        ruleStatus: [
          { required: true, message: '请选择规则状态', trigger: 'change' }
        ],
        priority: [
          { required: true, message: '请输入优先级', trigger: 'blur' }
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
        const response = await getPromotionRuleList(params)
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
        ruleType: '',
        channelType: '',
        ruleStatus: ''
      }
      this.handleSearch()
    },

    // 新增
    handleAdd() {
      this.dialogTitle = '新增规则'
      this.form = {
        ruleName: '',
        ruleCode: '',
        ruleType: '',
        channelType: '',
        productCategory: '',
        allocationRatio: 0,
        allocationBase: '',
        customFormula: '',
        effectiveStartTime: '',
        effectiveEndTime: '',
        ruleStatus: 'ACTIVE',
        priority: 1,
        description: ''
      }
      this.dialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑规则'
      this.form = { ...row }
      this.dialogVisible = true
    },

    // 查看
    handleView(row) {
      this.dialogTitle = '查看规则'
      this.form = { ...row }
      this.dialogVisible = true
    },

    // 删除
    handleDelete(row) {
      this.$confirm('确定要删除该规则吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deletePromotionRule(row.id)
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
              response = await updatePromotionRule(this.form.id, this.form)
            } else {
              response = await createPromotionRule(this.form)
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
.promotion-rules {
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