package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.stances.NeutralStance
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.power.SwoopPower

class Swoop : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(iop.changeStance(p, NeutralStance.STANCE_ID))
        addToBot(iop.actionApplyPower(p, p, SwoopPower.POWER_ID, -1))
    }

    companion object {
        @JvmStatic val C = CardConfig("Swoop", CardType.SKILL, CardRarity.RARE, CardTarget.SELF)
        val VARS = CardVariables.config { c: CardVariables -> c.cost(2, 1) }
    }
}