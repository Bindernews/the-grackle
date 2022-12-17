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
import java.io.Serializable
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
    ))
    // Prefer the patched version so that we get better debugging results in IntelliJ
    // but fall back to the normal desktop-1.0.jar if it's not available.
    val depDesktopModded = file("$projectDir/lib/desktop-1.0-modded.jar")
    if (depDesktopModded.exists()) {
        implementation(files(depDesktopModded))
    } else {
        implementation(files("$stsHome/desktop-1.0.jar"))
    }
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
}

tasks.register("packagePatchedJar") {
    doLast {
        exec {
            executable = findJavaExe("$stsHome/jre")
            workingDir = file(stsHome)
            args("-jar", mtsJar, "--mods", "basemod,stslib", "--package", "--close-when-finished")
        }
        copy {
            from("$stsHome/desktop-1.0-modded.jar")
            into("$projectDir/lib")
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

//---------------------
// IntelliJ Run Config
//---------------------

// All this code is about generating intellij run configurations in a way works with the DSL.

interface V2Option : Map<String, String>, Serializable {
    companion object {
        /**
         * Returns a V2Option that will run the named Gradle task before launching the run configuration.
         */
        fun beforeLaunch(task: String): V2Option {
            return from(mapOf(
                    "name" to "Gradle.BeforeRunTask",
                    "enabled" to "true",
                    "tasks" to task,
                    "externalProjectPath" to "\$PROJECT_DIR\$",
                    "vmOptions" to "",
                    "scriptParameters" to "",
            ))
        }

        /**
         * Helper function to create a V2Option from a string map.
         */
        fun from(m: Map<String, String>): V2Option {
            return V2OptionData(m)
        }
    }
}

fun unixPath(s: String): String {
    return s.replace('\\', '/')
}

data class V2OptionData(val m: Map<String, String>) : Map<String, String> by m, V2Option

interface IntellijRunSpec : Serializable {
    var name: String
    var taskType: String
    var isDefault: Boolean
    val options: MutableMap<String, String>
    val v2Options: MutableList<V2Option>

    fun option(key: String, value: String)

    fun generate(): Node

    companion object {
        /**
         * Default generate function.
         */
        fun defaultGenerate(spec: IntellijRunSpec): Node {
            val root = Node(null, "component", mapOf("name" to "ProjectRunConfigurationManager"))
            val cfg = Node(root, "configuration", mapOf(
                    "default" to spec.isDefault, "name" to spec.name, "type" to spec.taskType))
            for (opt in spec.options) {
                Node(cfg, "option", mapOf("name" to opt.key, "value" to opt.value))
            }
            val v2 = Node(cfg, "method", mapOf("v" to "2"))
            for (opt in spec.v2Options) {
                Node(v2, "option", opt)
            }
            return root
        }
    }
}

interface IntellijRunJarSpec {
    fun jarPath(f: File)
    fun parameters(s: String)
    fun workingDirectory(s: String)
    fun alternateJre(f: File)
}

data class IntellijRunData(
        override var name: String = "",
        override var taskType: String = "",
        override var isDefault: Boolean = false,
        override val options: MutableMap<String, String> = HashMap(),
        override val v2Options: MutableList<V2Option> = ArrayList(),
) : IntellijRunSpec {
    override fun generate(): Node = IntellijRunSpec.defaultGenerate(this)

    override fun option(key: String, value: String) {
        options[key] = value
    }
}

data class IntellijRunJarData(
        val parent: IntellijRunSpec
) : IntellijRunSpec by parent, IntellijRunJarSpec {
    init {
        taskType = "JarApplication"
    }

    override fun jarPath(f: File) = option("JAR_PATH", unixPath(f.absolutePath))
    override fun parameters(s: String) = option("PROGRAM_PARAMETERS", s)
    override fun workingDirectory(s: String) = option("WORKING_DIRECTORY", unixPath(s))
    override fun alternateJre(f: File) {
        options["ALTERNATIVE_JRE_PATH_ENABLED"] = "true"
        options["ALTERNATIVE_JRE_PATH"] = unixPath(f.absolutePath)
    }
}

fun IntellijRunSpec.jar(cf: Action<in IntellijRunJarSpec>) {
    cf.execute(IntellijRunJarData(this))
}


abstract class IntellijRun : DefaultTask() {
    @get:Input
    val configs: ListProperty<IntellijRunSpec> = project.objects.listProperty(IntellijRunSpec::class)

    init {
        description = "Generate IntelliJ run configuration"
        configs.convention(ArrayList())
    }

    @TaskAction
    fun writeFile() {
        for (spec in configs.get()) {
            val f = project.file(pathFor(spec))
            val writer = groovy.xml.XmlNodePrinter(f.printWriter())
            writer.print(spec.generate())
        }
    }

    fun add(cf: Action<in IntellijRunSpec>) {
        val spec = IntellijRunData()
        cf.execute(spec)
        configs.add(spec)
        outputs.file(pathFor(spec))
    }

    private fun pathFor(spec: IntellijRunSpec): String {
        return "${project.rootDir}/.idea/runConfigurations/${spec.name}.xml"
    }
}