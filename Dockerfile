# ---------- Build stage ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests package && cp target/*.jar /app/app.jar

# ---------- Run stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/app.jar /app/app.jar

ENV PORT=3000
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
EXPOSE 3000
CMD ["sh", "-c", "java -Dserver.port=${PORT} -Dserver.address=0.0.0.0 -jar /app/app.jar"]
