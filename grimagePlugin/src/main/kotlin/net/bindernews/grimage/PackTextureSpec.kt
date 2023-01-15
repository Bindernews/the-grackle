package net.bindernews.grimage

import com.badlogic.gdx.tools.texturepacker.TexturePacker
import com.badlogic.gdx.tools.texturepacker.TexturePackerFileProcessor
import org.gradle.api.Project
import org.gradle.api.Task
import java.io.File
import java.io.FilenameFilter
import java.io.Serializable

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
                processor.setComparator { file1, file2 -> file1.name.compareTo(file2.name) }
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