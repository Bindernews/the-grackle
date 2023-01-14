package io.bindernews.thegrackle.cards

import com.megacrit.cardcrawl.core.AbstractCreature
import io.bindernews.bnsts.CardVariables
import io.bindernews.thegrackle.helper.ModInterop.iop
import io.bindernews.thegrackle.power.ResearchAndDevPower

class ResearchAndDev : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(iop().actionApplyPower(p, p, ResearchAndDevPower.POWER_ID, magicNumber))
    }

    companion object {
        @JvmStatic val C = CardConfig("ResearchAndDev", CardType.POWER, CardRarity.RARE, CardTarget.SELF)
        @JvmStatic val VARS = CardVariables().apply {
            cost(2)
            magic(1, -1)
            onUpgrade { it.retain = true }
        }
    }
}