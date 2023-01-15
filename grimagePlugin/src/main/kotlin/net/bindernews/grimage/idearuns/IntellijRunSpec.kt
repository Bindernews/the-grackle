package net.bindernews.grimage.idearuns

import groovy.util.Node
import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.listProperty
import java.io.File
import java.io.Serializable

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
