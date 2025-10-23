package net.bindernews.grackle.cards

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.evacipated.cardcrawl.mod.stslib.patches.HitboxRightClick
import com.megacrit.cardcrawl.actions.common.GainBlockAction
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.bnsts.api.FlippableCard
import net.bindernews.bnsts.vfx.FlipCardEffect
import net.bindernews.grackle.GrackleMod
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.MiscUtil

class Snaaaake : BaseCard(C, VARS), FlippableCard {
    private var flipEffect: FlipCardEffect? = null

    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        MiscUtil.addToBot(GainBlockAction(p, magicNumber))
    }

    override fun update() {
        super.update()

        val flipFx = flipEffect
        if (hb.hovered && HitboxRightClick.rightClicked.get(hb) && flipFx == null) {
            val angle0 = if (isFlipped) MathUtils.PI else 0f
            flipEffect = FlipCardEffect(this, MathUtils.PI, angle0).withDuration(FLIP_FX_TIME)
            flipEffect!!.enqueue()
        }
        if (flipFx != null && flipFx.isDone) {
            GrackleMod.log.info("Card flip done!")
            isFlipped = !isFlipped
            flipEffect = null
        }
    }

    override fun render(sb: SpriteBatch, selected: Boolean) {
        val flipFx = flipEffect
        if (flipFx != null) {
            flipFx.renderEffect(sb)
        } else {
            super.render(sb, selected)
        }
    }

    override fun getCardDirection(): Int {
        return if (isFlipped) 1 else 0
    }

    override fun setCardDirection(mode: Int) {
    }

    companion object {
        @JvmField val C = CardConfig("Snaaaake", CardType.SKILL, CardRarity.COMMON, CardTarget.SELF).apply {
            parseDescription = true
        }
        @JvmField val VARS = CardVariables().apply {
            cost(1, -1)
            block(8, 11)
            magic(1, -1)
        }

        val FLIP_FX_TIME = 0.6f
    }
}