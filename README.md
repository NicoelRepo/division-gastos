# División de gastos

Aplicación demo en Spring Boot para crear grupos, cargar miembros y gastos compartidos, y calcular la liquidación final entre participantes.

## Requisitos

- Java 17 instalado y disponible en `PATH`.
- Docker Desktop, solo si se quiere ejecutar SonarQube local.
- No requiere base de datos: la persistencia es en memoria y los datos se pierden al reiniciar la aplicación.

## Ejecutar la aplicación

Desde la raíz del proyecto:

```powershell
.\gradlew.bat bootRun
```

Luego abrir:

```text
http://localhost:8080/
```

En Linux/macOS:

```bash
./gradlew bootRun
```

## Ejecutar tests y controles de calidad

```powershell
.\gradlew.bat check
```

Ese comando ejecuta:

- Tests con JUnit 5.
- Reporte de cobertura con JaCoCo.
- Verificación de cobertura mínima.
- Checkstyle.

El reporte HTML de cobertura queda en:

```text
build/reports/jacoco/test/html/index.html
```

## SonarQube local

Levantar SonarQube Community Edition:

```powershell
docker compose up -d
```

Abrir:

```text
http://localhost:9000/
```

SonarQube se ejecuta localmente en Docker. No se usa SonarCloud ni otro servicio externo para analizar el proyecto.

Para publicar el analisis desde Gradle, SonarQube pide autenticacion. Por eso hay que crear un token dentro de la instancia local abierta en `http://localhost:9000/` y setearlo en la variable de entorno `SONAR_TOKEN`.

En PowerShell:

```powershell
$env:SONAR_TOKEN="tu-token"
.\gradlew.bat test jacocoTestReport checkstyleMain sonar
```

En Linux/macOS:

```bash
export SONAR_TOKEN="tu-token"
./gradlew test jacocoTestReport checkstyleMain sonar
```
