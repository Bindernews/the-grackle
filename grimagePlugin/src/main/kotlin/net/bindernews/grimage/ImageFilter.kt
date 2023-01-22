package net.bindernews.grimage

import org.apache.tools.ant.util.ReaderInputStream
import java.awt.Image
import java.awt.image.BufferedImage
import java.awt.image.ImageObserver
import java.io.*
import java.util.concurrent.CountDownLatch
import javax.imageio.ImageIO

abstract class ImageFilter(`in`: Reader) : FilterReader(`in`) {
    companion object {
        const val BINARY_CHARSET = "ISO-8859-1"

        /**
         * Extension function that turns a generic [Image] into a [BufferedImage].
         * If the object is already a [BufferedImage], it returns itself.
         */
        fun Image.makeBuffered(): BufferedImage {
            if (this is BufferedImage) {
                return this
            }

            val imageReady = CountDownLatch(1)
            val observer = ImageObserver { _, flags, _, _, _, _ ->
                val done = flags.and(ImageObserver.ALLBITS.or(ImageObserver.ABORT)) > 0
                if (done) {
                    imageReady.countDown()
                }
                done
            }

            var w = getWidth(null)
            var h = getHeight(null)
            if (w == -1 || h == -1) {
                // Trigger observer
                getHeight(observer)
                // Wait for observer
                imageReady.await()
                w = getWidth(null)
                h = getHeight(null)
            }
            val buf = BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)
            val g = buf.createGraphics()
            g.drawImage(this, 0, 0, w, h, null)
            g.dispose()
            return buf
        }
    }

    private val outReader: StringReader by lazy { loadImage() }

    override fun read(cbuf: CharArray, off: Int, len: Int): Int {
        return outReader.read(cbuf, off, len)
    }

    private fun loadImage(): StringReader {
        val src = makeRGBA8(ImageIO.read(ReaderInputStream(`in`, Charsets.ISO_8859_1)))
        val outBuf = ByteArrayOutputStream()
        ImageIO.write(processImage(src), "png", outBuf)
        return StringReader(outBuf.toString(BINARY_CHARSET))
    }

    private fun makeRGBA8(img: BufferedImage): BufferedImage {
        if (img.type == BufferedImage.TYPE_INT_ARGB) {
            return img
        }
        val img2 = BufferedImage(img.width, img.height, BufferedImage.TYPE_INT_ARGB)
        val g = img2.createGraphics()
        g.drawImage(img, 0, 0, null)
        g.dispose()
        return img2
    }

    abstract fun processImage(src: BufferedImage): BufferedImage
}
