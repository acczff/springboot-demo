# 第一阶段：构建阶段 (使用 Maven 镜像)
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /build

# 利用 Docker 层缓存：先复制 pom.xml 并下载依赖
COPY pom.xml .
# 这一步会下载所有依赖，只要 pom.xml 不变，这一层的缓存就会被保留，以后构建会非常快
RUN mvn dependency:go-offline -B

# 复制源代码并打包
COPY src ./src
RUN mvn package -DskipTests

# 第二阶段：运行阶段 (使用轻量级 JDK 镜像)
FROM eclipse-temurin:21-jre
WORKDIR /app

# 从构建阶段(builder)将打包好的 jar 复制到运行阶段
COPY --from=builder /build/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]