import com.badlogic.gdx.tools.texturepacker.TexturePacker
import com.badlogic.gdx.tools.texturepacker.TexturePackerFileProcessor
import org.apache.tools.ant.taskdefs.condition.Os
import org.apache.tools.ant.util.ReaderInputStream
import java.awt.Image
import java.awt.image.BufferedImage
import java.awt.image.ImageObserver
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.FilenameFilter
import java.io.FilterReader
import java.io.Reader
import java.io.StringReader
import java.util.concurrent.locks.ReentrantLock
import javax.imageio.ImageIO
import kotlin.concurrent.withLock

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
    id("io.freefair.lombok") version "6.6"
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

    tasks.register<Copy>("resizeImages") {
        group = "images"
        filteringCharset = ImageFilter.BINARY_CHARSET
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

    val shrinkCards = tasks.register<Copy>("shrinkCards") {
        group = "images"
        filteringCharset = ImageFilter.BINARY_CHARSET
        // Resize all _p cards to be smaller
        from("$resRoot/images/cards") {
            val suffix = "_p.png"
            include("*${suffix}")
            rename { n -> n.substring(0, n.length - suffix.length) + ".png" }
            // Real size is 250x190, but this is going from the base game card atlas sizes
            filter(ResizeFilter::class, "width" to 248, "height" to 186)
        }
        into("$packerTmp/cards")
    }

    tasks.register<PackTextureTask>("packCards") {
        group = "images"
        dependsOn(shrinkCards)
        grid.set(true)
        source("$packerTmp/cards")
        output("$resOut/images/cards/cards.atlas")
    }

    val packIcons = tasks.register<PackTextureTask>("packIcons") {
        group = "images"
        source("$resRoot/images/icons")
        output("$resOut/images/icons.atlas")
    }

    val resizePowers = tasks.register<Copy>("resizePowerImages") {
        group = "images"
        filteringCharset = ImageFilter.BINARY_CHARSET
        from("$resRoot/images/powers") {
            include("*.png")
            filter(ResizeFilter::class, "width" to 48, "height" to 48)
            into("48")
        }
        from("$resRoot/images/powers") {
            include("*.png")
            filter(ResizeFilter::class, "width" to 128, "height" to 128)
            into("128")
        }
        into("$packerTmp/powers")
    }

    val packPowers = tasks.register<PackTextureTask>("packPowers") {
        group = "images"
        dependsOn(resizePowers)
        source("$packerTmp/powers")
        output("$resOut/images/powers/powers.atlas")
    }

    val relicOutlines = tasks.register<Copy>("relicOutlines") {
        group = "images"
        filteringCharset = ImageFilter.BINARY_CHARSET
        from("$resRoot/images/relics") {
            include("*.png")
            filter(OutlineFilter::class)
            rename { n -> changeSuffix(n, ".png", "_o.png") }
        }
        into("$resOut/images/relics")
    }

    tasks.getByName<Copy>("processResources") {
        dependsOn("resizeImages", "packCards", packIcons, packPowers, relicOutlines)
        // Exclude directories that are directly being packed
        val resImg = "grackleResources/images"
        exclude("$resImg/icons", "$resImg/powers")
    }
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


abstract class ImageFilter(`in`: Reader) : FilterReader(`in`) {
    companion object {
        const val BINARY_CHARSET = "ISO-8859-1"

        fun makeBuffered(src: Image): BufferedImage {
            if (src is BufferedImage) {
                return src
            }
            val w = src.getWidth(null)
            val h = src.getHeight(null)
            val buf = BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)
            val waiter = WaitObserver()
            if (!buf.graphics.drawImage(src, 0, 0, w, h, waiter)) {
                waiter.waitFor()
            }
            return buf
        }
    }

    private val outReader: StringReader by lazy { loadImage() }
    val transformers = ArrayList<Transformer<BufferedImage, BufferedImage>>()

    override fun read(cbuf: CharArray?, off: Int, len: Int): Int {
        return outReader.read(cbuf, off, len)
    }

    private fun loadImage(): StringReader {
        val src = ImageIO.read(ReaderInputStream(`in`, Charsets.ISO_8859_1))
        val dst = processImage(src)
        val outBuf = ByteArrayOutputStream()
        ImageIO.write(dst, "png", outBuf)
        return StringReader(outBuf.toString(BINARY_CHARSET))
    }

    fun add(t: Transformer<BufferedImage, BufferedImage>): ImageFilter {
        transformers.add(t)
        return this
    }

    abstract fun processImage(src: BufferedImage): BufferedImage

    class WaitObserver : ImageObserver {
        private val lock = ReentrantLock()
        private val cond = lock.newCondition()
        private var done = false

        override fun imageUpdate(img: Image, infoflags: Int, x: Int, y: Int, width: Int, height: Int): Boolean {
            if (infoflags and ImageObserver.ALLBITS > 0) {
                lock.withLock {
                    done = true
                    cond.signalAll()
                }
                return false
            }
            return true
        }

        fun waitFor() {
            lock.withLock {
                if (!done) {
                    cond.await()
                }
            }
        }
    }
}

