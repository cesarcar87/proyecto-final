# Etapa 1: Construcción
FROM maven:3.9.2-eclipse-temurin-17 AS build

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia el archivo pom.xml y descarga las dependencias
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# Copia todo el código fuente del proyecto al contenedor
COPY src ./src

# Construye el proyecto (sin ejecutar los tests)
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
FROM eclipse-temurin:17-jdk-alpine

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia el archivo JAR generado en la etapa anterior
COPY --from=build /app/target/gestion-estudiantil-1.0.0-SNAPSHOT.jar app.jar

# Copia el archivo client_secret.json al contenedor
COPY src/main/resources/client_secret.json /app/resources/client_secret_768873216358-7cdlvnvoc4rfc8vesjkp78d7j3g4ejdc.apps.googleusercontent.com.json

# Exponer el puerto donde correrá la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
