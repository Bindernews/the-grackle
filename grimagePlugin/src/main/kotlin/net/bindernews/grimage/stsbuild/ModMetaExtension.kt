package net.bindernews.grimage.stsbuild

import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import java.io.File
import java.io.FileNotFoundException

class ModMetaExtension(
    private val project: Project
) : NamedDomainObjectContainer<ModMeta> by project.container(ModMeta::class.java) {

    /**
     * An ordered list of paths to search for mods.
     *
     * Paths containing `$WORKSHOP_ID$` will have that part of the search path
     * replaced with the mod's workshop ID, used to accurately locate mods
     * from the Steam workshop.
     */
    val searchPaths: ListProperty<File> = project.objects.listProperty(File::class.java)

    /**
     * The directory containing Slay the Spire's `desktop-1.0.jar` and the `mods/` directory.
     */
    val stsHome: DirectoryProperty = project.objects.directoryProperty()

    /**
     * Root of the StS Steam Workshop, where all the Steam Workshop mod folders are stored.
     * If not `null`, [findMod] will search for the mod using the workshop ID.
     */
    var workshopRoot: File? = null

    /**
     * Update [stsHome] and [workshopRoot] based on the provided Steam directory.
     *
     * @param steamDir Steam directory containing `common/` and `workshop/`, among other files and directories
     */
    fun useSteam(steamDir: File) {
        val macPath = "/SlayTheSpire.app/Contents/Resources"
        stsHome.set(File("$steamDir/common/SlayTheSpire" + if (Os.isFamily(Os.FAMILY_MAC)) macPath else ""))
        workshopRoot = steamDir.resolve("workshop/content/646570/")
    }

    fun searchStSHome() {
        searchPaths.add(File("$stsHome/mods"))
    }

    /**
     * Creates [ModMeta] entries for `ModTheSpire.jar`, `BaseMod.jar`, and `StSLib.jar`.
     */
    fun addDefaultMods() {
        create("ModTheSpire.jar") {
            workshopId = "1605060445"
        }
        create("BaseMod.jar") {
            workshopId = "1605833019"
        }
        create("StSLib.jar") {
            workshopId = "1609158507"
        }
    }

    /**
     * Find a mod by searching the configured search path and optionally the steam workshop.
     *
     * The search path is searched in order, so the earliest found mod is returned.
     *
     * @param name the initial name provided when calling [create] or [register]
     * @throws FileNotFoundException if the mod cannot be found
     */
    fun findMod(name: String): File {
        val meta = getByName(name)
        val workshopIdStr = meta.workshopId.takeIf { it != "" } ?: "__UNDEFINED__"
        val paths = searchPaths.get().toMutableList()
        workshopRoot?.let { paths.add(File(it, workshopIdStr)) }
        // Search all paths and all alternate names for the mod.
        for (p in paths) {
            meta.searchIn(p)?.let { return it }
        }
        throw FileNotFoundException(name)
    }

    /**
     * Takes multiple mods and returns an array, suitable for passing to [Project.files].
     */
    fun findMods(vararg names: String): Array<File> = names.map { findMod(it) }.toTypedArray()

    /**
     * Takes a list of mod names and returns [File]s representing that mod in the local Steam workshop,
     * skipping any that can't be found.
     *
     * This is useful for copying dependencies locally, thus locking in the versions.
     */
    fun findWorkshopMods(vararg names: String): Array<File> {
        return names.mapNotNull { name ->
            val meta = getByName(name)
            val workshopDir = File(workshopRoot, meta.workshopId)
            if (workshopDir.isDirectory) {
                meta.searchIn(workshopDir)
            } else {
                null
            }
        }.toTypedArray()
    }

    companion object {
        @JvmStatic val DEFAULT_MODS = arrayOf("ModTheSpire.jar", "BaseMod.jar", "StSLib.jar")
    }
}