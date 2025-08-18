FROM gradle:8.8-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle clean bootJar -x test

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
