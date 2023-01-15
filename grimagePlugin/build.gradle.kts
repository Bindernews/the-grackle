import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "net.bindernews.grimage"
version = "1.0.0-SNAPSHOT"


plugins {
    kotlin("jvm") version "1.6.21"
    id("java-gradle-plugin")
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.badlogicgames.gdx:gdx-tools:1.5.4")
}

gradlePlugin {
    plugins {
        create("grimagePlugin") {
            id = "net.bindernews.grimage"
            implementationClass = "net.bindernews.grimage.GrimagePlugin"
        }
    }
}

tasks.getByName<KotlinCompile>("compileKotlin") {
    kotlinOptions.jvmTarget = "1.8"
}

