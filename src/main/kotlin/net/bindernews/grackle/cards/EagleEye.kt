package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.utility.ScryAction
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.helper.ModInterop.Companion.iop
import net.bindernews.grackle.stance.StanceEagle

class EagleEye : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(ScryAction(magicNumber))
        addToBot(iop().changeStance(p, StanceEagle.STANCE_ID))
    }

    companion object {
        @JvmStatic val C = CardConfig(this, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF)
        val VARS = CardVariables().apply {
            cost(1)
            magic(3, 5)
        }
    }
}