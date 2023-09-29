package net.bindernews.accesscheck

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.reflect.HasPublicType
import org.gradle.api.reflect.TypeOf
import org.gradle.api.tasks.TaskProvider

class AccessCheckPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.extensions.create("accessCheck", AccessCheckProjectExtensionImpl::class.java, target)
    }
}


interface AccessCheckProjectExtension {
    fun accessCheck(name: String, a: Action<AccessCheckTask>): TaskProvider<AccessCheckTask>
}

internal abstract class AccessCheckProjectExtensionImpl (
    private val project: Project
) : AccessCheckProjectExtension, HasPublicType {

    override fun accessCheck(name: String, a: Action<AccessCheckTask>): TaskProvider<AccessCheckTask> {
        return project.tasks.register(name, AccessCheckTask::class.java, a)
    }

    override fun getPublicType(): TypeOf<*> {
        return TypeOf.typeOf(AccessCheckProjectExtension::class.java)
    }
}
