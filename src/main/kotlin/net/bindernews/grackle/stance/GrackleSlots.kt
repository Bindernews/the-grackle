package net.bindernews.grackle.stance

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.Hitbox
import net.bindernews.grackle.GrackleMod
import net.bindernews.sts.slots.SlotHelper

class GrackleSlots(
    val tipHeader: String,
    val tipBody: String,
) : SlotHelper() {

    private val hitbox = Hitbox(1f, 1f)

    init {
        this.texBright = defaultTexBright
        this.texDark = defaultTexDark
        this.texOverlay = defaultTexOverlay
    }

    override fun update() {
        super.update()
        hitbox.update()
    }

    override fun render(sb: SpriteBatch) {
        super.render(sb)
        hitbox.render(sb)
        if (hitbox.hovered) {
            renderTip(tipHeader, tipBody)
        }
    }

    override fun calculateSlotPositions(count: Int): FloatArray {
        val scale = Settings.scale
        val scaleX = (slotOrbSize.x + 20f) * scale

        val unitOffsetX = -(count / 2).toFloat() - if (count % 2 == 0) { 0.5f } else { 0.0f }
        val offsetX = -40f
        val out = FloatArray(count * 2)
        for (i in 0 until count) {
            // Find x-position in unit scale (e.g. -2 to +2) then multiply by scaleX to convert to pixels
            val offX = (i.toFloat() + unitOffsetX) * scaleX + offsetX
            out[i * 2] = MathUtils.round(offX).toFloat()
            out[i * 2 + 1] = 340f * scale
        }

        // Update hitbox position and size
        val hbPos = Vector2(out[0], out[1])
        val hbSize = Vector2(out[count * 2 - 2], out[count * 2 - 1]).sub(hbPos)
        hitbox.resize(hbSize.x, hbSize.y)
        hitbox.translate(hbPos.x, hbPos.y)

        // Return
        return out
    }

    companion object {
        val slotOrbSize = Vector2(75f, 75f)

        val defaultTexBright by lazy {
            TextureRegion(GrackleMod.loadTexture("${GrackleMod.CO.RES_IMAGES}/ui/crushingBright.png"))
        }
        val defaultTexDark by lazy {
            TextureRegion(GrackleMod.loadTexture("${GrackleMod.CO.RES_IMAGES}/ui/crushing.png"))
        }
        val defaultTexOverlay by lazy {
            TextureRegion(GrackleMod.loadTexture("${GrackleMod.CO.RES_IMAGES}/ui/whiteOverlay.png"))
        }
    }
}