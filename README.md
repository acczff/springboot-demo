# Spring Boot Demo 项目

> **标签**: `Spring Boot` `Java 21` `Spring Data JPA` `MySQL` `RBAC` `用户管理` `角色权限` `菜单管理` `登录认证` `BCrypt`  
> **更新日期**: 2026-05-07  
> **版本**: v2.0.0

一个基于 Spring Boot 3.x 的后台管理系统后端，实现了完整的 RBAC（基于角色的访问控制）权限体系。

## 项目功能

- ✅ 用户管理（增删改查、分页、关键词搜索）
- ✅ 账号密码登录认证（BCrypt 加密）
- ✅ Token 拦截器（AuthInterceptor）
- ✅ 角色管理（CRUD）
- ✅ 权限管理（CRUD）
- ✅ 菜单管理（树形结构）
- ✅ 用户角色绑定（@ManyToMany）
- ✅ 角色权限绑定（@ManyToMany）
- ✅ 统一响应格式封装（Result<T>）
- ✅ 分层架构设计（Controller-Service-Repository-Entity）

## 技术栈

- **框架**: Spring Boot 3.x
- **Java版本**: 21
- **构建工具**: Maven
- **数据访问**: Spring Data JPA
- **数据库**: MySQL 9.x
- **密码加密**: jBCrypt
- **连接池**: HikariCP

## 数据库表结构

```
users          — 用户表
roles          — 角色表
permissions    — 权限表
menus          — 菜单表
user_roles     — 用户角色中间表（多对多）
role_permissions — 角色权限中间表（多对多）
```

## 项目结构

```
src/main/java/com/zff/springboot_demo/
├── SpringbootDemoApplication.java
├── Result.java                        # 统一响应封装
├── config/
│   └── WebMvcConfig.java              # 拦截器注册
├── interceptor/
│   └── AuthInterceptor.java           # Token 鉴权拦截器
├── auth/
│   └── controller/AuthController.java # 登录/退出/当前用户
├── user/                              # 用户模块（四层）
├── role/                              # 角色模块（四层）
├── permission/                        # 权限模块（四层）
└── menu/                              # 菜单模块（四层）
```

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.6+
- MySQL 8.0+

### 运行项目

```bash
git clone https://github.com/acczff/springboot-demo.git
cd springboot-demo
mvn spring-boot:run
```

### 主要接口

| 模块 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 认证 | POST | /api/auth/login | 登录 |
| 认证 | GET | /api/auth/me | 当前用户信息 |
| 认证 | POST | /api/auth/logout | 退出 |
| 用户 | GET | /api/users | 用户列表（分页+搜索） |
| 用户 | POST | /api/users | 新增用户 |
| 用户 | PUT | /api/users/{id} | 编辑用户 |
| 用户 | GET | /api/users/{id}/roles | 查询用户角色 |
| 用户 | PUT | /api/users/{id}/roles | 绑定用户角色 |
| 角色 | GET | /api/roles | 角色列表 |
| 角色 | POST | /api/roles | 新增角色 |
| 角色 | GET | /api/roles/{id}/permissions | 查询角色权限 |
| 角色 | PUT | /api/roles/{id}/permissions | 分配角色权限 |
| 权限 | GET | /api/permissions | 权限列表 |
| 权限 | POST | /api/permissions | 新增权限 |
| 菜单 | GET | /api/menus | 菜单树 |

### 响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": { }
}
```

### 测试账号

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | 123456 | ADMIN（全部权限） |

- **响应**: 用户对象或404错误

### 认证接口

#### 登录
- **URL**: `/api/auth/login`
- **方法**: POST
- **描述**: 根据账号密码进行登录校验（当前按用户名查询）
- **请求体**:

```json
{
  "account": "zhangsan",
  "password": "123456"
}
```

- **成功响应示例**:

```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "token-1712059200000",
    "userId": 1,
    "username": "zhangsan",
    "email": "zhangsan@example.com"
  }
}
```

## 开发指南

### 添加新的 API 接口

1. 在 `controller` 包下创建新的控制器类
2. 使用 `@RestController` 注解标记类
3. 使用 `@GetMapping`, `@PostMapping` 等注解定义接口
4. 返回 `Result<T>` 类型的统一响应

### 添加新的实体类

1. 在 `entity` 包下创建实体类
2. 定义属性和对应的 getter/setter 方法
3. 添加必要的注解（如 `@Entity`, `@Table` 等）

## 构建和部署

### 打包应用

```bash
mvn clean package
```

### 运行测试

```bash
mvn test
```

### 创建 Docker 镜像（可选）

```dockerfile
FROM openjdk:21-jdk-slim
COPY target/springboot-demo-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 配置说明

### 应用配置

配置文件位于 `src/main/resources/application.yml`

### 数据库配置

当前项目已配置 MySQL 连接与 JPA 参数，默认示例配置如下：

- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`
- `spring.datasource.hikari.maximum-pool-size`
- `spring.datasource.hikari.minimum-idle`
- `spring.datasource.hikari.connection-timeout`
- `spring.jpa.hibernate.ddl-auto`

请根据本地环境修改数据库地址、账号和密码。

## 今日更新（2026-03-31）

- 引入 `spring-boot-starter-data-jpa` 与 `mysql-connector-j` 依赖。
- 将用户仓储层从内存 `Map` 实现切换为 `JpaRepository`。
- 为 `User` 实体添加 JPA 注解并映射数据库表 `users`。
- 调整用户服务层对 `Optional` 与删除逻辑的处理。
- 将配置文件从 `application.properties` 迁移为 `application.yml`，补充 MySQL 与 JPA 配置。

## 今日更新（2026-04-02）

- 新增认证控制器 `AuthController`，提供 `POST /api/auth/login` 登录接口。
- 新增 `LoginRequest`、`LoginResponse` 和 `UserInfoDTO` 等认证/用户数据传输对象。
- 新增 `PasswordEncoder` 工具类，引入 `jBCrypt` 依赖用于密码加密与校验。
- 在 `UserService` 中新增按用户名查询方法，登录逻辑改为数据库校验。
- 在 `application.yml` 中新增 Hikari 连接池配置，完善数据源连接参数。

## 贡献指南

1. Fork 本项目
2. 创建特性分支
3. 提交更改
4. 推送到分支
5. 创建 Pull Request

## 许可证

本项目采用 MIT 许可证。

## 联系方式

- 项目地址: https://github.com/acczff/springboot-demo
- 作者: acczff

---

**注意**: 这是一个演示项目，适用于学习和参考。在生产环境中使用时，请根据实际需求进行适当的修改和优化。