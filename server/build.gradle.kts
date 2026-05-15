plugins {
    id("org.springframework.boot") version "4.0.6"
    id("io.spring.dependency-management") version "1.1.7"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(26)
    }
}

dependencies {
    implementation(project(":core"))

    implementation("org.springframework.boot:spring-boot-starter-webmvc")

    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
}

tasks.register<Copy>("copyFrontend") {
    dependsOn(":web:buildFrontend")

    from("../web/dist")
    into("src/main/resources/static")
}

tasks.named("processResources") {
    dependsOn("copyFrontend")
}