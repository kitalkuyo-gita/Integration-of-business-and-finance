<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <h2>IOBAF系统</h2>
        <p>业财一体化管理系统</p>
      </div>
      
      <el-form
        ref="loginForm"
        :model="loginForm"
        :rules="loginRules"
        class="login-form"
        @submit.native.prevent
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            prefix-icon="el-icon-user"
            size="large"
            clearable
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="el-icon-lock"
            size="large"
            show-password
            clearable
            @keyup.enter.native="handleLogin"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loginLoading"
            class="login-button"
            @click="handleLogin"
          >
            {{ loginLoading ? '登录中...' : '登录' }}
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="login-footer">
        <p>© 2024 IOBAF系统. All rights reserved.</p>
      </div>
    </div>
  </div>
</template>

<script>
import { login } from '@/api/user'

export default {
  name: 'Login',
  data() {
    return {
      // 登录表单
      loginForm: {
        username: 'admin',
        password: '123456'
      },
      // 登录验证规则
      loginRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ]
      },
      // 登录加载状态
      loginLoading: false
    }
  },
  methods: {
    // 处理登录
    async handleLogin() {
      try {
        // 表单验证
        const valid = await this.$refs.loginForm.validate()
        if (!valid) {
          return
        }

        this.loginLoading = true

        const response = await login(this.loginForm)
        if (response.code === 200) {
          this.$message.success('登录成功')
          
          // 跳转到首页或指定页面
          const redirect = this.$route.query.redirect || '/'
          this.$router.push(redirect)
        } else {
          this.$message.error(response.message || '登录失败')
        }
      } catch (error) {
        console.error('登录失败:', error)
        this.$message.error('登录失败，请检查网络连接')
      } finally {
        this.loginLoading = false
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.login-container {
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  
  .login-box {
    width: 400px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
    padding: 40px;
    
    .login-header {
      text-align: center;
      margin-bottom: 30px;
      
      h2 {
        color: #303133;
        margin: 0 0 10px 0;
        font-size: 24px;
        font-weight: 500;
      }
      
      p {
        color: #909399;
        margin: 0;
        font-size: 14px;
      }
    }
    
    .login-form {
      .login-button {
        width: 100%;
        height: 44px;
        font-size: 16px;
      }
    }
    
    .login-footer {
      text-align: center;
      margin-top: 20px;
      
      p {
        color: #909399;
        font-size: 12px;
        margin: 0;
      }
    }
  }
}
</style> 