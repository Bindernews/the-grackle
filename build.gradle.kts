import net.bindernews.grimage.*
import net.bindernews.grimage.idearuns.IntellijRun
import net.bindernews.grimage.idearuns.V2Option
import net.bindernews.grimage.idearuns.jar
import org.apache.tools.ant.taskdefs.condition.Os
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.awt.Dimension
import java.awt.Rectangle
import java.awt.image.BufferedImage
import java.io.FileNotFoundException
import java.io.Reader

val modName = "TheGrackle"
group = "io.bindernews.thegrackle"
version = "0.2-SNAPSHOT"

plugins {
    id("java")
    // MTS ships kotlin 1.4.10, but as long as we don't use any newer stdlib
    // features, we can get the compiler improvements of 1.5
    kotlin("jvm") version "1.5.32"
    id("co.uzzu.dotenv.gradle") version "2.0.0"
    id("io.freefair.lombok") version "6.6"
    id("net.bindernews.grimage")
}

// Constants
/** Map of deps to their Steam workshop IDs */
val WORKSHOP_IDS = mapOf(
        "ModTheSpire.jar" to "1605060445",
        "BaseMod.jar" to "1605833019",
        "StSLib.jar" to "1609158507",
        "downfall.jar" to "1610056683",
        "TS05_Marisa.jar" to "1614104912",
)

// Read user settings and add defaults
val steamDir: String = env.STEAM_DIR.value
val stsHome = env.fetchOrNull("STS_HOME") ?: getStsSteamHome()
val modsDir = env.fetch("MODS_DIR", "$stsHome/mods")
val workshopDir = "$steamDir/workshop/content/646570"
val mtsJar = findMod("ModTheSpire.jar")

val RES_DIR = "grackleResources"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    compileOnly("org.jetbrains:annotations:16.0.2")
    implementation(files(
        // Add the patched version if it exists, so we can get better IDE help.
//        file("$projectDir/lib/desktop-1.0-patched.jar").takeIf { it.exists() },
        // base game
        "$stsHome/desktop-1.0.jar",
        // mods
        findMod("BaseMod.jar"),
        findMod("ModTheSpire.jar"),
        findMod("StSLib.jar"),
        findMod("downfall.jar"),
//        findMod("TS05_Marisa.jar"),
    ))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.getByName<JavaCompile>("compileJava") {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
    options.encoding = "UTF-8"
}

tasks.getByName<KotlinCompile>("compileKotlin") {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs += "-Xno-param-assertions"
}

tasks.register<Copy>("copySteamLibs") {
    description = "Copy libraries from the steam workshop to the local lib folder"
    for (dep in WORKSHOP_IDS) {
        from("$workshopDir/${dep.value}/${dep.key}")
    }
    into("$rootDir/lib")
}

tasks.register<Copy>("installJar") {
    dependsOn("jar")
    group = "build"
    from(tasks.getByName<Jar>("jar").archiveFile)
    rename { "$modName.jar" }
    into(modsDir)
//    this.grimage.packImages {
//
//    }
}