class ResizeFilter(`in`: Reader) : ImageFilter(`in`) {
    var width: Int = -1
    var height: Int = -1

    override fun processImage(src: BufferedImage): BufferedImage {
        val nWidth = if (width == -1) src.width else width
        val nHeight = if (height == -1) src.height else height
        val scaled = src.getScaledInstance(nWidth, nHeight, BufferedImage.SCALE_SMOOTH)
        return makeBuffered(scaled)
    }
}


class OutlineFilter(`in`: Reader) : ImageFilter(`in`) {
    override fun processImage(src: BufferedImage): BufferedImage {
        for (y in 0 until src.height) {
            for (x in 0 until src.width) {
                val c = src.data.getSample(x, y, 3)
                if (c > 0) {
                    src.setRGB(x, y, 0xffffff or c.shl(24))
                } else {
                    src.setRGB(x, y, 0)
                }
            }
        }
        return src
    }
}


abstract class PackTextureTask : DefaultTask() {
    @get:Input abstract val maxWidth: Property<Int>
    @get:Input abstract val maxHeight: Property<Int>
    @get:Input abstract val grid: Property<Boolean>
    @get:InputDirectory abstract val inputDir: Property<File>
    @get:OutputFile abstract val outputPath: Property<File>
    private var filter: FilenameFilter = FilenameFilter { _, _ -> true }

    init {
        maxWidth.convention(4096)
        maxHeight.convention(4096)
        grid.convention(false)
    }

    fun filter(f: (File, String) -> Boolean) {
        this.filter = FilenameFilter(f)
    }

    fun source(path: Any) {
        inputDir.set(project.file(path))
    }

    fun output(path: Any) {
        outputPath.set(project.file(path))
    }

    @TaskAction
    fun pack() {
        val input = inputDir.get()
        val outputRoot = outputPath.get().parentFile
        val packName = outputPath.get().name
        val cfg = TexturePacker.Settings()
        cfg.maxHeight = maxHeight.get()
        cfg.maxWidth = maxWidth.get()
        cfg.grid = grid.get()
        cfg.combineSubdirectories = true
        cfg.filterMag = com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
        cfg.filterMin = cfg.filterMag

        didWork = if (TexturePacker.isModified("" + input, "" + outputRoot, packName)) {
            try {
                val processor = TexturePackerFileProcessor(cfg, packName)
                // Sort input files by name to avoid platform-dependent atlas output changes.
                processor.setComparator(Comparator<File> { file1, file2 -> file1.name.compareTo(file2.name) })
                processor.setInputFilter(filter)
                processor.process(input, outputRoot)
            } catch (ex: Exception) {
                throw RuntimeException("Error packing images.", ex)
            }
            true
        } else {
            false
        }
    }
}

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

fun changeSuffix(s: String, suffixIn: String, suffixOut: String): String {
    return if (s.endsWith(suffixIn)) {
        s.substring(0, s.length - suffixIn.length) + suffixOut
    } else {
        s
    }
}
