FROM openjdk:21-jdk-slim

WORKDIR /app

# Copiar o JAR gerado no build
COPY /target/razzies-0.0.1-SNAPSHOT.jar /app/razzies.jar

# Expõe a porta em que a aplicação Spring Boot irá rodar
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "/app/razzies.jar"]