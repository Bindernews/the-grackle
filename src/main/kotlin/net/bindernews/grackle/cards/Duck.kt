package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.actions.common.GainBlockAction
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.BlurPower
import net.bindernews.grackle.GrackleMod
import net.bindernews.grackle.actions.SoundAction
import net.bindernews.grackle.helper.CardVariables

class Duck : BaseCard(C, VARS) {
    /**
     * Set to false to disable quack sound.
     */
    var playSound = true
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(GainBlockAction(p, p, block))
        addToBot(ApplyPowerAction(p, p, BlurPower(p, magicNumber), magicNumber))
        if (playSound) {
            addToBot(SoundAction(GrackleMod.CO.SFX_QUACK))
        }
    }

    companion object {
        @JvmField
        val C = CardConfig("Duck", CardType.SKILL, CardRarity.COMMON, CardTarget.SELF)
        val VARS = CardVariables.config { c ->
            c.cost(1, -1)
            c.block(7, 11)
            c.magic(1, -1)
        }
    }
}