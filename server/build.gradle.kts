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
    implementation(project(":core")) // Importamos o core como dependência

    implementation("org.springframework.boot:spring-boot-starter-webmvc")

    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
}

tasks.register<Copy>("copyFrontend") {
    dependsOn(":web:buildFrontend") // Tarefa de copy que depende da tarefa de build do front

    from("../web/dist") // Copia o build do front
    into("src/main/resources/static") // Para a pasta de resources do Spring
}

tasks.named("processResources") { // Essa task sempre roda no build do spring
    dependsOn("copyFrontend") // Depende do copyFrontend, então vai rodar o build antes de iniciar o build da jar
}