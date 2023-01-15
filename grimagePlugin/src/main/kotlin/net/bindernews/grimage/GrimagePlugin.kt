package net.bindernews.grimage

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.reflect.HasPublicType
import org.gradle.api.reflect.TypeOf
import org.gradle.kotlin.dsl.create

class GrimagePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create<GrimagePluginExtensionImpl>("grimage")
    }
}

interface GrimagePluginExtension {
    fun packImages(cfg: Action<PackTextureSpec>): PackTextureSpec
}

internal abstract class GrimagePluginExtensionImpl(
        private val project: Project
) : GrimagePluginExtension, HasPublicType {
    override fun packImages(cfg: Action<PackTextureSpec>): PackTextureSpec {
        return PackTextureSpec.Impl(project).apply { cfg.execute(this) }
    }

    override fun getPublicType(): TypeOf<*> {
        return TypeOf.typeOf(GrimagePluginExtension::class.java)
    }
}
