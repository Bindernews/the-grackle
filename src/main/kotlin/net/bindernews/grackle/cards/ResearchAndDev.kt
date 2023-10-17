package net.bindernews.grackle.cards

import basemod.cardmods.RetainMod
import basemod.helpers.CardModifierManager.addModifier
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.ModInterop.Companion.iop
import net.bindernews.grackle.power.ResearchAndDevPower

class ResearchAndDev : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(iop().actionApplyPower(p, p, ResearchAndDevPower.POWER_ID, magicNumber))
    }

    companion object {
        @JvmField val C = CardConfig("ResearchAndDev", CardType.POWER, CardRarity.RARE, CardTarget.SELF)
        @JvmField val VARS = CardVariables().apply {
            cost(2)
            magic(1, -1)
            onUpgrade { addModifier(it, RetainMod()) }
        }
    }
}