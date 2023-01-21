package io.bindernews.thegrackle.cards

import basemod.cardmods.ExhaustMod
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.core.AbstractCreature
import io.bindernews.bnsts.CardVariables
import io.bindernews.thegrackle.power.PeckingOrderPower

class PeckingOrder : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature) {
        addToBot(ApplyPowerAction(p, p, PeckingOrderPower(p, magicNumber), magicNumber))
    }

    companion object {
        val C = CardConfig("PeckingOrder", CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF)
        val VARS = CardVariables.config { c: CardVariables ->
            c.cost(1, -1)
            c.magic(4, 6)
            c.addModifier(ExhaustMod())
        }
    }
}