import com.badlogic.gdx.tools.texturepacker.TexturePacker
import com.badlogic.gdx.tools.texturepacker.TexturePackerFileProcessor
import groovy.util.Node
import org.apache.tools.ant.taskdefs.condition.Os
import org.apache.tools.ant.util.ReaderInputStream
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.awt.Dimension
import java.awt.Image
import java.awt.Rectangle
import java.awt.image.BufferedImage
import java.awt.image.ImageObserver
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.FilenameFilter
import java.io.FilterReader
import java.io.Reader
import java.io.Serializable
import java.io.StringReader
import java.util.concurrent.CountDownLatch
import javax.imageio.ImageIO

val modName = "TheGrackle"
group = "io.bindernews.thegrackle"
version = "0.2-SNAPSHOT"

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
    kotlin("jvm") version "1.4.10"
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
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
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
}



run {
    val resRoot = "$projectDir/src/main/resources/$RES_DIR"
    val resOut = "$buildDir/resources/main/$RES_DIR"
    val packerTmp = "$buildDir/generated/images"

    val tResizeImages = tasks.register<ImageCopy>("resizeImages") {
        val src = "$resRoot/images"
        from("$src/1024") {
            include("*.png")
            filterResize(512, 512)
        }
        from("$src/energy") {
            include("energy_orb.png")
            rename { _ -> "card_small_orb.png" }
            filterResize(22, 22)
        }
        // Make orb in top-left corner
        from("$src/energy") {
            include("energy_orb.png")
            rename { _ -> "card_default_gray_orb.png" }
            filter(MoveEnergyOrbFilter::class)
        }
        into("$resOut/images/512")
    }

    val tCopyEnergy = tasks.register<ImageCopy>("copyEnergy") {
        from("$resRoot/images/energy")
        include("energy_orb.png")
        into("$resOut/images/1024")
    }

    val tMaskCards = tasks.register<ImageCopy>("maskCards") {
        val cardsRoot = "$resRoot/images/cards"
        from("$cardsRoot/attack") {
            filterMask(file("$cardsRoot/attack/attack_mask.png"))
        }
        from("$cardsRoot/power") {
            filterMask(file("$cardsRoot/power/power_mask.png"))
        }
        from("$cardsRoot/skill") {
        }
        include("*_p.png")
        into("$resOut/images/cards")
    }

    val tPackCards = tasks.register<ImageCopy>("packCards") {
        // Resize all _p cards to be smaller
        // Real size is 250x190, but this is going from the base game card atlas sizes
        dependsOn(tMaskCards)
        from("$resOut/images/cards")
        filterResize(248, 186)
        changeSuffix("_p.png", ".png")
        into("$packerTmp/cards")

        packImages {
            grid = true
            source("$packerTmp/cards")
            output("$resOut/images/cards/cards.atlas")
        }.forTask(this)
    }

    val tPackIcons = tasks.register("packIcons") {
        group = "images"
        packImages {
            source("$resRoot/images/icons")
            output("$resOut/images/icons.atlas")
        }.forTask(this)
    }

    val tResizePowers = tasks.register<ImageCopy>("resizePowerImages") {
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

        packImages {
            source("$packerTmp/powers")
            output("$resOut/images/powers/powers.atlas")
        }.forTask(this)
    }

    val tRelicOutlines = tasks.register<ImageCopy>("relicOutlines") {
        from("$resRoot/images/relics") {
            changeSuffix(".png", "_o.png")
            filter(OutlineFilter::class)
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
    doLast {
        exec {
            executable = findJavaExe("$stsHome/jre")
            workingDir = file(stsHome)
            args("-jar", mtsJar, "--mods", "basemod,stslib,grackle", "--out-jar", "--close-when-finished")
        }
        copy {
            from("$stsHome/desktop-1.0-patched.jar")
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

        fun Image.makeBuffered(): BufferedImage {
            if (this is BufferedImage) {
                return this
            }

            val imageReady = CountDownLatch(1)
            val observer = ImageObserver { _, flags, _, _, _, _ ->
                val done = flags.and(ImageObserver.ALLBITS.or(ImageObserver.ABORT)) > 0
                if (done) {
                    imageReady.countDown()
                }
                done
            }

            var w = getWidth(observer)
            var h = getHeight(observer)
            if (w == -1 || h == -1) {
                imageReady.await()
                w = getWidth(null)
                h = getHeight(null)
            }
            val buf = BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)
            val g = buf.createGraphics()
            g.drawImage(this, 0, 0, w, h, null)
            g.dispose()
            return buf
        }
    }

    private val outReader: StringReader by lazy { loadImage() }
    val transformers = ArrayList<Transformer<BufferedImage, BufferedImage>>()

    override fun read(cbuf: CharArray, off: Int, len: Int): Int {
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
}

class ResizeFilter(`in`: Reader) : ImageFilter(`in`) {
    var width: Int = -1
    var height: Int = -1

    override fun processImage(src: BufferedImage): BufferedImage {
        val nWidth = if (width == -1) src.width else width
        val nHeight = if (height == -1) src.height else height
        return src.getScaledInstance(nWidth, nHeight, BufferedImage.SCALE_SMOOTH).makeBuffered()
    }
}

class MaskFilter(`in`: Reader) : ImageFilter(`in`) {
    var mask: File? = null

    override fun processImage(src: BufferedImage): BufferedImage {
        val maskImg = ImageIO.read(mask!!)
        for (y in 0 until src.height) {
            for (x in 0 until src.width) {
                if (maskImg.alphaRaster.getSample(x, y, 0) == 0) {
                    src.alphaRaster.setSample(x, y, 0, 0)
                }
            }
        }
        return src
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


interface PackTextureSpec : Serializable {
    var maxWidth: Int
    var maxHeight: Int
    var grid: Boolean
    var inputDir: File
    var output: File?
    var filter: FilenameFilter

    fun filter(f: (File, String) -> Boolean) {
        this.filter = FilenameFilter(f)
    }

    fun source(path: Any)
    fun output(path: Any)

    fun forTask(t: Task) {
        t.outputs.file(this.output!!)
        t.doLast("TexturePackSpec.pack") { pack() }
    }

    fun pack(): Boolean {
        val outputRoot = output!!.parentFile
        val packName = output!!.name
        val cfg = TexturePacker.Settings()
        cfg.maxHeight = maxHeight
        cfg.maxWidth = maxWidth
        cfg.grid = grid
        cfg.combineSubdirectories = true
        cfg.filterMag = com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
        cfg.filterMin = cfg.filterMag

        val didWork = if (TexturePacker.isModified("" + inputDir, "" + outputRoot, packName)) {
            try {
                val processor = TexturePackerFileProcessor(cfg, packName)
                // Sort input files by name to avoid platform-dependent atlas output changes.
                processor.setComparator(Comparator<File> { file1, file2 -> file1.name.compareTo(file2.name) })
                processor.setInputFilter(filter)
                processor.process(inputDir, outputRoot)
            } catch (ex: Exception) {
                throw RuntimeException("Error packing images.", ex)
            }
            true
        } else {
            false
        }
        return didWork
    }

    open class Impl(private val project: Project) : PackTextureSpec {
        override var maxWidth: Int = 4096
        override var maxHeight: Int = 4096
        override var grid: Boolean = false
        override var inputDir: File = File("")
        override var output: File? = null
        override var filter: FilenameFilter = FilenameFilter { _, _ -> true }

        override fun source(path: Any) {
            inputDir = project.file(path)
        }

        override fun output(path: Any) {
            output = project.file(path)
        }
    }
}

fun packImages(cf: Action<in PackTextureSpec>): PackTextureSpec {
    return PackTextureSpec.Impl(project).apply { cf.execute(this) }
}

open class ImageCopy : Copy() {
    init {
        group = "images"
        filteringCharset = ImageFilter.BINARY_CHARSET
    }

    fun CopySpec.filterResize(width: Int, height: Int) {
//        println("filterResize w = $width, h = $height")
        filter(ResizeFilter::class, "width" to width, "height" to height)
    }

    fun CopySpec.filterMask(file: File) {
        filter(MaskFilter::class, "mask" to file)
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