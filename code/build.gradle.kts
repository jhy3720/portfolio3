import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.0.6"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("jvm") version "1.7.22"
	kotlin("plugin.spring") version "1.7.22"
}

group = "com.project"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17


repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web:3.0.6")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	//JWT토큰 발행 관련
	implementation ("commons-codec:commons-codec:1.15")
	implementation ("com.auth0:auth0:1.27.0")
	implementation ("com.auth0:java-jwt:3.18.1")
	implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
	implementation ("commons-codec:commons-codec:1.15")
	implementation("org.json:json:20210307")
	implementation ("javax.xml.bind:jaxb-api:2.3.1")


	//JWT 인터셉터사용 관련
	implementation("org.springframework.boot:spring-boot-starter-security:3.0.6")
	implementation("io.jsonwebtoken:jjwt-api:0.11.2")
	implementation("io.jsonwebtoken:jjwt-impl:0.11.2")
	implementation("io.jsonwebtoken:jjwt-jackson:0.11.2")

	//JPA 사용관련
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.0.6")
	// PostgreSQL JDBC 라이브러리
	runtimeOnly("org.postgresql:postgresql")


	// JDBC 드라이버 - 예시로 H2 데이터베이스 사용
	implementation("com.h2database:h2:1.4.200")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
