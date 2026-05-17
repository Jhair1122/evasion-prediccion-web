# Construcción: Usamos Maven con JDK 11
FROM maven:3.8.6-openjdk-11 AS build
WORKDIR /app

# Copiamos el descriptor de dependencias y descargamos offline
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos el resto del código y construimos
COPY src ./src
RUN mvn clean package

# Imagen final: Usamos una JRE 11 mantenida por Eclipse Temurin
FROM eclipse-temurin:11-jre
WORKDIR /app

# Copiamos el JAR generado desde la etapa de construcción
COPY --from=build /app/target/evasion-web-1.0.jar .

# Exponemos el puerto que usará la app
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "evasion-web-1.0.jar"]
