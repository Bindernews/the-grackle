package net.bindernews.grackle.cards

import basemod.cardmods.ExhaustMod
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.core.AbstractCreature
import net.bindernews.grackle.helper.CardVariables
import net.bindernews.grackle.power.PeckingOrderPower

class PeckingOrder : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature?) {
        addToBot(ApplyPowerAction(p, p, PeckingOrderPower(p, magicNumber), magicNumber))
    }

    companion object {
        val C = CardConfig("PeckingOrder", CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF)
        val VARS = CardVariables().apply {
            cost(1, -1)
            magic(4, 6)
            addModifier(ExhaustMod())
        }
    }
}