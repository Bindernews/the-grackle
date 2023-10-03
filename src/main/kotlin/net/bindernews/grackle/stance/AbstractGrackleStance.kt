package net.bindernews.grackle.stance

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.stances.AbstractStance
import net.bindernews.grackle.GrackleMod
import net.bindernews.grackle.GrackleMod.CO.RES_IMAGES
import net.bindernews.grackle.relics.BorrowedMoment

abstract class AbstractGrackleStance : AbstractStance() {

    /**
     * From left-to-right, which technique slots are enabled.
     */
    var slotEnabled = BooleanArray(3)

    /**
     * From left-to-right, data about how to display each slot.
     */
    var slotDisplay = FloatArray(3)

    /**
     * Left to right, positions of each slot relative to
     */
    var slotPositions = FloatArray(0)

    /**
     * Timer
     */
    var flashTimer: Float = 0.0f

    open val slotTexBright: TextureRegion = defaultTexBright
    open val slotTexDark: TextureRegion = defaultTexDark
    open val slotTexOverlay: TextureRegion = defaultTexOverlay

    /**
     * Called to trigger the technique.
     */
    abstract fun technique()

    override fun onEnterStance() {
        if (AbstractDungeon.player.hasRelic(BorrowedMoment.ID)) {
            setSlotCount(4)
        } else {
            setSlotCount(3)
        }
        slotEnabled.fill(true)
        slotDisplay.fill(1.0f)
        slotPositions = getSlotPositions(slotEnabled.size, Vector2(80f, 80f))
    }

    fun setSlotCount(slots: Int) {
        if (slotEnabled.size != slots) {
            slotEnabled = BooleanArray(slots)
            slotDisplay = FloatArray(slots)
        }
    }

    fun spendTechniqueCharge(): Boolean {
        for (i in slotEnabled.size - 1 downTo 0) {
            if (slotEnabled[i]) {
                slotEnabled[i] = false
                slotDisplay[i] = 0.4f
                return true
            }
        }
        return false
    }

    override fun onPlayCard(card: AbstractCard) {
        if (card.type == AbstractCard.CardType.SKILL && spendTechniqueCharge()) {
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
//        if (this.flashTimer < 1.0f) {
//            this.flashTimer = 1.0f
//        }
    }

    override fun render(sb: SpriteBatch) {
        super.render(sb)
        renderTechniqueUI(sb)
    }

    fun renderTechniqueUI(sb: SpriteBatch) {
        val origin = Vector2(AbstractDungeon.player.drawX, AbstractDungeon.player.drawY)
        val scale = Settings.scale
        val sz = slotOrbSize

        for (i in 0 until slotEnabled.size) {
            val orbPos = Vector2(slotPositions[i * 2], slotPositions[i * 2 + 1]).add(origin)
            val displayVal = slotDisplay[i]

            // Draw basic texture
            val tex0 = if (slotDisplay[i] >= 1f) { slotTexBright } else { slotTexDark }
            sb.setColor(1f, 1f, 1f, 0.9f)
            sb.draw(tex0, orbPos.x, orbPos.y, sz.x/2f, sz.y/2f, sz.x, sz.y, scale, scale, 0f)

            // Draw overlay effect texture
            if (displayVal > 0f) {
                val alpha = if (displayVal < 0.2f) {
                    Interpolation.linear.apply(0.0f, 0.95f, displayVal / 0.2f)
                } else {
                    Interpolation.linear.apply(0.0f, 0.95f, (displayVal - 0.2f) / 0.2f)
                }
                sb.setColor(1f, 1f, 1f, alpha)
                sb.draw(slotTexOverlay, orbPos.x, orbPos.y, sz.x/2f, sz.y/2f, sz.x, sz.y, scale, scale, 0f)
            }

            // Reset color
            sb.setColor(1f, 1f, 1f, 1f)
        }
    }

    companion object {
        val slotOrbSize = Vector2(75f, 75f)

        fun getSlotPositions(slots: Int, slotSize: Vector2): FloatArray {
            val scale = Settings.scale
            val origin = Vector2(AbstractDungeon.player.drawX, AbstractDungeon.player.drawY)
            origin.y += 380f * scale
            val scaleX = 80f * scale

            val unitOffsetX = -(slots / 2).toFloat() - if (slots % 2 == 0) { 0.5f } else { 0.0f }
            val out = FloatArray(slots * 2)
            for (i in 0 until slots) {
                // Find x-position in unit scale (e.g. -2 to +2) then multiply by scaleX to convert to pixels
                val offX = (i.toFloat() + unitOffsetX) * scaleX
                out[i] = MathUtils.round(offX).toFloat()
                out[i + 1] = origin.y
            }
            return out
        }

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

}