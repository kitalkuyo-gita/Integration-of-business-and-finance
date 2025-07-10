<template>
  <div class="app-wrapper">
    <!-- 侧边栏 -->
    <div class="sidebar-container">
      <div class="logo">
        <h2>IOBAF系统</h2>
      </div>
      <el-menu
        :default-active="$route.path"
        class="sidebar-menu"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        router
      >
        <el-menu-item index="/dashboard">
          <i class="el-icon-menu"></i>
          <span slot="title">仪表板</span>
        </el-menu-item>
        <el-menu-item index="/user/list">
          <i class="el-icon-user"></i>
          <span slot="title">用户管理</span>
        </el-menu-item>
        <el-menu-item index="/customer/list">
          <i class="el-icon-user-solid"></i>
          <span slot="title">客户管理</span>
        </el-menu-item>
        <el-menu-item index="/contract/list">
          <i class="el-icon-document"></i>
          <span slot="title">合同管理</span>
        </el-menu-item>
        <el-menu-item index="/finance/voucher">
          <i class="el-icon-money"></i>
          <span slot="title">财务管理</span>
        </el-menu-item>
      </el-menu>
    </div>

    <!-- 主内容区 -->
    <div class="main-container">
      <!-- 顶部导航 -->
      <div class="navbar">
        <div class="navbar-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ $route.meta.title }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="navbar-right">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <i class="el-icon-user"></i>
              管理员
              <i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="profile">个人信息</el-dropdown-item>
              <el-dropdown-item command="password">修改密码</el-dropdown-item>
              <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>

      <!-- 内容区域 -->
      <div class="app-main">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Layout',
  methods: {
    handleCommand(command) {
      switch (command) {
        case 'profile':
          this.$message.info('个人信息功能开发中...')
          break
        case 'password':
          this.$message.info('修改密码功能开发中...')
          break
        case 'logout':
          this.$confirm('确定要退出登录吗？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.$router.push('/login')
          }).catch(() => {})
          break
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.app-wrapper {
  display: flex;
  height: 100vh;
  
  .sidebar-container {
    width: 200px;
    background-color: #304156;
    
    .logo {
      height: 60px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #fff;
      font-size: 18px;
      font-weight: bold;
      border-bottom: 1px solid #435266;
    }
    
    .sidebar-menu {
      border: none;
    }
  }
  
  .main-container {
    flex: 1;
    display: flex;
    flex-direction: column;
    
    .navbar {
      height: 60px;
      background-color: #fff;
      border-bottom: 1px solid #e6e6e6;
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0 20px;
      
      .navbar-left {
        .el-breadcrumb {
          line-height: 60px;
        }
      }
      
      .navbar-right {
        .user-info {
          cursor: pointer;
          color: #606266;
          
          &:hover {
            color: #409EFF;
          }
        }
      }
    }
    
    .app-main {
      flex: 1;
      background-color: #f0f2f5;
      overflow-y: auto;
    }
  }
}
</style> 