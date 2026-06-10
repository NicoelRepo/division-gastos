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

## Integracion continua

El repositorio incluye un workflow de GitHub Actions en `.github/workflows/ci.yml`.

En cada `push` a `master` o `main`, y en cada pull request, GitHub ejecuta:

- Compilacion.
- Tests con JUnit 5.
- Reporte y verificacion de cobertura con JaCoCo.
- Checkstyle.
- Publicacion de reportes como artifact del workflow.

El job de SonarCloud corre despues de los controles anteriores usando el mismo task Gradle `sonar` que se usa localmente. Para habilitarlo en GitHub hay que configurar:

- Repository variable `SONAR_ORGANIZATION`.
- Repository variable `SONAR_PROJECT_KEY`.
- Repository secret `SONAR_TOKEN`.

El host de SonarCloud se define en el workflow con `SONAR_HOST_URL=https://sonarcloud.io`. El workflow espera el resultado del Quality Gate de SonarCloud con `sonar.qualitygate.wait=true`; si el Quality Gate falla, el job de SonarCloud tambien falla.

SonarCloud analiza `src/main/java` y `src/main/resources` para incluir las plantillas Thymeleaf en el analisis de seguridad. Los tests se declaran en `src/test/java`.

La cobertura local de JaCoCo y la cobertura mostrada por SonarCloud pueden no tener el mismo porcentaje porque no representan exactamente la misma metrica. La verificacion local actual de Gradle usa cobertura de instrucciones de JaCoCo con minimo 80%, mientras que SonarCloud muestra una cobertura combinada de lineas y condiciones. Para el flujo de CI, SonarCloud queda como gate final de calidad en nube.

## Despliegue continuo

El proyecto incluye configuracion para desplegar en Render mediante Blueprint:

- `Dockerfile`: construye el JAR de Spring Boot y lo ejecuta en una imagen Java 17.
- `render.yaml`: define un Web Service Docker en plan free.
- `server.port=${PORT:8080}`: permite que Render asigne el puerto HTTP con la variable `PORT`.

Render queda configurado para desplegar desde `master` solo cuando los checks del repositorio pasan:

```yaml
autoDeployTrigger: checksPass
```

Esto mantiene el flujo esperado: primero GitHub Actions valida build, tests, cobertura, Checkstyle y SonarCloud; despues Render despliega automaticamente el commit aprobado.

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

Por defecto, el task `sonar` apunta al host local `http://localhost:9000` y usa `division-gastos` como project key. Esos valores se pueden pisar con variables de entorno:

- `SONAR_HOST_URL`
- `SONAR_ORGANIZATION`
- `SONAR_PROJECT_KEY`
- `SONAR_TOKEN`

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
