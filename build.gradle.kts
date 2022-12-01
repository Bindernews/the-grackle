import org.apache.tools.ant.taskdefs.condition.Os
val modName = "TheGrackle"

plugins {
    id("java")
    id("co.uzzu.dotenv.gradle") version "2.0.0"
}


// Read user settings and add defaults
val steamDir: String = env.STEAM_DIR.value
// Default directory unless user overrides
val stsSteamDir = if (Os.isFamily(Os.FAMILY_MAC)) {
    "$steamDir/common/SlayTheSpire/SlayTheSpire.app/Contents/Resources"
} else {
    "$steamDir/common/SlayTheSpire/"
}
val stsHome = env.fetch("STS_HOME", stsSteamDir)
val modsDir = env.fetch("MODS_DIR", "$stsHome/mods")

group = "io.bindernews.thegrackle"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    compileOnly("org.jetbrains:annotations:16.0.2")
    val libs = "$rootDir/lib"
    implementation(files(
        "$libs/BaseMod.jar",
        "$libs/ModTheSpire.jar",
        "$libs/StSLib.jar",
        "$libs/downfall.jar",
        "$stsHome/desktop-1.0.jar",
    ))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.register<Copy>("copySteamLibs") {
    description = "Copy libraries from the steam workshop to the local lib folder"
    val workshopDir = "$steamDir/workshop/content/646570"
    from("$workshopDir/1605060445/ModTheSpire.jar")
    from("$workshopDir/1605833019/BaseMod.jar")
    from("$workshopDir/1609158507/StSLib.jar")
    from("$workshopDir/1610056683/downfall.jar")
    into("$rootDir/lib")
}

tasks.register<Copy>("installJar") {
    dependsOn("jar")
    group = "build"
    from(tasks.getByName<Jar>("jar").archiveFile)
    rename { "$modName.jar" }
    into(modsDir)
}
