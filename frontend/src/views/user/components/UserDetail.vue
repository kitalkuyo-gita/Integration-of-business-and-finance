<template>
  <el-dialog
    title="用户详情"
    :visible="visible"
    width="600px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div v-if="user" class="user-detail">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="用户ID">
          {{ user.id }}
        </el-descriptions-item>
        <el-descriptions-item label="用户名">
          {{ user.username }}
        </el-descriptions-item>
        <el-descriptions-item label="真实姓名">
          {{ user.realName }}
        </el-descriptions-item>
        <el-descriptions-item label="邮箱">
          {{ user.email }}
        </el-descriptions-item>
        <el-descriptions-item label="手机号">
          {{ user.phone }}
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="user.status === 1 ? 'success' : 'danger'">
            {{ user.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ formatDate(user.createTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="更新时间">
          {{ formatDate(user.updateTime) }}
        </el-descriptions-item>
      </el-descriptions>

      <!-- 角色信息 -->
      <div class="detail-section">
        <h4>角色信息</h4>
        <el-table
          v-if="user.roles && user.roles.length > 0"
          :data="user.roles"
          border
          size="small"
        >
          <el-table-column prop="roleName" label="角色名称" />
          <el-table-column prop="roleCode" label="角色编码" />
          <el-table-column prop="description" label="角色描述" />
          <el-table-column prop="status" label="状态" width="80">
            <template slot-scope="scope">
              <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'" size="small">
                {{ scope.row.status === 1 ? '启用' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-else description="暂无角色信息" />
      </div>

      <!-- 权限信息 -->
      <div class="detail-section">
        <h4>权限信息</h4>
        <el-table
          v-if="user.permissions && user.permissions.length > 0"
          :data="user.permissions"
          border
          size="small"
        >
          <el-table-column prop="permissionName" label="权限名称" />
          <el-table-column prop="permissionCode" label="权限编码" />
          <el-table-column prop="permissionType" label="权限类型" width="100">
            <template slot-scope="scope">
              <el-tag :type="getPermissionTypeTag(scope.row.permissionType)" size="small">
                {{ getPermissionTypeText(scope.row.permissionType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="permissionPath" label="权限路径" />
          <el-table-column prop="status" label="状态" width="80">
            <template slot-scope="scope">
              <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'" size="small">
                {{ scope.row.status === 1 ? '启用' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-else description="暂无权限信息" />
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'UserDetail',
  props: {
    // 对话框显示状态
    visible: {
      type: Boolean,
      default: false
    },
    // 用户数据
    user: {
      type: Object,
      default: null
    }
  },
  methods: {
    // 关闭对话框
    handleClose() {
      this.$emit('close')
    },

    // 格式化日期
    formatDate(date) {
      if (!date) return '暂无'
      return new Date(date).toLocaleString()
    },

    // 获取权限类型标签样式
    getPermissionTypeTag(type) {
      const typeMap = {
        1: 'primary',   // 菜单
        2: 'success',    // 按钮
        3: 'warning'     // 接口
      }
      return typeMap[type] || 'info'
    },

    // 获取权限类型文本
    getPermissionTypeText(type) {
      const typeMap = {
        1: '菜单',
        2: '按钮',
        3: '接口'
      }
      return typeMap[type] || '未知'
    }
  }
}
</script>

<style lang="scss" scoped>
.user-detail {
  .detail-section {
    margin-top: 20px;
    
    h4 {
      margin: 0 0 10px 0;
      color: #303133;
      font-size: 14px;
      font-weight: 500;
    }
  }
}

.dialog-footer {
  text-align: right;
}
</style> 