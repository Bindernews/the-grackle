package io.bindernews.thegrackle.cards

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.core.AbstractCreature
import io.bindernews.bnsts.CardVariables
import io.bindernews.thegrackle.power.FiredUpPower

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