#!/bin/bash

# IOBAF系统部署脚本
# 作者: IOBAF Team
# 日期: 2024-01-01

echo "开始部署IOBAF系统..."

# 检查Docker是否安装
if ! command -v docker &> /dev/null; then
    echo "错误: Docker未安装，请先安装Docker"
    exit 1
fi

# 检查Docker Compose是否安装
if ! command -v docker-compose &> /dev/null; then
    echo "错误: Docker Compose未安装，请先安装Docker Compose"
    exit 1
fi

# 停止现有容器
echo "停止现有容器..."
docker-compose -f docker/docker-compose.yml down

# 构建镜像
echo "构建Docker镜像..."
docker-compose -f docker/docker-compose.yml build

# 启动服务
echo "启动服务..."
docker-compose -f docker/docker-compose.yml up -d

# 等待服务启动
echo "等待服务启动..."
sleep 30

# 检查服务状态
echo "检查服务状态..."
docker-compose -f docker/docker-compose.yml ps

echo "IOBAF系统部署完成！"
echo "前端访问地址: http://localhost"
echo "后端API地址: http://localhost:8080/api"
echo "RabbitMQ管理界面: http://localhost:15672"
echo "用户名: admin, 密码: admin123" 