package io.bindernews.thegrackle.cards

import com.megacrit.cardcrawl.actions.common.GainBlockAction
import com.megacrit.cardcrawl.core.AbstractCreature
import io.bindernews.bnsts.CardVariables
import io.bindernews.thegrackle.helper.ModInterop.iop
import io.bindernews.thegrackle.power.TempThornsPower

class WindowPain : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(GainBlockAction(p, p, block))
        addToBot(iop().actionApplyPower(p, p, TempThornsPower.POWER_ID, magicNumber))
    }

    companion object {
        @JvmStatic val C = CardConfig("WindowPain", CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF)
        val VARS = CardVariables().apply {
            cost(1)
            block(6, 10)
            magic(1, 2)
        }
    }
}