package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.power.FireWithinPower

class FireWithin : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(ApplyPowerAction(p, p, FireWithinPower(p, magicNumber)))
    }

    companion object {
        @JvmField val C = CardConfig("FireWithin", CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF)
        val VARS = CardVariables.config { c ->
            c.cost(1)
            c.magic(2, 3)
        }
    }
}