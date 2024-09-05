FROM openjdk:17-slim

ENV TZ=Asia/Seoul

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8080 9999

CMD ["java", "-jar", "app.jar", "--spring.config.location=file:/config/application.yml"]