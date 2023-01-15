package net.bindernews.grimage

import org.gradle.api.Action
import org.gradle.api.file.CopySpec
import org.gradle.api.tasks.Copy
import org.gradle.work.DisableCachingByDefault

@DisableCachingByDefault(because = "Not worth caching")
open class ImageCopy : Copy() {
    init {
        filteringCharset = ImageFilter.BINARY_CHARSET
    }

    fun CopySpec.filterImage(cfg: Action<ImageMultiFilter>): CopySpec {
        return filter(mapOf("configure" to cfg), ImageMultiFilter::class.java)
    }

    fun CopySpec.filterImage(transform: ImageTransformer): CopySpec {
        return filterImage { add(transform) }
    }
}