<template>
  <div class="process-parameters">
    <div class="page-header">
      <h2>工艺参数管理</h2>
      <el-button type="primary" @click="handleAdd">新增参数</el-button>
    </div>

    <!-- 搜索条件 -->
    <el-card class="search-card">
      <el-form :model="searchForm" :inline="true" @submit.native.prevent>
        <el-form-item label="工单号">
          <el-input v-model="searchForm.workOrderNo" placeholder="请输入工单号" clearable></el-input>
        </el-form-item>
        <el-form-item label="产品编码">
          <el-input v-model="searchForm.productCode" placeholder="请输入产品编码" clearable></el-input>
        </el-form-item>
        <el-form-item label="工序编码">
          <el-input v-model="searchForm.processCode" placeholder="请输入工序编码" clearable></el-input>
        </el-form-item>
        <el-form-item label="参数类型">
          <el-select v-model="searchForm.parameterType" placeholder="请选择参数类型" clearable>
            <el-option label="温度" value="TEMPERATURE"></el-option>
            <el-option label="压力" value="PRESSURE"></el-option>
            <el-option label="速度" value="SPEED"></el-option>
            <el-option label="时间" value="TIME"></el-option>
            <el-option label="功率" value="POWER"></el-option>
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
        <el-table-column prop="workOrderNo" label="工单号" min-width="120"></el-table-column>
        <el-table-column prop="productCode" label="产品编码" min-width="120"></el-table-column>
        <el-table-column prop="productName" label="产品名称" min-width="120"></el-table-column>
        <el-table-column prop="processCode" label="工序编码" min-width="120"></el-table-column>
        <el-table-column prop="processName" label="工序名称" min-width="120"></el-table-column>
        <el-table-column prop="equipmentCode" label="设备编码" min-width="120"></el-table-column>
        <el-table-column prop="equipmentName" label="设备名称" min-width="120"></el-table-column>
        <el-table-column prop="parameterType" label="参数类型" min-width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.parameterType === 'TEMPERATURE'" type="danger">温度</el-tag>
            <el-tag v-else-if="scope.row.parameterType === 'PRESSURE'" type="warning">压力</el-tag>
            <el-tag v-else-if="scope.row.parameterType === 'SPEED'" type="success">速度</el-tag>
            <el-tag v-else-if="scope.row.parameterType === 'TIME'" type="info">时间</el-tag>
            <el-tag v-else-if="scope.row.parameterType === 'POWER'" type="primary">功率</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="parameterName" label="参数名称" min-width="120"></el-table-column>
        <el-table-column prop="parameterValue" label="参数值" min-width="100">
          <template slot-scope="scope">
            {{ scope.row.parameterValue }} {{ scope.row.parameterUnit }}
          </template>
        </el-table-column>
        <el-table-column prop="standardValue" label="标准值" min-width="100">
          <template slot-scope="scope">
            {{ scope.row.standardValue }} {{ scope.row.parameterUnit }}
          </template>
        </el-table-column>
        <el-table-column prop="isQualified" label="是否合格" min-width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isQualified === 1" type="success">合格</el-tag>
            <el-tag v-else type="danger">不合格</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="recordTime" label="记录时间" min-width="150">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.recordTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="operatorName" label="操作员" min-width="100"></el-table-column>
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
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="700px">
      <el-form :model="form" :rules="rules" ref="form" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="工单号" prop="workOrderNo">
              <el-input v-model="form.workOrderNo" placeholder="请输入工单号"></el-input>
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
            <el-form-item label="工序编码" prop="processCode">
              <el-input v-model="form.processCode" placeholder="请输入工序编码"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="工序名称" prop="processName">
              <el-input v-model="form.processName" placeholder="请输入工序名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备编码" prop="equipmentCode">
              <el-input v-model="form.equipmentCode" placeholder="请输入设备编码"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设备名称" prop="equipmentName">
              <el-input v-model="form.equipmentName" placeholder="请输入设备名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="参数类型" prop="parameterType">
              <el-select v-model="form.parameterType" placeholder="请选择参数类型">
                <el-option label="温度" value="TEMPERATURE"></el-option>
                <el-option label="压力" value="PRESSURE"></el-option>
                <el-option label="速度" value="SPEED"></el-option>
                <el-option label="时间" value="TIME"></el-option>
                <el-option label="功率" value="POWER"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="参数名称" prop="parameterName">
              <el-input v-model="form.parameterName" placeholder="请输入参数名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="参数值" prop="parameterValue">
              <el-input-number v-model="form.parameterValue" :precision="2" placeholder="请输入参数值"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="参数单位" prop="parameterUnit">
              <el-input v-model="form.parameterUnit" placeholder="请输入参数单位"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="标准值" prop="standardValue">
              <el-input-number v-model="form.standardValue" :precision="2" placeholder="请输入标准值"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="上限值" prop="upperLimit">
              <el-input-number v-model="form.upperLimit" :precision="2" placeholder="请输入上限值"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="下限值" prop="lowerLimit">
              <el-input-number v-model="form.lowerLimit" :precision="2" placeholder="请输入下限值"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="是否合格" prop="isQualified">
              <el-radio-group v-model="form.isQualified">
                <el-radio :label="1">合格</el-radio>
                <el-radio :label="0">不合格</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="操作员" prop="operatorName">
              <el-input v-model="form.operatorName" placeholder="请输入操作员姓名"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
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
import { getProcessParameterList, createProcessParameter, updateProcessParameter, deleteProcessParameter } from '@/api/mes'

