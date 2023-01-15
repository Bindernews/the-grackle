package net.bindernews.grimage

import net.bindernews.grimage.ImageFilter.Companion.makeBuffered
import java.awt.image.BufferedImage
import java.io.Reader


class ResizeOp(var width: Int = -1, var height: Int = -1) : ImageTransformer {
    override fun transform(src: BufferedImage): BufferedImage {
        val nWidth = if (width == -1) src.width else width
        val nHeight = if (height == -1) src.height else height
        return src.getScaledInstance(nWidth, nHeight, BufferedImage.SCALE_SMOOTH).makeBuffered()
    }

    class Rd(`in`: Reader) : ImageFilter(`in`) {
        var width: Int = -1
        var height: Int = -1

        override fun processImage(src: BufferedImage): BufferedImage {
            return ResizeOp(width, height).transform(src)
        }
    }
}

