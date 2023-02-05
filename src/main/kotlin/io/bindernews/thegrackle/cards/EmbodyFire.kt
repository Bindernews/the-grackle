package io.bindernews.thegrackle.cards

import basemod.cardmods.InnateMod
import basemod.helpers.CardModifierManager
import com.megacrit.cardcrawl.core.AbstractCreature
import io.bindernews.thegrackle.helper.CardVariables
import io.bindernews.thegrackle.helper.ModInterop.Companion.iop
import io.bindernews.thegrackle.power.EmbodyFirePower

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