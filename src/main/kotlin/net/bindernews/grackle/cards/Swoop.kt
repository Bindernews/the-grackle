package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.stances.NeutralStance
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.ModInterop.Companion.iop
import net.bindernews.grackle.power.SwoopPower

class Swoop : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(iop().changeStance(p, NeutralStance.STANCE_ID))
        addToBot(ApplyPowerAction(p, p, SwoopPower(p, -1)))
    }

    companion object {
        @JvmField val C = CardConfig("Swoop", CardType.SKILL, CardRarity.RARE, CardTarget.SELF)
        val VARS = CardVariables.config { c: CardVariables -> c.cost(2, 1) }
    }
}