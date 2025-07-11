<template>
  <div class="quality-traces">
    <div class="page-header">
      <h2>质量追溯记录</h2>
      <el-button type="primary" @click="handleAdd">新增记录</el-button>
    </div>

    <!-- 搜索条件 -->
    <el-card class="search-card">
      <el-form :model="searchForm" :inline="true" @submit.native.prevent>
        <el-form-item label="追溯码">
          <el-input v-model="searchForm.traceCode" placeholder="请输入追溯码" clearable></el-input>
        </el-form-item>
        <el-form-item label="产品编码">
          <el-input v-model="searchForm.productCode" placeholder="请输入产品编码" clearable></el-input>
        </el-form-item>
        <el-form-item label="批次号">
          <el-input v-model="searchForm.batchNo" placeholder="请输入批次号" clearable></el-input>
        </el-form-item>
        <el-form-item label="检测结果">
          <el-select v-model="searchForm.inspectionResult" placeholder="请选择检测结果" clearable>
            <el-option label="合格" value="PASS"></el-option>
            <el-option label="不合格" value="FAIL"></el-option>
            <el-option label="警告" value="WARNING"></el-option>
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
        <el-table-column prop="traceCode" label="追溯码" min-width="150"></el-table-column>
        <el-table-column prop="productCode" label="产品编码" min-width="120"></el-table-column>
        <el-table-column prop="productName" label="产品名称" min-width="120"></el-table-column>
        <el-table-column prop="batchNo" label="批次号" min-width="120"></el-table-column>
        <el-table-column prop="workOrderNo" label="工单号" min-width="120"></el-table-column>
        <el-table-column prop="processCode" label="工序编码" min-width="120"></el-table-column>
        <el-table-column prop="processName" label="工序名称" min-width="120"></el-table-column>
        <el-table-column prop="equipmentCode" label="设备编码" min-width="120"></el-table-column>
        <el-table-column prop="equipmentName" label="设备名称" min-width="120"></el-table-column>
        <el-table-column prop="inspectionItem" label="检测项目" min-width="120"></el-table-column>
        <el-table-column prop="inspectionValue" label="检测值" min-width="100">
          <template slot-scope="scope">
            {{ scope.row.inspectionValue }} {{ scope.row.inspectionUnit }}
          </template>
        </el-table-column>
        <el-table-column prop="inspectionResult" label="检测结果" min-width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.inspectionResult === 'PASS'" type="success">合格</el-tag>
            <el-tag v-else-if="scope.row.inspectionResult === 'FAIL'" type="danger">不合格</el-tag>
            <el-tag v-else-if="scope.row.inspectionResult === 'WARNING'" type="warning">警告</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="defectType" label="缺陷类型" min-width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.defectType === 'DIMENSION'" type="danger">尺寸</el-tag>
            <el-tag v-else-if="scope.row.defectType === 'SURFACE'" type="warning">表面</el-tag>
            <el-tag v-else-if="scope.row.defectType === 'MATERIAL'" type="info">材料</el-tag>
            <el-tag v-else-if="scope.row.defectType === 'ASSEMBLY'" type="primary">装配</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="defectLevel" label="缺陷等级" min-width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.defectLevel === 'MINOR'" type="info">轻微</el-tag>
            <el-tag v-else-if="scope.row.defectLevel === 'MAJOR'" type="warning">严重</el-tag>
            <el-tag v-else-if="scope.row.defectLevel === 'CRITICAL'" type="danger">致命</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="treatmentMethod" label="处理方式" min-width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.treatmentMethod === 'REPAIR'" type="warning">返修</el-tag>
            <el-tag v-else-if="scope.row.treatmentMethod === 'SCRAP'" type="danger">报废</el-tag>
            <el-tag v-else-if="scope.row.treatmentMethod === 'REWORK'" type="info">重做</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="inspectionTime" label="检测时间" min-width="150">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.inspectionTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="operatorName" label="操作员" min-width="100"></el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" @click="handleDelete(scope.row)">删除</el-button>
            <el-button type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" @click="handleLifecycle(scope.row)">生命周期</el-button>
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
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="800px">
      <el-form :model="form" :rules="rules" ref="form" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="追溯码" prop="traceCode">
              <el-input v-model="form.traceCode" placeholder="请输入追溯码"></el-input>
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
            <el-form-item label="批次号" prop="batchNo">
              <el-input v-model="form.batchNo" placeholder="请输入批次号"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="工单号" prop="workOrderNo">
              <el-input v-model="form.workOrderNo" placeholder="请输入工单号"></el-input>
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
            <el-form-item label="操作员" prop="operatorName">
              <el-input v-model="form.operatorName" placeholder="请输入操作员姓名"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="检测项目" prop="inspectionItem">
              <el-input v-model="form.inspectionItem" placeholder="请输入检测项目"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检测标准" prop="inspectionStandard">
              <el-input v-model="form.inspectionStandard" placeholder="请输入检测标准"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="检测值" prop="inspectionValue">
              <el-input-number v-model="form.inspectionValue" :precision="2" placeholder="请输入检测值"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检测单位" prop="inspectionUnit">
              <el-input v-model="form.inspectionUnit" placeholder="请输入检测单位"></el-input>
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
            <el-form-item label="检测结果" prop="inspectionResult">
              <el-select v-model="form.inspectionResult" placeholder="请选择检测结果">
                <el-option label="合格" value="PASS"></el-option>
                <el-option label="不合格" value="FAIL"></el-option>
                <el-option label="警告" value="WARNING"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="缺陷类型" prop="defectType" v-if="form.inspectionResult === 'FAIL'">
              <el-select v-model="form.defectType" placeholder="请选择缺陷类型">
                <el-option label="尺寸" value="DIMENSION"></el-option>
                <el-option label="表面" value="SURFACE"></el-option>
                <el-option label="材料" value="MATERIAL"></el-option>
                <el-option label="装配" value="ASSEMBLY"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="form.inspectionResult === 'FAIL'">
          <el-col :span="12">
            <el-form-item label="缺陷等级" prop="defectLevel">
              <el-select v-model="form.defectLevel" placeholder="请选择缺陷等级">
                <el-option label="轻微" value="MINOR"></el-option>
                <el-option label="严重" value="MAJOR"></el-option>
                <el-option label="致命" value="CRITICAL"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="处理方式" prop="treatmentMethod">
              <el-select v-model="form.treatmentMethod" placeholder="请选择处理方式">
                <el-option label="返修" value="REPAIR"></el-option>
                <el-option label="报废" value="SCRAP"></el-option>
                <el-option label="重做" value="REWORK"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="缺陷描述" prop="defectDescription" v-if="form.inspectionResult === 'FAIL'">
          <el-input v-model="form.defectDescription" type="textarea" placeholder="请输入缺陷描述"></el-input>
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
import { getQualityTraceList, createQualityTrace, updateQualityTrace, deleteQualityTrace } from '@/api/quality'

