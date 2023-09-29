package net.bindernews.grimage.ext

import java.awt.Graphics2D
import java.awt.Image
import java.awt.Rectangle
import java.awt.image.BufferedImage

fun Graphics2D.drawImage(img: Image, area: Rectangle) {
    drawImage(img, area.x, area.y, area.width, area.height, null)
}

inline fun BufferedImage.withGraphics(block: (g: Graphics2D) -> Unit) {
    val g = createGraphics()
    try {
        block(g)
    } finally {
        g.dispose()
    }
}