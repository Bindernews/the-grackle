package net.bindernews.grackle.cards

import basemod.cardmods.InnateMod
import basemod.helpers.CardModifierManager
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.ModInterop.Companion.iop
import net.bindernews.grackle.power.EmbodyFirePower

class EmbodyFire : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(iop().actionApplyPower(p, p, EmbodyFirePower.POWER_ID, -1))
    }
    companion object {
        @JvmStatic val C = CardConfig("EmbodyFire", CardType.POWER, CardRarity.RARE, CardTarget.SELF)
        @JvmStatic val VARS = CardVariables().apply {
            cost(2)
            onUpgrade { CardModifierManager.addModifier(it, InnateMod()) }
        }
    }
}