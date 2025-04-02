From maven:3.8.6-openjdk-17-slim AS build
COPY . .
Run mvn clean package -DskipTests

FROM openjdk:22-jdk-slim
COPY --from=build /target/websocket-demo-0.0.1-SNAPSHOT.jar /app/websocket-demo.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/websocket-demo.jar"]