run {
    val resRoot = "$projectDir/src/main/resources/$RES_DIR"
    val resOut = "$buildDir/resources/main/$RES_DIR"
    val packerTmp = "$buildDir/generated/images"

    val tResizeImages = tasks.register<MyImageCopy>("resizeImages") {
        val src = "$resRoot/images"
        from("$src/1024") {
            include("*.png")
            filterImage(ResizeOp(512, 512))
        }
        from("$src/energy") {
            include("energy_orb.png")
            rename { _ -> "card_small_orb.png" }
            filterImage(ResizeOp(22, 22))
        }
        // Make orb in top-left corner
        from("$src/energy") {
            include("energy_orb.png")
            rename { _ -> "card_default_gray_orb.png" }
            filter(MoveEnergyOrbFilter::class)
        }
        into("$resOut/images/512")
    }

    val tCopyEnergy = tasks.register<MyImageCopy>("copyEnergy") {
        from("$resRoot/images/energy")
        include("energy_orb.png")
        into("$resOut/images/1024")
    }

    val tMaskCards = tasks.register<MyImageCopy>("maskCards") {
        val cardsRoot = "$resRoot/images/cards"
        from("$cardsRoot/attack") {
            filterImage(MaskOp(file("$cardsRoot/attack/attack_mask.png")))
        }
        from("$cardsRoot/power") {
            filterImage(MaskOp(file("$cardsRoot/power/power_mask.png")))
        }
        from("$cardsRoot/skill") {
            filterImage(MaskOp(file("$cardsRoot/skill/skill_mask.png")))
        }
        include("*_p.png")
        into("$resOut/images/cards")
    }

    val tPackCards = tasks.register<MyImageCopy>("packCards") {
        // Resize all _p cards to be smaller
        // Real size is 250x190, but this is going from the base game card atlas sizes
        dependsOn(tMaskCards)
        from("$resOut/images/cards")
        filterImage(ResizeOp(248, 186))
        changeSuffix("_p.png", ".png")
        into("$packerTmp/cards")

        packImages {
            grid = true
            source("$packerTmp/cards")
            output("$resOut/images/cards/cards.atlas")
        }
    }

    val tPackIcons = tasks.register("packIcons") {
        group = "images"
        packImages {
            source("$resRoot/images/icons")
            output("$resOut/images/icons.atlas")
        }
    }

    val tResizePowers = tasks.register<MyImageCopy>("resizePowerImages") {
        from("$resRoot/images/powers") {
            include("*.png")
            filterImage(ResizeOp(48, 48))
            into("48")
        }
        from("$resRoot/images/powers") {
            include("*.png")
            filterImage(ResizeOp(128, 128))
            into("128")
        }
        into("$packerTmp/powers")

        packImages {
            source("$packerTmp/powers")
            output("$resOut/images/powers/powers.atlas")
        }
    }

    val tRelicOutlines = tasks.register<MyImageCopy>("relicOutlines") {
        from("$resRoot/images/relics") {
            changeSuffix(".png", "_o.png")
            filterImage(OutlineOp())
        }
        into("$resOut/images/relics")
    }

    tasks.getByName<Copy>("processResources") {
        dependsOn(tResizeImages, tPackIcons, tPackCards, tMaskCards, tResizePowers, tRelicOutlines, tCopyEnergy)
        // Exclude directories that are directly being packed or manually copied
        val resImg = "grackleResources/images"
        exclude("$resImg/icons", "$resImg/powers", "$resImg/cards")
    }
}

tasks.register<IntellijRun>("genIntelliJRuns") {
    description = "Generate run configurations for IntelliJ"
    add {
        name = "Run MTS"
        jar {
            jarPath(mtsJar)
            parameters("--mods basemod,stslib,grackle")
            workingDirectory(stsHome)
            alternateJre(file("$stsHome/jre"))
            v2Options.add(V2Option.beforeLaunch("installJar"))
        }
    }
    add {
        name = "Run MTS with Downfall"
        jar {
            jarPath(mtsJar)
            parameters("--mods basemod,stslib,downfall,grackle --skip-intro")
            workingDirectory(stsHome)
            alternateJre(file("$stsHome/jre"))
            v2Options.add(V2Option.beforeLaunch("installJar"))
        }
    }
}

tasks.register("packagePatchedJar") {
    dependsOn("installJar")
    doLast {
        exec {
            executable = findJavaExe("$stsHome/jre")
            workingDir = file(stsHome)
            args("-jar", mtsJar, "--mods", "basemod,stslib,grackle", "--out-jar", "--close-when-finished")
        }
        copy {
            from("$stsHome/desktop-1.0-patched.jar")
            into("$projectDir/lib")
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
        }
    }
}

//------------------
// Begin Helper Code
//------------------

// This is a bunch of helper functions and classes.


fun findMod(name: String): File {
    val workshopId = WORKSHOP_IDS[name]
    val paths = listOf("$rootDir/libs", modsDir, "$workshopDir/$workshopId").map { file(it) }
    for (p in paths) {
        val child = p.resolve(name)
        if (p.resolve(name).isFile) {
            return child
        }
    }
    throw FileNotFoundException(name)
}

fun getStsSteamHome(): String {
    val macPath = "/SlayTheSpire.app/Contents/Resources"
    return "$steamDir/common/SlayTheSpire" + if (Os.isFamily(Os.FAMILY_MAC)) macPath else ""
}

fun findJavaExe(javaHome: String): String {
    val exeWin = file("$javaHome/bin/java.exe")
    val exeNix = file("$javaHome/bin/java")
    return if (exeWin.exists()) {
        exeWin.absolutePath
    } else if (exeNix.exists()) {
        exeNix.absolutePath
    } else {
        throw Exception("could not find java executable")
    }
}

class MoveEnergyOrbFilter(`in`: Reader) : ImageFilter(`in`) {
    var orbArea = Rectangle(82, 22, 82, 82)
    var imgSize = Dimension(512, 512)

    override fun processImage(src: BufferedImage): BufferedImage {
        val dst = BufferedImage(imgSize.width, imgSize.height, BufferedImage.TYPE_INT_ARGB)
        val g = dst.createGraphics()
        try {
            g.drawImage(src, orbArea.x, orbArea.y, orbArea.width, orbArea.height, null)
        } finally {
            g.dispose()
        }
        return dst
    }
}

open class MyImageCopy : ImageCopy() {
    init {
        group = "images"
    }
}
