package net.bindernews.grackle.cards

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.watcher.VigorPower
import net.bindernews.grackle.helper.CardVariables

class TargetingComputer : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(ApplyPowerAction(p, p, VigorPower(p, magicNumber), magicNumber))
    }

    companion object {
        @JvmField val C = CardConfig("TargetingComputer", CardType.SKILL, CardRarity.COMMON, CardTarget.SELF)
        val VARS = CardVariables.config { c ->
            c.cost(1)
            c.magic(3, 6)
        }
    }
}