export default {
  name: 'ProcessParameters',
  data() {
    return {
      loading: false,
      searchForm: {
        workOrderNo: '',
        productCode: '',
        processCode: '',
        parameterType: ''
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
        workOrderNo: '',
        productCode: '',
        productName: '',
        processCode: '',
        processName: '',
        equipmentCode: '',
        equipmentName: '',
        parameterType: '',
        parameterName: '',
        parameterValue: 0,
        parameterUnit: '',
        standardValue: 0,
        upperLimit: 0,
        lowerLimit: 0,
        isQualified: 1,
        operatorName: '',
        remarks: ''
      },
      rules: {
        workOrderNo: [
          { required: true, message: '请输入工单号', trigger: 'blur' }
        ],
        productCode: [
          { required: true, message: '请输入产品编码', trigger: 'blur' }
        ],
        processCode: [
          { required: true, message: '请输入工序编码', trigger: 'blur' }
        ],
        parameterType: [
          { required: true, message: '请选择参数类型', trigger: 'change' }
        ],
        parameterName: [
          { required: true, message: '请输入参数名称', trigger: 'blur' }
        ],
        parameterValue: [
          { required: true, message: '请输入参数值', trigger: 'blur' }
        ],
        parameterUnit: [
          { required: true, message: '请输入参数单位', trigger: 'blur' }
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
        const response = await getProcessParameterList(params)
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
        workOrderNo: '',
        productCode: '',
        processCode: '',
        parameterType: ''
      }
      this.handleSearch()
    },

    // 新增
    handleAdd() {
      this.dialogTitle = '新增工艺参数'
      this.form = {
        workOrderNo: '',
        productCode: '',
        productName: '',
        processCode: '',
        processName: '',
        equipmentCode: '',
        equipmentName: '',
        parameterType: '',
        parameterName: '',
        parameterValue: 0,
        parameterUnit: '',
        standardValue: 0,
        upperLimit: 0,
        lowerLimit: 0,
        isQualified: 1,
        operatorName: '',
        remarks: ''
      }
      this.dialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑工艺参数'
      this.form = { ...row }
      this.dialogVisible = true
    },

    // 查看
    handleView(row) {
      this.dialogTitle = '查看工艺参数'
      this.form = { ...row }
      this.dialogVisible = true
    },

    // 删除
    handleDelete(row) {
      this.$confirm('确定要删除该工艺参数吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteProcessParameter(row.id)
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
              response = await updateProcessParameter(this.form.id, this.form)
            } else {
              response = await createProcessParameter(this.form)
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
.process-parameters {
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

.dialog-footer {
  text-align: right;
}
</style> 