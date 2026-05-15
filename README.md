# Arquitetura e build

O projeto utiliza uma arquitetura **Gradle multi-module**.

Cada pasta representa um módulo independente:
- `core` → código Java puro com a entrega principal
- `server` → backend Spring Boot
- `web` → frontend React

Dessa forma, conseguimos separar o código Java puro do core em um projeto separado e
ter um step de build unificado entre os 3 componentes, onde tudo acaba empacotado numa única jar
que roda o servidor.

## Configuração dos Módulos

### `settings.gradle.kts`

Responsável por registrar os módulos do projeto.

```kotlin
rootProject.name = "trabalho-edl-filas"

include("core")
include("server")
include("web")
```

## Configuração Global

### `build.gradle.kts` (raiz)
Basicamente centraliza config de repositórios de dependências que os dois projetos back usam,
além de propagar dependências comuns.

```kotlin
plugins {
    base
}

allprojects {
    group = "br.unisinos.edl.filas"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")

    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(26))
        }
    }

    dependencies {
        testImplementation(platform("org.junit:junit-bom:5.10.0"))
        testImplementation("org.junit.jupiter:junit-jupiter")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
```
## Módulo `core`
### `core/build.gradle.kts`

```kotlin
plugins {
    `java-library` // Utilizado em projetos java em formatos de lib, como o core. Sem entrypoint.
}
```
## Módulo `server`
### `server/build.gradle.kts`

```kotlin
plugins {
    java
    id("org.springframework.boot") version "4.0.6"
    id("io.spring.dependency-management") version "1.1.7"
}

dependencies {
    implementation(project(":core")) // Importa a dependência do core

    implementation("org.springframework.boot:spring-boot-starter-web") // Importa o springboot

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
```

O módulo depende diretamente do `core` utilizando:

```kotlin
implementation(project(":core"))
```

Isso evita a necessidade de publicar dependências localmente via Maven.

---

## Módulo `web`

A config gradle do módulo `web` utiliza o plugin `id("com.github.node-gradle.node")` para integração 
entre Node.js e Gradle.

### `web/build.gradle.kts`

```kotlin
plugins {
    id("com.github.node-gradle.node") version "7.1.0"
}

node { // Seta a versão do node para o build
    version.set("22.15.0")
    download.set(true)
} 

tasks.register<NpmTask>("buildFrontend") { // Cria uma task custom pro build
    dependsOn(tasks.npmInstall) // Priemiro roda o `npm install` se precisar

    // Depois roda o comando `npm run build` que compila o código React para JS puro e HTML.
    npmCommand.set(listOf("run", "build")) 
}
```

O frontend é buildado utilizando Vite com `npm run build`, e os arquivos gerados são colocados na pasta
`/web/dist`.

Depois, ao executar o build do server (Descrito abaixo), esses arquivos são copiados para
a pasta `/server/src/main/resources/static`.

## Configuração no `server/build.gradle.kts` (Build integrado)
```kotlin
tasks.register<Copy>("copyFrontend") {
    dependsOn(":web:buildFrontend") // Tarefa de copy que depende da tarefa de build do front

    from("../web/dist") // Copia o build do front
    into("src/main/resources/static") // Para a pasta de resources do Spring
}

tasks.named("processResources") { // Essa task sempre roda no build do Spring
    // Depende do copyFrontend, então vai rodar o build antes de iniciar o build da jar
    dependsOn("copyFrontend") 
}
```
---
## Fluxo do Build
Ao executar `./gradlew build`: 

1. Builda o core
2. Builda o frontend React
3. Gera os arquivos estáticos
4. Copia os arquivos para o Spring Boot e gera a jar com tudo incluido
---
## Servindo o Frontend
O Spring Boot serve automaticamente arquivos presentes na pasta `src/main/resources/static`

Isso permite que backend e frontend sejam distribuídos juntos em uma única aplicação.

---
## Compatibilidade com SPA
Em projetos com rotas no front em modo SPA (React Router, por exemplo), é necessário fazer o Spring "ignorar" 
essas requests. Pra isso usamos esse controler que redireciona as requests de volta pro front.

### `SpaController.java`
```java
@Controller
public class SpaController {
    @RequestMapping(value = {"/{path:[^\\.]*}", "/**/{path:[^\\.]*}"})
    public String redirect() {
        return "forward:/index.html";
    }
}
```
Isso garante funcionamento correto de rotas SPA caso o projeto utilize. Em projetos reais, coisas como:
```text
/dashboard (Tela de dashboard)
/users (Tela de listagem de usuários)
/settings (Tela de configurações)
```
---

## Desenvolvimento Local

### Backend
```bash
./gradlew :server:bootRun
```

### Frontend
```bash
npm run dev
```
---
## Proxy do Vite
O Vite (Responsável pelo build do React e funciona como dev server também) tem essa funcionalidade
de proxy que facilita o desenvolvimento local sem ter que mexer em código. Toda request fetch que
roda direcionada ao `/api` quando rodando o projeto com `npm run dev` redireciona pro backend.

Isso permite subir o vite em modo dev (Com hot reload e afins) sem ter que adaptar o código 
para ter uma variação por ambiente.
### `vite.config.ts`
```ts
server: {
  proxy: {
    "/api": {
      target: "http://localhost:8080",
      changeOrigin: true
    }
  }
}
```
Durante o desenvolvimento:
- frontend roda separado,
- backend roda separado,
- chamadas `/api` são redirecionadas automaticamente.