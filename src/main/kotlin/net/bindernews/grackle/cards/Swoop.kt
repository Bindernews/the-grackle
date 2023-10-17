package net.bindernews.grackle.cards

import basemod.AutoAdd
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.stances.NeutralStance
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.power.SwoopPower

@AutoAdd.Ignore
class Swoop : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(iop.changeStance(p, NeutralStance.STANCE_ID))
        addToBot(iop.actionApplyPower(p, p, SwoopPower.POWER_ID, -1))
    }

    companion object {
        @JvmField val C = CardConfig("Swoop", CardType.SKILL, CardRarity.RARE, CardTarget.SELF)
        @JvmField val VARS = CardVariables.config { c: CardVariables -> c.cost(2, 1) }
    }
}