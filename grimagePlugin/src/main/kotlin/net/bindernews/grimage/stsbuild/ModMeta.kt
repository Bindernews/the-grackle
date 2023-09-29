package net.bindernews.grimage.stsbuild

import org.gradle.api.Named
import java.io.Serializable

class ModMeta(private val name: String) : Named, Serializable {
    /**
     * List of file names for the mod, generally ending in `.jar`.
     */
    val fileNames: MutableList<String> = arrayListOf()
    /**
     * The mod's workshop ID, or `""` if it's not a workshop mod.
     */
    var workshopId: String = ""
        set(value) {
            field = value
            dirty = true
        }

    @Transient
    internal var dirty: Boolean = false

    init {
        if (name.endsWith(".jar")) {
            fileNames.add(name)
        }
    }

    override fun getName(): String = name
}