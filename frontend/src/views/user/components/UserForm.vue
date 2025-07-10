<template>
  <el-dialog
    :title="dialogTitle"
    :visible="visible"
    :width="dialogWidth"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <el-form
      ref="userForm"
      :model="form"
      :rules="rules"
      :label-width="labelWidth"
      @submit.native.prevent
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="用户名" prop="username">
            <el-input
              v-model="form.username"
              placeholder="请输入用户名"
              :disabled="mode === 'edit'"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="真实姓名" prop="realName">
            <el-input
              v-model="form.realName"
              placeholder="请输入真实姓名"
              clearable
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="邮箱" prop="email">
            <el-input
              v-model="form.email"
              placeholder="请输入邮箱"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="手机号" prop="phone">
            <el-input
              v-model="form.phone"
              placeholder="请输入手机号"
              clearable
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="密码" prop="password" v-if="mode === 'add'">
            <el-input
              v-model="form.password"
              type="password"
              placeholder="请输入密码"
              show-password
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="确认密码" prop="confirmPassword" v-if="mode === 'add'">
            <el-input
              v-model="form.confirmPassword"
              type="password"
              placeholder="请再次输入密码"
              show-password
              clearable
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="form.status">
              <el-radio :label="1">启用</el-radio>
              <el-radio :label="0">禁用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleCancel">取消</el-button>
      <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
        {{ submitText }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { createUser, updateUser } from '@/api/user'

export default {
  name: 'UserForm',
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
    },
    // 表单模式：add-新增，edit-编辑
    mode: {
      type: String,
      default: 'add'
    },
    // 对话框宽度
    dialogWidth: {
      type: String,
      default: '600px'
    },
    // 标签宽度
    labelWidth: {
      type: String,
      default: '100px'
    }
  },
  data() {
    // 密码确认验证
    const validateConfirmPassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.form.password) {
        callback(new Error('两次输入密码不一致'))
      } else {
        callback()
      }
    }

    return {
      // 表单数据
      form: {
        username: '',
        realName: '',
        email: '',
        phone: '',
        password: '',
        confirmPassword: '',
        status: 1
      },
      // 表单验证规则
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' },
          { pattern: /^[a-zA-Z0-9_]+$/, message: '用户名只能包含字母、数字和下划线', trigger: 'blur' }
        ],
        realName: [
          { required: true, message: '请输入真实姓名', trigger: 'blur' },
          { min: 2, max: 20, message: '真实姓名长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, validator: validateConfirmPassword, trigger: 'blur' }
        ],
        status: [
          { required: true, message: '请选择状态', trigger: 'change' }
        ]
      },
      // 提交加载状态
      submitLoading: false
    }
  },
  computed: {
    // 对话框标题
    dialogTitle() {
      return this.mode === 'add' ? '新增用户' : '编辑用户'
    },
    // 提交按钮文本
    submitText() {
      return this.submitLoading ? '提交中...' : (this.mode === 'add' ? '新增' : '更新')
    }
  },
  watch: {
    // 监听对话框显示状态
    visible(val) {
      if (val) {
        this.initForm()
      }
    },
    // 监听用户数据变化
    user: {
      handler(val) {
        if (val && this.visible) {
          this.initForm()
        }
      },
      deep: true
    }
  },
  methods: {
    // 初始化表单
    initForm() {
      this.$nextTick(() => {
        this.$refs.userForm.clearValidate()
        
        if (this.user) {
          // 编辑模式，填充表单数据
          this.form = {
            id: this.user.id,
            username: this.user.username,
            realName: this.user.realName,
            email: this.user.email,
            phone: this.user.phone,
            password: '',
            confirmPassword: '',
            status: this.user.status
          }
        } else {
          // 新增模式，重置表单
          this.form = {
            username: '',
            realName: '',
            email: '',
            phone: '',
            password: '',
            confirmPassword: '',
            status: 1
          }
        }
      })
    },

    // 提交表单
    async handleSubmit() {
      try {
        // 表单验证
        const valid = await this.$refs.userForm.validate()
        if (!valid) {
          return
        }

        this.submitLoading = true

        // 构建提交数据
        const submitData = {
          username: this.form.username,
          realName: this.form.realName,
          email: this.form.email,
          phone: this.form.phone,
          status: this.form.status
        }

        // 新增模式需要密码
        if (this.mode === 'add') {
          submitData.password = this.form.password
        }

        let response
        if (this.mode === 'add') {
          // 新增用户
          response = await createUser(submitData)
        } else {
          // 更新用户
          response = await updateUser(this.form.id, submitData)
        }

        if (response.code === 200) {
          this.$message.success(this.mode === 'add' ? '新增成功' : '更新成功')
          this.$emit('success')
        } else {
          this.$message.error(response.message || (this.mode === 'add' ? '新增失败' : '更新失败'))
        }
      } catch (error) {
        console.error('提交用户表单失败:', error)
        this.$message.error(this.mode === 'add' ? '新增失败' : '更新失败')
      } finally {
        this.submitLoading = false
      }
    },

    // 取消
    handleCancel() {
      this.$emit('cancel')
    },

    // 关闭对话框
    handleClose() {
      this.$emit('cancel')
    }
  }
}
</script>

<style lang="scss" scoped>
.dialog-footer {
  text-align: right;
}
</style> 