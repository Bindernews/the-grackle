package net.bindernews.grackle.stance

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.stances.AbstractStance
import com.megacrit.cardcrawl.vfx.stance.StanceAuraEffect
import com.megacrit.cardcrawl.vfx.stance.WrathParticleEffect
import net.bindernews.grackle.GrackleMod
import net.bindernews.grackle.helper.MiscUtil.addToBot
import net.bindernews.grackle.helper.ModInterop.Companion.iop
import net.bindernews.grackle.power.MultiHitPower
import net.bindernews.grackle.power.PeckingOrderPower
import net.bindernews.grackle.relics.BorrowedMoment

class StanceEagle : AbstractStance(), StanceDelegate {
    var owner: AbstractCreature = AbstractDungeon.player

    val slots = GrackleSlots("Eagle Stance", "Gain 2 Multi-Hit")

    init {
        ID = STANCE_ID
        name = STRINGS.NAME
        updateDescription()
    }

    override fun getDescription(): String = description

    override fun updateDescription() {
        description = STRINGS.DESCRIPTION[0].format(BUF_AMOUNT)
    }

    fun technique() {
        addToBot(iop().actionApplyPower(owner, owner, MultiHitPower.POWER_ID, BUF_AMOUNT))
    }

    override fun onEnterStance() {
        if (AbstractDungeon.player.hasRelic(BorrowedMoment.ID)) {
            slots.count = 4
        } else {
            slots.count = 3
        }
        slots.resetSlots()
    }

    override fun onExitStance() {
        addToBot(iop().actionApplyPower(owner, owner, PeckingOrderPower.POWER_ID, BUF_AMOUNT))
    }

    override fun onPlayCard(card: AbstractCard) {
        if (card.type == AbstractCard.CardType.SKILL && slots.spendSlot()) {
            technique()
        }
    }

    override fun updateAnimation() {
        if (!Settings.DISABLE_EFFECTS) {
            particleTimer -= Gdx.graphics.deltaTime
            if (particleTimer < 0.0f) {
                particleTimer = 0.0f
                AbstractDungeon.effectsQueue.add(WrathParticleEffect())
            }
        }

        particleTimer2 -= Gdx.graphics.deltaTime
        if (particleTimer2 < 0.0f) {
            particleTimer2 = MathUtils.random(0.45f, 0.55f)
            AbstractDungeon.effectsQueue.add(StanceAuraEffect("Wrath"))
        }
    }

    override fun update() {
        super.update()
        slots.update()
    }

    override fun render(sb: SpriteBatch) {
        super.render(sb)
        slots.render(sb)
    }

    companion object {
        @JvmField val STANCE_ID = GrackleMod.makeId("StanceEagle")
        @JvmStatic val STRINGS = CardCrawlGame.languagePack.getStanceString(STANCE_ID)!!

        /**
         * How many pecking order to add
         */
        var BUF_AMOUNT = 2
    }
}