# 书法交流活动管理系统

基于 Spring Boot + Vue2 + Element UI 的书法交流活动管理系统，包含后端、管理端和用户端三个项目。

## 项目结构

```
calligraphy-system/
├── backend/    # Spring Boot 后端 (端口 8080)
├── admin/      # 管理端前端 (端口 8081)
└── front/      # 用户端前端 (端口 8082)
```

## 技术栈

- **后端**: Spring Boot 2.7 + MyBatis-Plus + MySQL 8.0 + JWT
- **前端**: Vue 2 + Element UI + Axios + Vue Router

## 功能模块

### 管理端 (admin)
- 管理员登录（管理员 / 活动管理员 两种角色）
- 用户管理：查看、禁用/启用、删除用户
- 管理员管理：新增、编辑、删除管理员
- 书法作品管理：查看、上下架、删除作品
- 活动管理：新增、编辑、开始/结束、删除活动
- 报名管理：审核通过/拒绝报名
- 视频管理：查看、上下架、删除视频
- 投票管理：创建投票、管理投票选项、开始/结束投票
- 投诉管理：查看投诉、处理/驳回投诉

### 用户端 (front)
- 用户注册、登录
- 首页：展示最新作品、活动、视频
- 书法作品：浏览、搜索、分类筛选、查看详情
- 交流活动：浏览、搜索、查看详情、报名参加
- 书法视频：浏览、搜索、查看详情
- 作品投票：浏览投票活动、投票
- 个人中心：修改个人信息、修改密码
- 我的作品：发布、编辑、删除自己的作品
- 我的报名：查看报名记录、取消报名
- 我的投诉：提交投诉、查看投诉进度
- 详情页功能：点赞、踩、评论、投诉

## 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Node.js 14+
- npm 6+

## 快速开始

### 1. 数据库初始化

```bash
mysql -u root -p < backend/src/main/resources/calligraphy.sql
```

### 2. 启动后端

```bash
cd backend
# 修改 src/main/resources/application.yml 中的数据库配置
mvn spring-boot:run
```

后端运行在 http://localhost:8080

### 3. 启动管理端

```bash
cd admin
npm install
npm run serve
```

管理端运行在 http://localhost:8081

### 4. 启动用户端

```bash
cd front
npm install
npm run serve
```

用户端运行在 http://localhost:8082

## 测试账号

### 管理端
| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | 123456 |
| 活动管理员 | activity_admin | 123456 |

### 用户端
| 用户名 | 密码 |
|--------|------|
| user1 | 123456 |
| user2 | 123456 |

## 接口说明

所有接口以 `/api` 开头，使用 JWT Token 认证（Header: `token`）。

公开接口（无需登录）：
- `POST /api/user/login` - 用户登录
- `POST /api/user/register` - 用户注册
- `POST /api/admin/login` - 管理员登录
- `GET /api/front/**` - 用户端公开数据
- `POST /api/file/upload` - 文件上传
