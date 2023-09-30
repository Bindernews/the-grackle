package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.LoseStrengthPower
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.power.BurningPower

class BurningBird : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(iop.actionApplyPower(p, p, "Strength", magicNumber))
        addToBot(iop.actionApplyPower(p, p, LoseStrengthPower.POWER_ID, magicNumber))
        addToBot(BurningPower.makeAction(p, p, block))
    }

    companion object {
        @JvmField val C = CardConfig("BurningBird", CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF)
        val VARS = CardVariables().apply {
            cost(0)
            magic(2, 4)
            block(2)
        }
    }
}