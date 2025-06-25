# первичная сборка
FROM maven:3.8.5-openjdk-17 AS assembly
WORKDIR /backend

COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src

RUN mvn clean package -DskipTests

# второй этап сборки
FROM eclipse-temurin:17-jre
WORKDIR /backend
COPY --from=assembly /backend/target/backend-0.0.1-SNAPSHOT.jar .

CMD ["java", "-jar", "backend-0.0.1-SNAPSHOT.jar"]