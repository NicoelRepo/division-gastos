plugins {
	java
	jacoco
	checkstyle
	id("org.springframework.boot") version "4.0.6"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.sonarqube") version "6.3.1.5724"
}

group = "ar.edu.uade.grupo5"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	testImplementation("org.springframework.boot:spring-boot-starter-thymeleaf-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport)
}

jacoco {
	toolVersion = "0.8.13"
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports {
		xml.required = true
		html.required = true
		csv.required = false
	}
}

tasks.jacocoTestCoverageVerification {
	dependsOn(tasks.jacocoTestReport)
	violationRules {
		rule {
			limit {
				minimum = "0.80".toBigDecimal()
			}
		}
	}
}

tasks.check {
	dependsOn(tasks.jacocoTestCoverageVerification)
}

checkstyle {
	toolVersion = "10.21.4"
	configFile = file("config/checkstyle/checkstyle.xml")
	isIgnoreFailures = false
}

fun envOrDefault(name: String, defaultValue: String): String =
	System.getenv(name)?.takeIf { it.isNotBlank() } ?: defaultValue

sonar {
	properties {
		val sonarOrganization = System.getenv("SONAR_ORGANIZATION")?.takeIf { it.isNotBlank() }

		property("sonar.projectKey", envOrDefault("SONAR_PROJECT_KEY", "division-gastos"))
		property("sonar.projectName", "division-gastos")
		property("sonar.host.url", envOrDefault("SONAR_HOST_URL", "http://localhost:9000"))
		sonarOrganization?.let { property("sonar.organization", it) }
		System.getenv("SONAR_TOKEN")?.takeIf { it.isNotBlank() }?.let { property("sonar.token", it) }
		property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/test/jacocoTestReport.xml")
		property("sonar.java.checkstyle.reportPaths", "build/reports/checkstyle/main.xml")
	}
}
