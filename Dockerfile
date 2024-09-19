# Etapa 1: Construcción
FROM maven:3.9.2-eclipse-temurin-17 AS build

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia el archivo pom.xml y las dependencias de Maven
COPY pom.xml .

# Descarga las dependencias necesarias (esto se realiza antes de copiar el código fuente para aprovechar la cache)
RUN mvn dependency:go-offline -B

# Copia todo el código fuente del proyecto en el contenedor
COPY src ./src

# Construye el proyecto utilizando Maven
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
FROM eclipse-temurin:17-jdk-alpine

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia el archivo JAR construido en la etapa anterior al contenedor
COPY --from=build /app/target/gestion-estudiantil-1.0.0-SNAPSHOT.jar app.jar

# Expon el puerto en el que la aplicación escuchará
EXPOSE 5432

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
