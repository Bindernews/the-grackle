package net.bindernews.grimage

import org.gradle.api.Action
import org.gradle.api.Task
import org.gradle.api.file.CopySpec

fun Task.packImages(cfg: Action<in PackTextureSpec>): PackTextureSpec {
    val pts = PackTextureSpec.Impl(this.project)
    cfg.execute(pts)
    pts.forTask(this)
    return pts
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
