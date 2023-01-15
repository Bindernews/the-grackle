package net.bindernews.grimage

import java.awt.image.BufferedImage

class OutlineOp : ImageTransformer {
    override fun transform(src: BufferedImage): BufferedImage {
        for (y in 0 until src.height) {
            for (x in 0 until src.width) {
                val c = src.data.getSample(x, y, 3)
                if (c > 0) {
                    src.setRGB(x, y, 0xffffff or c.shl(24))
                } else {
                    src.setRGB(x, y, 0)
                }
            }
        }
        return src
    }
}
