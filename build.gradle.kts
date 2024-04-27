import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

plugins {
    kotlin("jvm") version "1.9.23"
}

group = "org.openartifact.test"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.kotlin-graphics:glm:0.9.9.1-12")

    implementation(platform("org.lwjgl:lwjgl-bom:3.3.3"))
    implementation("org.lwjgl", "lwjgl-glfw")


    implementation(project(":Artifact"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

tasks.jar {
    manifest.attributes["Main-Class"] = "EngineLaunchKt"
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree)
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.register<Delete>("cleanTargetDir") {
    delete("${System.getProperty("user.home")}/.artifactengine/projects/ArtifactTestProject/gameData")
}

tasks.register<Copy>("copyResourcesToProjectGameData") {
    group = "build"

    dependsOn("cleanTargetDir")

    from("src/main/resources")
    into("${System.getProperty("user.home")}/.artifactengine/projects/ArtifactTestProject/gameData")
}

tasks.named("build").configure {
    dependsOn("copyResourcesToProjectGameData")
}