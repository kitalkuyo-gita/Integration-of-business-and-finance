<template>
  <div class="error-page">
    <div class="error-content">
      <div class="error-code">404</div>
      <div class="error-message">页面不存在</div>
      <div class="error-description">
        抱歉，您访问的页面不存在或已被删除
      </div>
      <div class="error-actions">
        <el-button type="primary" @click="goHome">返回首页</el-button>
        <el-button @click="goBack">返回上页</el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Error404',
  methods: {
    goHome() {
      this.$router.push('/')
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.error-page {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f0f2f5;
  
  .error-content {
    text-align: center;
    
    .error-code {
      font-size: 120px;
      font-weight: bold;
      color: #409EFF;
      line-height: 1;
      margin-bottom: 20px;
    }
    
    .error-message {
      font-size: 24px;
      color: #303133;
      margin-bottom: 10px;
    }
    
    .error-description {
      font-size: 14px;
      color: #909399;
      margin-bottom: 30px;
    }
    
    .error-actions {
      .el-button {
        margin: 0 10px;
      }
    }
  }
}
</style> 