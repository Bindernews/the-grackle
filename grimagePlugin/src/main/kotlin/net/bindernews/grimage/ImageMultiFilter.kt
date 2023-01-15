package net.bindernews.grimage

import org.gradle.api.Action
import org.gradle.api.Transformer
import java.awt.image.BufferedImage
import java.io.Reader
import java.util.function.Supplier

interface ImageTransformer : Transformer<BufferedImage, BufferedImage>

class ImageMultiFilter(`in`: Reader) : ImageFilter(`in`) {
    private val transformers = ArrayList<Supplier<ImageTransformer>>()
    var configure: Action<ImageMultiFilter> = Action{}

    fun add(t: ImageTransformer): ImageMultiFilter {
        transformers.add { t }
        return this
    }

    fun add(t: Supplier<ImageTransformer>): ImageMultiFilter {
        transformers.add(t)
        return this
    }

    override fun processImage(src: BufferedImage): BufferedImage {
        configure.execute(this)
        var dst = src
        for (tr in transformers) {
            dst = tr.get().transform(dst)
        }
        return dst
    }
}