FROM openjdk:8-jdk-alpine
# 设置工作目录
WORKDIR /app

# 将可执行jar文件复制到工作目录
COPY .target/banksystem-1.0-SNAPSHOT.jar /app/

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "banksystem-1.0-SNAPSHOT.jar"]

