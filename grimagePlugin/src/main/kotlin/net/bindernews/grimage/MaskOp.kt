package net.bindernews.grimage

import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO


class MaskOp(var mask: File? = null) : ImageTransformer {
    private val maskImage: BufferedImage by lazy {
        ImageIO.read(Objects.requireNonNull(mask, "mask is null"))
    }

    override fun transform(src: BufferedImage): BufferedImage {
        for (y in 0 until src.height) {
            for (x in 0 until src.width) {
                if (maskImage.alphaRaster.getSample(x, y, 0) == 0) {
                    src.alphaRaster.setSample(x, y, 0, 0)
                }
            }
        }
        return src
    }
}

