package net.bindernews.grackle.cards

import basemod.AutoAdd
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.power.PlaguePower

@AutoAdd.Ignore
class Plague : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(iop.actionApplyPower(p, p, PlaguePower.POWER_ID, magicNumber))
    }

    companion object {
        @JvmStatic val C = CardConfig("Plague", CardType.POWER, CardRarity.RARE, CardTarget.SELF)
        @JvmStatic val VARS = CardVariables().apply {
            cost(3, 2)
            magic(1, 2)
        }
    }
}