<template>
  <div class="user-list">
    <!-- 搜索表单 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" :inline="true" @submit.native.prevent>
        <el-form-item label="用户名">
          <el-input
            v-model="searchForm.username"
            placeholder="请输入用户名"
            clearable
            @keyup.enter.native="handleSearch"
          />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input
            v-model="searchForm.realName"
            placeholder="请输入真实姓名"
            clearable
            @keyup.enter.native="handleSearch"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">
            搜索
          </el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 用户表格 -->
    <el-card class="table-card" shadow="never">
      <BaseTable
        ref="userTable"
        :data="userList"
        :loading="loading"
        :pagination="pagination"
        :show-selection="true"
        @add="handleAdd"
        @edit="handleEdit"
        @delete="handleDelete"
        @batch-delete="handleBatchDelete"
        @refresh="loadUserList"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        @selection-change="handleSelectionChange"
      >
        <!-- 用户名列 -->
        <el-table-column
          prop="username"
          label="用户名"
          min-width="120"
          show-overflow-tooltip
        />

        <!-- 真实姓名列 -->
        <el-table-column
          prop="realName"
          label="真实姓名"
          min-width="120"
          show-overflow-tooltip
        />

        <!-- 邮箱列 -->
        <el-table-column
          prop="email"
          label="邮箱"
          min-width="180"
          show-overflow-tooltip
        />

        <!-- 手机号列 -->
        <el-table-column
          prop="phone"
          label="手机号"
          min-width="120"
          show-overflow-tooltip
        />

        <!-- 状态列 -->
        <el-table-column
          prop="status"
          label="状态"
          width="80"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>

        <!-- 创建时间列 -->
        <el-table-column
          prop="createTime"
          label="创建时间"
          width="160"
          align="center"
        >
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>

        <!-- 更新时间列 -->
        <el-table-column
          prop="updateTime"
          label="更新时间"
          width="160"
          align="center"
        >
          <template slot-scope="scope">
            {{ formatDate(scope.row.updateTime) }}
          </template>
        </el-table-column>

        <!-- 自定义操作列 -->
        <template #action="{ row, $index }">
          <el-button
            type="text"
            size="small"
            @click="handleView(row, $index)"
          >
            查看
          </el-button>
          <el-button
            type="text"
            size="small"
            @click="handleEdit(row, $index)"
          >
            编辑
          </el-button>
          <el-button
            v-if="row.status === 0"
            type="text"
            size="small"
            @click="handleActivate(row, $index)"
          >
            激活
          </el-button>
          <el-button
            v-else
            type="text"
            size="small"
            @click="handleDeactivate(row, $index)"
          >
            禁用
          </el-button>
          <el-button
            type="text"
            size="small"
            style="color: #F56C6C"
            @click="handleDelete(row, $index)"
          >
            删除
          </el-button>
        </template>
      </BaseTable>
    </el-card>

    <!-- 用户表单对话框 -->
    <UserForm
      ref="userForm"
      :visible="formVisible"
      :user="currentUser"
      :mode="formMode"
      @success="handleFormSuccess"
      @cancel="handleFormCancel"
    />

    <!-- 用户详情对话框 -->
    <UserDetail
      ref="userDetail"
      :visible="detailVisible"
      :user="currentUser"
      @close="handleDetailClose"
    />
  </div>
</template>

<script>
import BaseTable from '@/components/common/BaseTable.vue'
import UserForm from './components/UserForm.vue'
import UserDetail from './components/UserDetail.vue'
import { getUserList, deleteUser, activateUser, deactivateUser, deleteUsers } from '@/api/user'

