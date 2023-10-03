package net.bindernews.grackle.stance

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.input.InputHelper
import com.megacrit.cardcrawl.stances.AbstractStance
import net.bindernews.grackle.GrackleMod
import net.bindernews.grackle.GrackleMod.CO.RES_IMAGES
import net.bindernews.grackle.relics.BorrowedMoment
import net.bindernews.sts.slots.SlotHelper

abstract class AbstractGrackleStance : AbstractStance() {

    open val slots: SlotHelper = MySlots(defaultTexBright, defaultTexDark, defaultTexOverlay)

    /**
     * Timer
     */
    var flashTimer: Float = 0.0f

    /**
     * Called to trigger the technique.
     */
    abstract fun technique()

    override fun onEnterStance() {
        if (AbstractDungeon.player.hasRelic(BorrowedMoment.ID)) {
            slots.count = 4
        } else {
            slots.count = 3
        }
    }

    override fun onPlayCard(card: AbstractCard) {
        if (card.type == AbstractCard.CardType.SKILL && slots.spendSlot()) {
            technique()
        }
    }

    override fun update() {
//        this.hitbox2.update()
//        if (this.flashTimer > 1.0f) {
//            this.flashTimer -= Gdx.graphics.deltaTime
//        }
//        for (i in 0..2) {
//            if (this.animAlphaBySlot.get(i) > 0.0f) {
//                val var10000: FloatArray = this.animAlphaBySlot
//                var10000[i] -= Gdx.graphics.deltaTime
//                if (this.animAlphaBySlot.get(i) < 0.2f) {
//                    this.useBrightTexture.get(i) = false
//                }
//                if (this.animAlphaBySlot.get(i) < 0.0f) {
//                    this.animAlphaBySlot.get(i) = 0.0f
//                }
//            }
//        }
        if (this.flashTimer < 1.0f) {
            this.flashTimer = 1.0f
        }
    }

    override fun render(sb: SpriteBatch) {
        super.render(sb)
        slots.render(sb)
    }


    companion object {
        val slotOrbSize = Vector2(75f, 75f)

        val defaultTexBright by lazy {
            TextureRegion(GrackleMod.loadTexture("$RES_IMAGES/ui/crushingBright.png"))
        }
        val defaultTexDark by lazy {
            TextureRegion(GrackleMod.loadTexture("$RES_IMAGES/ui/crushing.png"))
        }
        val defaultTexOverlay by lazy {
            TextureRegion(GrackleMod.loadTexture("$RES_IMAGES/ui/whiteOverlay.png"))
        }
    }

    class MySlots(texBright: TextureRegion, texDark: TextureRegion, texOverlay: TextureRegion) : SlotHelper() {
        init {
            this.texBright = texBright
            this.texDark = texDark
            this.texOverlay = texOverlay
        }

        override fun render(sb: SpriteBatch) {
            super.render(sb)
            if (hoveredHitbox != -1) {
                val scale = Settings.scale
//                val tipX = if (InputHelper.mX)

            }
        }

        override fun calculateSlotPositions(count: Int): FloatArray {
            val scale = Settings.scale
            val origin = Vector2(AbstractDungeon.player.drawX, AbstractDungeon.player.drawY)
            origin.y += 380f * scale
            val scaleX = (slotOrbSize.x + 10f) * scale

            val unitOffsetX = -(count / 2).toFloat() - if (count % 2 == 0) { 0.5f } else { 0.0f }
            val out = FloatArray(count * 2)
            for (i in 0 until count) {
                // Find x-position in unit scale (e.g. -2 to +2) then multiply by scaleX to convert to pixels
                val offX = (i.toFloat() + unitOffsetX) * scaleX
                out[i] = MathUtils.round(offX).toFloat()
                out[i + 1] = origin.y
            }
            return out
        }
    }

}