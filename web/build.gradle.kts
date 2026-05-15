import com.github.gradle.node.npm.task.NpmTask

plugins {
    id("com.github.node-gradle.node") version "7.1.0"
}

node {
    version.set("22.19.0")
    download.set(true)
}

tasks.register<NpmTask>("buildFrontend") {
    dependsOn(tasks.npmInstall)

    npmCommand.set(listOf("run", "build"))
}