package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.ModInterop.Companion.iop
import net.bindernews.grackle.power.MultiHitPower

class BePrepared : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(iop().actionApplyPower(p, p, MultiHitPower.POWER_ID, magicNumber))
    }

    companion object {
        @JvmField val C = CardConfig("BePrepared", CardType.SKILL, CardRarity.COMMON, CardTarget.SELF)
        val VARS = CardVariables().apply {
            cost(1, 0)
            magic(1, -1)
        }
    }
}