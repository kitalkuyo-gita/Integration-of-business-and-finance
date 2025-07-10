const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,
  // 开发服务器配置
  devServer: {
    port: 8081,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        pathRewrite: {
          '^/api': '/api'
        }
      }
    }
  },
  // 生产环境配置
  publicPath: process.env.NODE_ENV === 'production' ? './' : '/',
  // 输出目录
  outputDir: 'dist',
  // 静态资源目录
  assetsDir: 'static',
  // 是否在保存时使用 `eslint-loader` 进行检查
  lintOnSave: process.env.NODE_ENV !== 'production',
  // 生产环境是否生成 sourceMap 文件
  productionSourceMap: false,
  // CSS 相关选项
  css: {
    loaderOptions: {
      sass: {
        additionalData: `@import "@/styles/variables.scss";`
      }
    }
  }
}) 