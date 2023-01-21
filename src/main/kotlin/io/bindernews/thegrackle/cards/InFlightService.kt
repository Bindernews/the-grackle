package io.bindernews.thegrackle.cards

import basemod.cardmods.ExhaustMod
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.potions.RegenPotion
import com.megacrit.cardcrawl.powers.RegenPower
import io.bindernews.bnsts.CardVariables
import io.bindernews.thegrackle.helper.ModInterop.iop

class InFlightService : BaseCard(C, VARS) {
    override fun apply(p: AbstractCreature, m: AbstractCreature) {
        if (p is AbstractPlayer) {
            addToBot(ObtainPotionAction(RegenPotion()))
        } else {
            addToBot(iop().actionApplyPower(p, p, RegenPower.POWER_ID, magicNumber))
        }
    }

    companion object {
        @JvmStatic val C = CardConfig("InFlightService", CardType.SKILL, CardRarity.RARE, CardTarget.SELF)
        val VARS = CardVariables.config { c ->
            c.cost(2, 1)
            c.magic(5, -1)
            c.addModifier(ExhaustMod())
        }
    }
}