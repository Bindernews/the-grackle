import com.badlogic.gdx.tools.texturepacker.TexturePacker
import com.badlogic.gdx.tools.texturepacker.TexturePackerFileProcessor
import groovy.util.Node
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
//        findMod("TS05_Marisa.jar"),
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

    tasks.register<ImageCopy>("resizeImages") {
        val src = "$resRoot/images"
        from("$src/1024") {
            include("*.png")
            filterResize(512, 512)
            into("512")
        }
        from("$src/energy") {
            include("energy_orb.png")
            rename { _ -> "card_small_orb.png" }
            filterResize(22, 22)
            into("512")
        }
        from("$src/energy") {
            include("energy_orb.png")
            into("1024")
        }
        into("$resOut/images")
    }

    val shrinkCards = tasks.register<ImageCopy>("shrinkCards") {
        // Resize all _p cards to be smaller
        from("$resRoot/images/cards") {
            // Real size is 250x190, but this is going from the base game card atlas sizes
            filterResize(248, 186)
            changeSuffix("_p.png", ".png")
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

    val resizePowers = tasks.register<ImageCopy>("resizePowerImages") {
        from("$resRoot/images/powers") {
            include("*.png")
            filterResize(48, 48)
            into("48")
        }
        from("$resRoot/images/powers") {
            include("*.png")
            filterResize(128, 128)
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

    val relicOutlines = tasks.register<ImageCopy>("relicOutlines") {
        from("$resRoot/images/relics") {
            changeSuffix(".png", "_o.png")
            filter(OutlineFilter::class)
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

tasks.register<IntellijJarRun>("genIntelliJRuns") {
    description = "Generate run configurations for IntelliJ"
    runName.set("Run MTS")
    jarPath.set(mtsJar)
    parameters.set("--mods basemod,stslib,grackle")
    workingDirectory.set(stsHome)
    alternateJre.set(file("$stsHome/jre"))
    beforeRunTask.set("installJar")
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


//------------------------
// Begin Library-ish Code
//------------------------

// This is imperative code, implementing build tasks for the build script above.
// Eventually it could be moved to buildSrc or a plugin, but for now it's here.
//

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
    @get:Input
    val maxWidth: Property<Int> = project.objects.property()
    @get:Input
    val maxHeight: Property<Int> = project.objects.property()
    @get:Input
    val grid: Property<Boolean> = project.objects.property()
    @get:InputDirectory
    val inputDir: Property<File> = project.objects.property()
    @get:OutputFile
    abstract val outputPath: Property<File>
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

open class ImageCopy : Copy() {
    init {
        group = "images"
        filteringCharset = ImageFilter.BINARY_CHARSET
    }

    fun CopySpec.filterResize(width: Int, height: Int) {
        filter(ResizeFilter::class, "width" to width, "height" to height)
    }

    /**
     * Include all files ending with `suffix` and change the output names to end with `newEnd`.
     * @param suffix Suffix of existing files
     * @param newEnd Suffix to replace with
     */
    fun CopySpec.changeSuffix(suffix: String, newEnd: String) {
        include("*$suffix")
        rename { n -> n.removeSuffix(suffix) + newEnd }
    }
}

abstract class IntelliJRun : DefaultTask() {
    @get:Input
    val runName: Property<String> = project.objects.property()
    @get:Input
    val taskType: Property<String> = project.objects.property()
    @get:Input
    val isDefault: Property<Boolean> = project.objects.property()
    @get:Input
    val options: MutableMap<String, String> = HashMap()

    /**
     * Name of Gradle task to execute before running this run configuration.
     */
    @get:Input
    val beforeRunTask: Property<String> = project.objects.property()

    init {
        description = "Generate IntelliJ run configuration"
        isDefault.convention(false)
        beforeRunTask.convention("")
        outputs.file { "${project.rootDir}/.idea/runConfigurations/${runName.get()}.xml" }
    }

    @TaskAction
    fun writeFile() {
        val xml = generate()
        for (f in outputs.files) {
            val writer = groovy.xml.XmlNodePrinter(f.printWriter())
            writer.print(xml)
        }
    }

    open fun generate(): Node {
        val root = Node(null, "component", mapOf("name" to "ProjectRunConfigurationManager"))
        val cfg = Node(root, "configuration",
                mapOf("default" to isDefault.get(), "name" to runName.get(), "type" to taskType.get()))
        for (opt in options) {
            Node(cfg, "option", mapOf("name" to opt.key, "value" to opt.value))
        }
        val v2 = Node(cfg, "method", mapOf("v" to "2"))
        beforeRunTask.orNull?.run {
            Node(v2, "option", mapOf(
                    "name" to "Gradle.BeforeRunTask",
                    "enabled" to "true",
                    "tasks" to this,
                    "externalProjectPath" to "\$PROJECT_DIR\$",
                    "vmOptions" to "",
                    "scriptParameters" to ""
            ))
        }
        addV2Options(v2)
        return root
    }

    open fun addV2Options(node: Node) {}


    fun unixPath(s: String): String {
        return s.replace('\\', '/')
    }
}

open class IntellijJarRun : IntelliJRun() {
    @get:Input
    val jarPath: Property<File> = project.objects.property()
    @get:Input
    val parameters: Property<String> = project.objects.property()
    @get:Input
    val workingDirectory: Property<String> = project.objects.property()
    @get:Input
    val alternateJre: Property<File> = project.objects.property()

    init {
        taskType.convention("JarApplication")
        parameters.convention("")
        workingDirectory.convention(null as String?)
        alternateJre.convention(null as File?)
    }

    override fun generate(): Node {
        options["JAR_PATH"] = unixPath(jarPath.get().toString())
        options["PROGRAM_PARAMETERS"] = parameters.get()
        workingDirectory.orNull?.run {
            options["WORKING_DIRECTORY"] = unixPath(this)
        }
        alternateJre.orNull?.run {
            options["ALTERNATIVE_JRE_PATH_ENABLED"] = "true"
            options["ALTERNATIVE_JRE_PATH"] = unixPath(this.absolutePath)
        }
        return super.generate()
    }
}
