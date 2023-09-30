package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.watcher.VigorPower
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.stance.StanceEagle

class Perch : BaseCard(C, VARS) {

    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(iop.actionGainEnergy(p, cost))
        addToBot(iop.actionApplyPower(p, p, VigorPower.POWER_ID, magicNumber))
        addToBot(iop.changeStance(p, StanceEagle.STANCE_ID))
    }

    companion object {
        @JvmStatic val C = CardConfig("Perch", CardType.SKILL, CardRarity.COMMON, CardTarget.SELF)
        val VARS = CardVariables().apply {
            cost(1)
            magic(2, 3)
        }
    }
}