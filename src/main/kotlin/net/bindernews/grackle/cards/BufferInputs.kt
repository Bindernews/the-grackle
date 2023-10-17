package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.cardmods.AutoDescription
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.power.SpeedPower
import net.bindernews.grackle.power.SpeedPreservePower

class BufferInputs : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(iop.actionApplyPower(p, p, SpeedPower.POWER_ID, magicNumber))
        addToBot(iop.actionApplyPower(p, p, SpeedPreservePower.POWER_ID, 1))
    }

    companion object {
        @JvmField val C = CardConfig("BufferInputs", CardType.SKILL, CardRarity.RARE, CardTarget.SELF)
        val VARS = CardVariables.config { c ->
            c.cost(2)
            c.magic(2, 3)
            c.addModifier(AutoDescription())
            c.onUpgrade {
                it.selfRetain = true
                it.retain = true
                it.initializeDescription()
            }
        }
    }
}