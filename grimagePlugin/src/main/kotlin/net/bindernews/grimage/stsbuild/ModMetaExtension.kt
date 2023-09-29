package net.bindernews.grimage.stsbuild

import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
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

    val workshopRoot: DirectoryProperty = project.objects.directoryProperty()

    /**
     * Add Slay the Spire's `mods` directory and all Steam's StS workshop directory to the search path.
     *
     * @param steamDir Steam directory containing `common/` and `workshop/`, among other files and directories
     */
    fun searchSteam(steamDir: File) {
        val macPath = "/SlayTheSpire.app/Contents/Resources"
        stsHome.set(File("$steamDir/common/SlayTheSpire" + if (Os.isFamily(Os.FAMILY_MAC)) macPath else ""))
        searchPaths.add(stsHome.get().dir("mods").asFile)
        searchPaths.add(File("$steamDir/workshop/content/646570/\$WORKSHOP_ID\$"))
    }

    fun searchStSHome(stsHome: File) {
        searchPaths.add(File("$stsHome/mods"))
    }

    /**
     * Add [ModMeta] entries for `ModTheSpire.jar`, `BaseMod.jar`, and `StSLib.jar`.
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

    fun findMod(name: String): File {
        val meta = getByName(name)
        val workshopIdStr = meta.workshopId.takeIf { it != "" } ?: "__UNDEFINED__"
        val paths = searchPaths.get().map {
            val p = it.absolutePath.replace("\$WORKSHOP_ID\$", workshopIdStr)
            File(p)
        }
        // Search all paths and all alternate names for the mod.
        for (p in paths) {
            for (altName in meta.fileNames) {
                val child = p.resolve(altName)
                if (child.isFile) {
                    return child
                }
            }
        }
        throw FileNotFoundException(name)
    }
}