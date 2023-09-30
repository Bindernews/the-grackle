package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.RegenPower
import net.bindernews.grackle.helper.CardVariables

class HangarMaintenance : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(iop.actionApplyPower(p, p, RegenPower.POWER_ID, magicNumber))
    }

    companion object {
        @JvmField val C = CardConfig("HangarMaintenance", CardType.POWER, CardRarity.RARE, CardTarget.SELF)
        val VARS = CardVariables.config { c ->
            c.cost(2, 1)
            c.magic(5)
        }
    }
}