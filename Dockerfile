FROM openjdk:17-alpine
# 환경 설정

# 권한 부여
#ARG JAR_FILE=/build/libs/cube-log-0.0.1-SNAPSHOT.jar
#COPY ${JAR_FILE} /cube-log.jar
COPY build/libs/*.jar /cube-log.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar", "/cube-log.jar"]


