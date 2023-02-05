package io.bindernews.thegrackle.cards

import com.megacrit.cardcrawl.core.AbstractCreature
import io.bindernews.bnsts.CardVariables
import io.bindernews.thegrackle.helper.ModInterop.Companion.iop
import io.bindernews.thegrackle.power.MultiHitPower

class BePrepared : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(iop().actionApplyPower(p, p, MultiHitPower.POWER_ID, magicNumber))
    }

    companion object {
        @JvmStatic val C = CardConfig("BePrepared", CardType.SKILL, CardRarity.COMMON, CardTarget.SELF)
        @JvmStatic val VARS = CardVariables().apply {
            cost(1, 0)
            magic(1, -1)
        }
    }
}