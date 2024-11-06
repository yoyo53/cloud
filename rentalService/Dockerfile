FROM openjdk:17-jdk-slim AS build

WORKDIR /app

COPY . .

RUN ./gradlew build

FROM openjdk:17-jdk-alpine

COPY --from=build /app/build/libs/*.jar /app/app.jar

CMD ["java", "-jar", "/app/app.jar"]