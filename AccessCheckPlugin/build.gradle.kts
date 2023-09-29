import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "net.bindernews.accesscheck"
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
    implementation("org.javassist:javassist:3.29.2-GA")
    implementation("com.google.guava:guava:32.1.2-jre")
}

gradlePlugin {
    plugins {
        create("AccessCheckPlugin") {
            id = "net.bindernews.accesscheck"
            implementationClass = "net.bindernews.accesscheck.AccessCheckPlugin"
        }
    }
}

tasks.getByName<KotlinCompile>("compileKotlin") {
    kotlinOptions.jvmTarget = "1.8"
}

