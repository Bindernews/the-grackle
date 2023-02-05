package io.bindernews.thegrackle.cards

import basemod.cardmods.ExhaustMod
import basemod.cardmods.RetainMod
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.stances.NeutralStance
import io.bindernews.thegrackle.helper.CardVariables
import io.bindernews.thegrackle.helper.ModInterop.Companion.iop

class GentleLanding : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(iop().changeStance(p, NeutralStance.STANCE_ID))
    }

    companion object {
        @JvmStatic val C = CardConfig("GentleLanding", CardType.SKILL, CardRarity.COMMON, CardTarget.SELF)
        val VARS = CardVariables().apply {
            cost(1, 0)
            addModifier(ExhaustMod())
            addModifier(RetainMod())
        }
    }
}