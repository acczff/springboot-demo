FROM maven:3.9-eclipse-temurin-21 AS builder

ARG MODULE
WORKDIR /build

ENV MAVEN_OPTS="-Xmx512m -XX:MaxMetaspaceSize=256m -Djava.awt.headless=true"

COPY pom.xml ./
COPY springboot-demo-user/pom.xml springboot-demo-user/pom.xml
COPY springboot-demo-biz/pom.xml springboot-demo-biz/pom.xml
COPY springboot-demo-gateway/pom.xml springboot-demo-gateway/pom.xml

RUN mvn -pl ${MODULE} -am dependency:go-offline -DskipTests

COPY springboot-demo-user/src springboot-demo-user/src
COPY springboot-demo-biz/src springboot-demo-biz/src
COPY springboot-demo-gateway/src springboot-demo-gateway/src

RUN mvn -pl ${MODULE} -am package -DskipTests

FROM eclipse-temurin:21-jre

ARG MODULE
WORKDIR /app

ENV JAVA_TOOL_OPTIONS="-XX:+UseSerialGC -Djava.security.egd=file:/dev/./urandom"

COPY --from=builder /build/${MODULE}/target/*.jar app.jar

EXPOSE 8080 8081 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
