# Spring Boot Demo 项目

一个基于 Spring Boot 3.5.13 的演示项目，展示了基本的 REST API 开发模式。

## 项目概述

这是一个简单的 Spring Boot 演示项目，包含以下功能：
- 健康检查接口
- 用户实体模型
- 统一响应格式

## 技术栈

- **框架**: Spring Boot 3.5.13
- **Java版本**: 21
- **构建工具**: Maven
- **Web框架**: Spring Web
- **测试框架**: Spring Boot Test

## 项目结构

```
src/main/java/com/zff/springboot_demo/
├── SpringbootDemoApplication.java    # 应用启动类
├── HealthController.java             # 健康检查控制器
├── Result.java                       # 统一响应结果封装
└── user/
    ├── entity/
    │   └── User.java                 # 用户实体类
    └── repository/
        └── UserRepository.java       # 用户数据访问层
```

## 快速开始

### 环境要求

- JDK 21 或更高版本
- Maven 3.6 或更高版本

### 运行项目

1. 克隆项目
```bash
git clone https://github.com/acczff/springboot-demo.git
cd springboot-demo
```

2. 编译项目
```bash
mvn clean compile
```

3. 运行项目
```bash
mvn spring-boot:run
```

或者直接运行主类：
```bash
mvn exec:java -Dexec.mainClass="com.zff.springboot_demo.SpringbootDemoApplication"
```

### 测试接口

项目启动后，访问以下接口：

- **健康检查**: `GET http://localhost:8080/api/health`

响应示例：
```json
{
  "code": 200,
  "message": "健康检查成功",
  "data": {
    "status": "ok",
    "timestamp": 1672531200000
  }
}
```

## API 文档

### 健康检查接口

- **URL**: `/api/health`
- **方法**: GET
- **描述**: 检查应用运行状态
- **响应**: 
  - `status`: 应用状态（ok/error）
  - `timestamp`: 当前时间戳

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

配置文件位于 `src/main/resources/application.properties`

### 数据库配置（待扩展）

当前项目使用内存数据库，可根据需要配置 MySQL、PostgreSQL 等数据库。

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