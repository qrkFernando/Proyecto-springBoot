# Dockerfile para Librería Online - Spring Boot Application

# Etapa 1: Build
FROM openjdk:22-jdk-slim AS builder

WORKDIR /app

# Copiar archivos de Maven
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Copiar código fuente
COPY src src

# Hacer el wrapper executable
RUN chmod +x ./mvnw

# Construir la aplicación
RUN ./mvnw clean package -DskipTests

# Etapa 2: Runtime
FROM openjdk:22-jre-slim

WORKDIR /app

# Crear usuario no-root para seguridad
RUN groupadd -r libreria && useradd -r -g libreria libreria

# Instalar herramientas básicas
RUN apt-get update && apt-get install -y \
    && rm -rf /var/lib/apt/lists/*

# Copiar JAR desde etapa de build
COPY --from=builder /app/target/libreria-online.jar app.jar

# Crear directorio para logs y datos
RUN mkdir -p /app/logs /app/data && \
    chown -R libreria:libreria /app

# Cambiar a usuario no-root
USER libreria

# Configurar variables de entorno
ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8080
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Exponer puerto
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Comando de inicio
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

# Metadatos
LABEL maintainer="atahualpa@example.com"
LABEL version="1.0.0"
LABEL description="Librería Online - Sistema completo de librería con Spring Boot"