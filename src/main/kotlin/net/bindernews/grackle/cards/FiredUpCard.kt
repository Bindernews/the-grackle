package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.power.FiredUpPower

class FiredUpCard : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(ApplyPowerAction(p, p, FiredUpPower(p, magicNumber)))
    }

    companion object {
        @JvmField val C = CardConfig("FiredUpCard", CardType.POWER, CardRarity.RARE, CardTarget.SELF)
        val VARS = CardVariables.config { c ->
            c.cost(2, 1)
            c.magic(8, -1)
        }
    }
}