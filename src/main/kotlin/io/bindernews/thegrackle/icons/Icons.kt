package io.bindernews.thegrackle.icons

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon
import com.evacipated.cardcrawl.mod.stslib.icons.CustomIconHelper
import io.bindernews.thegrackle.GrackleMod

val iconAtlas by lazy { TextureAtlas(GrackleMod.CO.RES_IMAGES + "/icons.atlas") }

fun registerIcons() {
    GrackleMod.log.debug(GrackleMod.CO.REG_START, "icons")
    listOf<AbstractCustomIcon>(
        MusicNoteIcon
    ).forEach(CustomIconHelper::addCustomIcon)
    GrackleMod.log.debug(GrackleMod.CO.REG_END, "icons")
}

object MusicNoteIcon : AbstractCustomIcon(
    GrackleMod.makeId("MusicNote"),
    iconAtlas.findRegion("musical_note")
)
