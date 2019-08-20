FROM maven:3.6-jdk-8-alpine As builder
WORKDIR /app
COPY pom.xml .
RUN mvn -e -B dependency:resolve
COPY src ./src
RUN mvn -e -B package

FROM openjdk:8-jre-alpine
ARG arg
COPY --from=builder /app/target/money-transfer-*.jar /app.jar
CMD ["java", "-jar", "app.jar", "${arg}"]
