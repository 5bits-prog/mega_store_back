# Usa una imagen oficial de Java como base
FROM eclipse-temurin:17-jdk-alpine

# Establece el directorio de trabajo en el contenedor
WORKDIR /app

# Copia el archivo JAR al contenedor
COPY target/mi-aplicacion.jar app.jar

# Expone el puerto 8080 para que la aplicación sea accesible
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
