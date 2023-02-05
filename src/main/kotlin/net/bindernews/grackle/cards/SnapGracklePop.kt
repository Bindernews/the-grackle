package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.VulnerablePower
import com.megacrit.cardcrawl.powers.WeakPower
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.ModInterop.Companion.iop
import net.bindernews.grackle.helper.magic2

/**
 * Art should be of bones breaking or something.
 */
class SnapGracklePop : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        if (m != null) {
            addToBot(iop().actionApplyPower(p, m, VulnerablePower.POWER_ID, magicNumber))
            addToBot(iop().actionApplyPower(p, m, WeakPower.POWER_ID, magic2))
        }
    }

    companion object {
        @JvmStatic val C = CardConfig("SnapGracklePop", CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY)
        @JvmStatic val VARS = CardVariables().apply {
            cost(1)
            magic(2, 3)
            magic2(1, 2)
        }
    }
}