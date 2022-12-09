import com.badlogic.gdx.tools.texturepacker.TexturePacker
import org.apache.tools.ant.taskdefs.condition.Os
import org.apache.tools.ant.util.ReaderInputStream
import org.gradle.api.internal.file.CopyActionProcessingStreamAction
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.internal.file.copy.CopyAction
import org.gradle.api.internal.file.copy.FileCopyDetailsInternal
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.FilterReader
import java.io.Reader
import java.io.StringReader
import javax.imageio.ImageIO

val modName = "TheGrackle"

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.badlogicgames.gdx:gdx-tools:1.5.4")
    }
}

plugins {
    id("java")
    id("co.uzzu.dotenv.gradle") version "2.0.0"
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

group = "io.bindernews.thegrackle"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    compileOnly("org.jetbrains:annotations:16.0.2")
    implementation(files(
        findMod("BaseMod.jar"),
        findMod("ModTheSpire.jar"),
        findMod("StSLib.jar"),
        findMod("downfall.jar"),
        findMod("TS05_Marisa.jar"),
        "$stsHome/desktop-1.0.jar",
    ))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.getByName<JavaCompile>("compileJava") {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
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
}

run {
    val resRoot = "$projectDir/src/main/resources/$RES_DIR"
    val resOut = "$buildDir/resources/main/$RES_DIR"
    val packerTmp = "$buildDir/generated/images"

    tasks.register<Copy>("processImages") {
        filteringCharset = ResizeFilter.BINARY_CHARSET
        val src = "$resRoot/images"
        from("$src/1024") {
            include("*.png")
            filter(ResizeFilter::class, "width" to 512, "height" to 512)
            into("512")
        }
        from("$src/energy") {
            include("energy_orb.png")
            rename { _ -> "card_small_orb.png" }
            filter(ResizeFilter::class, "width" to 22, "height" to 22)
            into("512")
        }
        from("$src/energy") {
            include("energy_orb.png")
            into("1024")
        }
        into("$resOut/images")
    }

    tasks.register<Copy>("shrinkCards") {
        filteringCharset = ResizeFilter.BINARY_CHARSET
        // Resize all _p cards to be smaller
        from("$resRoot/images/cards") {
            val SUFFIX = "_p.png"
            include("*${SUFFIX}")
            rename { n -> n.substring(0, n.length - SUFFIX.length) + ".png" }
            // Real size is 250x190, but this is going from the base game card atlas sizes
            filter(ResizeFilter::class, "width" to 248, "height" to 186)
        }
        into("$packerTmp/grackle/cards")
    }

    tasks.register("packTextures") {
        dependsOn("shrinkCards")
        doLast {
            val cfg = TexturePacker.Settings()
            cfg.maxHeight = 4096
            cfg.maxWidth = 4096
            cfg.grid = true
            cfg.combineSubdirectories = true
            cfg.filterMag = com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
            cfg.filterMin = cfg.filterMag
            TexturePacker.process(cfg, packerTmp, "$resOut/images/cards", "cards")
        }
    }

    tasks.getByName("processResources").dependsOn("processImages", "packTextures")
}

tasks.register<DefaultTask>("genIntelliJRuns") {
    description = "Generate run configurations for IntelliJ"
    // TODO use groovy.Node to programmatically generate XML
    val d = "\$"
    val config = """
    <component name="ProjectRunConfigurationManager">
      <configuration default="false" name="Run MTS" type="JarApplication">
        <option name="JAR_PATH" value="$mtsJar" />
        <option name="PROGRAM_PARAMETERS" value="--mods basemod,stslib,grackle" />
        <option name="WORKING_DIRECTORY" value="$stsHome" />
        <option name="ALTERNATIVE_JRE_PATH_ENABLED" value="true" />
        <option name="ALTERNATIVE_JRE_PATH" value="${stsHome}/jre" />
        <method v="2">
            <option name="Gradle.BeforeRunTask" enabled="true" tasks="installJar" externalProjectPath="${d}PROJECT_DIR${d}" vmOptions="" scriptParameters="" />
        </method>
      </configuration>
    </component>""".trimIndent()

    outputs.file("$rootDir/.idea/runConfigurations/Run_MTS.xml")
    doLast {
        for (f in outputs.files) {
            f.absoluteFile.writeText(config)
        }
    }
}

class ResizeFilter(`in`: Reader) : FilterReader(`in`) {
    companion object {
        const val BINARY_CHARSET = "ISO-8859-1"
    }

    private val outReader: StringReader by lazy { loadImage() }

    var width: Int = -1
    var height: Int = -1
    override fun read(cbuf: CharArray?, off: Int, len: Int): Int {
        return outReader.read(cbuf, off, len)
    }

    fun loadImage(): StringReader {
        val buf = ImageIO.read(ReaderInputStream(`in`, Charsets.ISO_8859_1))
        val nWidth = if (width == -1) buf.width else width
        val nHeight = if (height == -1) buf.height else height
        val scaled = buf.getScaledInstance(nWidth, nHeight, BufferedImage.SCALE_SMOOTH)
        val scaledBuf = BufferedImage(nWidth, nHeight, BufferedImage.TYPE_INT_ARGB)
        scaledBuf.graphics.drawImage(scaled, 0, 0, null)
        val outBuf = ByteArrayOutputStream()
        ImageIO.write(scaledBuf, "png", outBuf)
        return StringReader(outBuf.toString(Charsets.ISO_8859_1))
    }
}

open class ResizeImageCopy : Copy() {

    @Input var width: Int = 0
    @Input var height: Int = 0

    private lateinit var resolver: FileResolver

    override fun createCopyAction(): CopyAction {
        resolver = fileLookup.getFileResolver(destinationDir)
        return CopyAction { stream ->
            val w = ResizeWorker()
            stream.process(w)
            WorkResults.didWork(w.didWork)
        }
    }

    inner class ResizeWorker: CopyActionProcessingStreamAction {
        var didWork: Boolean = false
        private val scaledBuf = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

        override fun processFile(details: FileCopyDetailsInternal) {
            val target = resolver.resolve(details.relativePath.pathString)
            val buf = ImageIO.read(details.file)
            val scaled = buf.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH)
            scaledBuf.graphics.drawImage(scaled, 0, 0, null)
            ImageIO.write(scaledBuf, "png", target)
            didWork = true
        }
    }
}

fun findMod(name: String): File {
    val workshopId = WORKSHOP_IDS[name]
    // TODO change to a list
    val paths = files("$rootDir/libs", modsDir, "$workshopDir/$workshopId")
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
