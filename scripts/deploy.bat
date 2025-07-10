@echo off
REM IOBAF系统部署脚本 (Windows)
REM 作者: IOBAF Team
REM 日期: 2024-01-01

echo 开始部署IOBAF系统...

REM 检查Docker是否安装
docker --version >nul 2>&1
if errorlevel 1 (
    echo 错误: Docker未安装，请先安装Docker Desktop
    pause
    exit /b 1
)

REM 检查Docker Compose是否安装
docker-compose --version >nul 2>&1
if errorlevel 1 (
    echo 错误: Docker Compose未安装，请先安装Docker Compose
    pause
    exit /b 1
)

REM 停止现有容器
echo 停止现有容器...
docker-compose -f docker/docker-compose.yml down

REM 构建镜像
echo 构建Docker镜像...
docker-compose -f docker/docker-compose.yml build

REM 启动服务
echo 启动服务...
docker-compose -f docker/docker-compose.yml up -d

REM 等待服务启动
echo 等待服务启动...
timeout /t 30 /nobreak >nul

REM 检查服务状态
echo 检查服务状态...
docker-compose -f docker/docker-compose.yml ps

echo.
echo IOBAF系统部署完成！
echo 前端访问地址: http://localhost
echo 后端API地址: http://localhost:8080/api
echo RabbitMQ管理界面: http://localhost:15672
echo 用户名: admin, 密码: admin123
echo.
pause 