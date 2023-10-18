package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.helper.CardVariables

class TargetingComputer : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(iop.actionApplyPower(p, p, "Vigor", magicNumber))
    }

    companion object {
        @JvmField val C = CardConfig("TargetingComputer", CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF)
        val VARS = CardVariables.config { c ->
            c.cost(1)
            c.magic(2, 4)
        }
    }
}