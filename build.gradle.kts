import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val springVersion = "3.1.3"

plugins {
    id("org.springframework.boot") version "3.1.3"
    id("io.spring.dependency-management") version "1.1.3"
    kotlin("jvm") version "1.9.10"
    kotlin("plugin.spring") version "1.9.10"
    id("org.flywaydb.flyway") version "9.20.1"
}

group = "com.itavgur"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    //spring boot
    implementation("org.springframework.boot:spring-boot-configuration-processor:$springVersion")
    developmentOnly("org.springframework.boot:spring-boot-devtools:$springVersion")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:$springVersion")
    //AOP
    implementation("org.springframework.boot:spring-boot-starter-aop:$springVersion")
    implementation("org.yaml:snakeyaml:2.2")
    //kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    //DB
    implementation("org.springframework.boot:spring-boot-starter-jdbc:$springVersion")
    runtimeOnly("com.mysql:mysql-connector-j")
    implementation("org.flywaydb:flyway-core:9.20.1")
    implementation("org.flywaydb:flyway-mysql:9.20.1")
    //monitoring
    implementation("org.springframework.boot:spring-boot-starter-actuator:$springVersion")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    //tests
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springVersion")
    testImplementation("org.mockito:mockito-core:5.5.0")
    //swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    //security
    implementation("org.springframework.boot:spring-boot-starter-security:$springVersion")
    //validation
    implementation("org.springframework.boot:spring-boot-starter-validation:$springVersion")
    //web
    implementation("org.springframework.boot:spring-boot-starter-web:$springVersion")

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

flyway {
    url = "jdbc:mysql://otus_mysql/otus_high"
    user = "root"
    password = "root"
    schemas = arrayOf("otus_high")
}