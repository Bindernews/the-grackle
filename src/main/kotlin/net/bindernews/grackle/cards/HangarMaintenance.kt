package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.RegenPower
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.ModInterop.Companion.iop

class HangarMaintenance : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        val power = iop().createPower(RegenPower.POWER_ID, p, magicNumber)
        addToBot(ApplyPowerAction(p, p, power, magicNumber))
    }

    companion object {
        @JvmField val C = CardConfig("HangarMaintenance", CardType.POWER, CardRarity.RARE, CardTarget.SELF)
        val VARS = CardVariables.config { c ->
            c.cost(2, 1)
            c.magic(5)
        }
    }
}