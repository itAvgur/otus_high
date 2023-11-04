import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val springVersion = "3.1.3"

plugins {
    id("org.springframework.boot") version "3.1.3"
    id("io.spring.dependency-management") version "1.1.3"
    kotlin("jvm") version "1.9.10"
    kotlin("plugin.noarg") version "1.9.20"
    kotlin("plugin.spring") version "1.9.10"
    id("org.flywaydb.flyway") version "9.20.1"
    idea
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

noArg {
    annotation("com.itavgur.otushighload.util.NoArgConstructor")
}


group = "com.itavgur"
version = "0.0.5"

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
    implementation("org.postgresql:postgresql:${project.properties["postgres.version"]}")
    runtimeOnly("com.mysql:mysql-connector-j")
    implementation("org.flywaydb:flyway-core:${project.properties["flydb.version"]}")
    implementation("org.flywaydb:flyway-mysql:${project.properties["flydb.version"]}")
    //monitoring
    implementation("org.springframework.boot:spring-boot-starter-actuator:$springVersion")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    //tests
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springVersion")
    testImplementation("org.mockito:mockito-core:${project.properties["mockito-core.version"]}")
    //swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${project.properties["springdoc-webmvc.version"]}")
    //security
    implementation("org.springframework.boot:spring-boot-starter-security:$springVersion")
    //validation
    implementation("org.springframework.boot:spring-boot-starter-validation:$springVersion")
    //web
    implementation("org.springframework.boot:spring-boot-starter-web:$springVersion")
    //cache
    implementation("org.springframework.boot:spring-boot-starter-cache:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-redis:$springVersion")

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