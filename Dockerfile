# Stage 1: Build the application
FROM maven:3.9.1-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jre
WORKDIR /app

# 필요한 유틸리티 설치
RUN apt-get update && apt-get install -y iputils-ping telnet && rm -rf /var/lib/apt/lists/*

# 애플리케이션 복사 및 실행 준비
COPY --from=build /app/target/*.jar app.jar
COPY wait-for-it.sh /app/wait-for-it.sh

# wait-for-it.sh 실행 권한 추가
RUN chmod +x /app/wait-for-it.sh

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