export default {
  name: 'UserList',
  components: {
    BaseTable,
    UserForm,
    UserDetail
  },
  data() {
    return {
      // 搜索表单
      searchForm: {
        username: '',
        realName: '',
        status: null
      },
      // 用户列表
      userList: [],
      // 加载状态
      loading: false,
      // 分页配置
      pagination: {
        current: 1,
        size: 10,
        total: 0,
        pageSizes: [10, 20, 50, 100],
        layout: 'total, sizes, prev, pager, next, jumper'
      },
      // 选中的用户
      selection: [],
      // 表单对话框显示状态
      formVisible: false,
      // 表单模式：add-新增，edit-编辑
      formMode: 'add',
      // 当前用户
      currentUser: null,
      // 详情对话框显示状态
      detailVisible: false
    }
  },
  created() {
    this.loadUserList()
  },
  methods: {
    // 加载用户列表
    async loadUserList() {
      try {
        this.loading = true
        const params = {
          current: this.pagination.current,
          size: this.pagination.size,
          ...this.searchForm
        }
        
        const response = await getUserList(params)
        if (response.code === 200) {
          this.userList = response.data.records
          this.pagination.total = response.data.total
        } else {
          this.$message.error(response.message || '获取用户列表失败')
        }
      } catch (error) {
        console.error('加载用户列表失败:', error)
        this.$message.error('获取用户列表失败')
      } finally {
        this.loading = false
      }
    },

    // 搜索
    handleSearch() {
      this.pagination.current = 1
      this.loadUserList()
    },

    // 重置搜索
    handleReset() {
      this.searchForm = {
        username: '',
        realName: '',
        status: null
      }
      this.handleSearch()
    },

    // 新增用户
    handleAdd() {
      this.currentUser = null
      this.formMode = 'add'
      this.formVisible = true
    },

    // 编辑用户
    handleEdit(row) {
      this.currentUser = { ...row }
      this.formMode = 'edit'
      this.formVisible = true
    },

    // 查看用户详情
    handleView(row) {
      this.currentUser = { ...row }
      this.detailVisible = true
    },

    // 删除用户
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除该用户吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await deleteUser(row.id)
        if (response.code === 200) {
          this.$message.success('删除成功')
          this.loadUserList()
        } else {
          this.$message.error(response.message || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除用户失败:', error)
          this.$message.error('删除失败')
        }
      }
    },

    // 批量删除用户
    async handleBatchDelete(selection) {
      if (selection.length === 0) {
        this.$message.warning('请选择要删除的用户')
        return
      }

      try {
        await this.$confirm(`确定要删除选中的 ${selection.length} 个用户吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const userIds = selection.map(item => item.id)
        const response = await deleteUsers(userIds)
        if (response.code === 200) {
          this.$message.success('批量删除成功')
          this.loadUserList()
        } else {
          this.$message.error(response.message || '批量删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量删除用户失败:', error)
          this.$message.error('批量删除失败')
        }
      }
    },

    // 激活用户
    async handleActivate(row) {
      try {
        await this.$confirm('确定要激活该用户吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await activateUser(row.id)
        if (response.code === 200) {
          this.$message.success('激活成功')
          this.loadUserList()
        } else {
          this.$message.error(response.message || '激活失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('激活用户失败:', error)
          this.$message.error('激活失败')
        }
      }
    },

    // 禁用用户
    async handleDeactivate(row) {
      try {
        await this.$confirm('确定要禁用该用户吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await deactivateUser(row.id)
        if (response.code === 200) {
          this.$message.success('禁用成功')
          this.loadUserList()
        } else {
          this.$message.error(response.message || '禁用失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('禁用用户失败:', error)
          this.$message.error('禁用失败')
        }
      }
    },

    // 表单成功回调
    handleFormSuccess() {
      this.formVisible = false
      this.loadUserList()
    },

    // 表单取消回调
    handleFormCancel() {
      this.formVisible = false
    },

    // 详情关闭回调
    handleDetailClose() {
      this.detailVisible = false
    },

    // 分页大小变化
    handleSizeChange(size) {
      this.pagination.size = size
      this.pagination.current = 1
      this.loadUserList()
    },

    // 当前页变化
    handleCurrentChange(current) {
      this.pagination.current = current
      this.loadUserList()
    },

    // 选择变化
    handleSelectionChange(selection) {
      this.selection = selection
    },

    // 格式化日期
    formatDate(date) {
      if (!date) return ''
      return new Date(date).toLocaleString()
    }
  }
}
</script>

<style lang="scss" scoped>
.user-list {
  padding: 20px;
  
  .search-card {
    margin-bottom: 20px;
  }
  
  .table-card {
    .el-card__body {
      padding: 0;
    }
  }
}
</style> 