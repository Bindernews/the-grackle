package net.bindernews.grackle.icons

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon
import com.evacipated.cardcrawl.mod.stslib.icons.CustomIconHelper
import net.bindernews.grackle.GrackleMod

val iconAtlas by lazy { TextureAtlas(GrackleMod.CO.RES_IMAGES + "/icons.atlas") }

fun registerIcons() {
    GrackleMod.log.traceEntry()
    listOf<AbstractCustomIcon>(
        MusicNoteIcon
    ).forEach(CustomIconHelper::addCustomIcon)
    GrackleMod.log.traceExit()
}

object MusicNoteIcon : AbstractCustomIcon(
    GrackleMod.makeId("MusicNote"),
    iconAtlas.findRegion("musical_note")
)