export default {
  name: 'QualityTraces',
  data() {
    return {
      loading: false,
      searchForm: {
        traceCode: '',
        productCode: '',
        batchNo: '',
        inspectionResult: ''
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
        traceCode: '',
        productCode: '',
        productName: '',
        batchNo: '',
        workOrderNo: '',
        processCode: '',
        processName: '',
        equipmentCode: '',
        equipmentName: '',
        operatorName: '',
        inspectionItem: '',
        inspectionStandard: '',
        inspectionValue: 0,
        inspectionUnit: '',
        upperLimit: 0,
        lowerLimit: 0,
        inspectionResult: 'PASS',
        defectType: '',
        defectLevel: '',
        defectDescription: '',
        treatmentMethod: '',
        remarks: ''
      },
      rules: {
        traceCode: [
          { required: true, message: '请输入追溯码', trigger: 'blur' }
        ],
        productCode: [
          { required: true, message: '请输入产品编码', trigger: 'blur' }
        ],
        inspectionItem: [
          { required: true, message: '请输入检测项目', trigger: 'blur' }
        ],
        inspectionValue: [
          { required: true, message: '请输入检测值', trigger: 'blur' }
        ],
        inspectionUnit: [
          { required: true, message: '请输入检测单位', trigger: 'blur' }
        ],
        inspectionResult: [
          { required: true, message: '请选择检测结果', trigger: 'change' }
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
        const response = await getQualityTraceList(params)
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
        traceCode: '',
        productCode: '',
        batchNo: '',
        inspectionResult: ''
      }
      this.handleSearch()
    },

    // 新增
    handleAdd() {
      this.dialogTitle = '新增质量追溯记录'
      this.form = {
        traceCode: '',
        productCode: '',
        productName: '',
        batchNo: '',
        workOrderNo: '',
        processCode: '',
        processName: '',
        equipmentCode: '',
        equipmentName: '',
        operatorName: '',
        inspectionItem: '',
        inspectionStandard: '',
        inspectionValue: 0,
        inspectionUnit: '',
        upperLimit: 0,
        lowerLimit: 0,
        inspectionResult: 'PASS',
        defectType: '',
        defectLevel: '',
        defectDescription: '',
        treatmentMethod: '',
        remarks: ''
      }
      this.dialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑质量追溯记录'
      this.form = { ...row }
      this.dialogVisible = true
    },

    // 查看
    handleView(row) {
      this.dialogTitle = '查看质量追溯记录'
      this.form = { ...row }
      this.dialogVisible = true
    },

    // 查看生命周期
    handleLifecycle(row) {
      this.$router.push(`/quality/lifecycle/${row.traceCode}`)
    },

    // 删除
    handleDelete(row) {
      this.$confirm('确定要删除该质量追溯记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteQualityTrace(row.id)
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
              response = await updateQualityTrace(this.form.id, this.form)
            } else {
              response = await createQualityTrace(this.form)
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
.quality-traces {
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